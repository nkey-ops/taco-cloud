package resourceserver.sevice;

import jakarta.validation.constraints.NotNull;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.oidc.OidcClientRegistration;
import org.springframework.security.oauth2.server.authorization.oidc.converter.OidcClientRegistrationRegisteredClientConverter;
import org.springframework.security.oauth2.server.authorization.oidc.http.converter.OidcClientRegistrationHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import resourceserver.data.UserRepository;
import resourceserver.domain.User;

@Component
public class AuthorizationServerService {
  Logger log = LoggerFactory.getLogger(AuthorizationServerService.class);

  private final String encodedCredentials;
  private final String resourceServerUrl;
  private final String baseUrl = "localhost:9999";

  private final RestClient restClient;
  private final UserRepository userRepository;

  private final OidcClientRegistrationRegisteredClientConverter
      oidcClientRegistrationRegisteredClientConverter =
          new OidcClientRegistrationRegisteredClientConverter();

  private final String tokenEndpoint;
  private final String clientRegistrationEndpoint;

  public AuthorizationServerService(
      @Value("${auth-server.client-registrar.client-id}") String clientId,
      @Value("${auth-server.client-registrar.secret}") String clientSecret,
      @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}") String resourceServerUrl,
      RestClient.Builder restBuilder,
      UserRepository userRepository) {

    this.encodedCredentials =
        Base64.getEncoder()
            .encodeToString(String.format("%s:%s", clientId, clientSecret).getBytes());
    this.resourceServerUrl = resourceServerUrl;
    this.userRepository = userRepository;

    this.tokenEndpoint = resourceServerUrl + "/oauth2/token";
    this.clientRegistrationEndpoint = resourceServerUrl + "/connect/register";

    var converters =
        List.of(
            new OidcClientRegistrationHttpMessageConverter(),
            new OAuth2AccessTokenResponseHttpMessageConverter());

    this.restClient =
        RestClient.builder()
            .baseUrl(resourceServerUrl)
            .messageConverters(converter -> converter.addAll(0, converters))
            .build();
  }

  @NotNull
  private OAuth2AccessToken getClientRegistrationAccessToken() {
    var headers = new HttpHeaders();
    headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
    headers.set(HttpHeaders.AUTHORIZATION, "Basic " + encodedCredentials);

    var body = new LinkedMultiValueMap<String, String>(2);
    body.add("grant_type", "client_credentials");
    body.add("scope", "client.create");

    var responseEntity =
        restClient
            .post()
            .uri(tokenEndpoint)
            .headers(h -> h.addAll(headers))
            .body(body)
            .retrieve()
            .toEntity(OAuth2AccessTokenResponse.class);

    if (responseEntity.getStatusCode() != HttpStatus.OK) {
      throw new IllegalStateException(
          String.format(
              "The token wasn't receieved form auth-server: %s due to [%s]: %s",
              resourceServerUrl, responseEntity.getStatusCode(), responseEntity.getBody()));
    }

    log.debug(
        "Response: {}{}{}",
        responseEntity.getBody(),
        System.lineSeparator(),
        responseEntity.getHeaders());

    var response = responseEntity.getBody();
    Objects.requireNonNull(response, "Response body cannot be empty");

    return response.getAccessToken();
  }

  public RegisteredClient createRegistereClient(User user) {
    Objects.requireNonNull(user);
    OAuth2AccessToken clientRegistrationAccessToken = getClientRegistrationAccessToken();

    var headers = new HttpHeaders();
    headers.add(
        HttpHeaders.AUTHORIZATION, "Bearer " + clientRegistrationAccessToken.getTokenValue());
    headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

    var clientRegistration =
        OidcClientRegistration.builder()
            .clientName(user.getUsername())
            .redirectUri(baseUrl + "/login/oauth2/code/" + user.getUsername())
            .build();

    var responseEntity =
        restClient
            .post()
            .uri(clientRegistrationEndpoint)
            .headers(h -> h.addAll(headers))
            .body(clientRegistration)
            .retrieve()
            .toEntity(OidcClientRegistration.class);

    if (responseEntity.getStatusCode() != HttpStatus.CREATED) {
      throw new IllegalStateException(
          String.format(
              "The authorization server client wasn't created due to [%s]: %s",
              responseEntity.getStatusCode(), responseEntity.getBody()));
    }

    log.debug(
        "Response: {}{}{}",
        responseEntity.getStatusCode() + System.lineSeparator(),
        responseEntity.getHeaders() + System.lineSeparator(),
        responseEntity.getBody().getClaims());

    var registeredClient = responseEntity.getBody();
    user.setClientId(registeredClient.getClientId());
    // TODO Time-limited
    user.setRegistrationAccessToken(registeredClient.getRegistrationAccessToken());

    user = userRepository.save(user);

    return oidcClientRegistrationRegisteredClientConverter.convert(registeredClient);
  }

  @NotNull
  public RegisteredClient getClientInfo(User user) {
    Objects.requireNonNull(user);
    var clientId =
        user.getClientId()
            .orElseThrow(
                () -> new IllegalArgumentException("User doesn't have client credentials"));

    var registrationAccessToken =
        user.getRegistrationAccessToken()
            .orElseThrow(
                () -> new IllegalArgumentException("User doesn't have client credentials"));

    var headers = new HttpHeaders();
    headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + registrationAccessToken);
    headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

    var responseEntity =
        restClient
            .get()
            .uri(clientRegistrationEndpoint + "?client_id=" + clientId)
            .headers(h -> h.addAll(headers))
            .retrieve()
            .toEntity(OidcClientRegistration.class);

    if (responseEntity.getStatusCode() != HttpStatus.OK) {
      throw new IllegalStateException(
          String.format(
              "The authorization server's client info wasn't returned due to [%s]: %s",
              responseEntity.getStatusCode(), responseEntity.getBody()));
    }

    log.debug(
        "Response: {}{}{}",
        responseEntity.getStatusCode() + System.lineSeparator(),
        responseEntity.getHeaders() + System.lineSeparator(),
        responseEntity.getBody().getClaims());

    return oidcClientRegistrationRegisteredClientConverter.convert(responseEntity.getBody());
  }
}
