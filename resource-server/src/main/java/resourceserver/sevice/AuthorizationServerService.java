package resourceserver.sevice;

import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.oidc.OidcClientRegistration;
import org.springframework.security.oauth2.server.authorization.oidc.http.converter.OidcClientRegistrationHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import jakarta.validation.constraints.NotNull;
import resourceserver.data.UserRepository;
import resourceserver.domain.User;

@Component
public class AuthorizationServerService {
  Logger log = LoggerFactory.getLogger(AuthorizationServerService.class);

  private final String encodedCredentials;
  private final String resourceServerURL;
  private final String baseURL = "localhost:9999";

  private final RestTemplate restTemplate;
  private final RestClient restClient;

  private final UserRepository userRepository;

  public AuthorizationServerService(
      @Value("${auth-server.client-registrar.client-id}") String clientId,
      @Value("${auth-server.client-registrar.secret}") String clientSecret,
      @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}") String resourceServerURL,
      RestTemplateBuilder restTemplateBuilder,
      RestClient.Builder restBuilder,
      UserRepository userRepository) {

    this.encodedCredentials =
        Base64.getEncoder()
            .encodeToString(String.format("%s:%s", clientId, clientSecret).getBytes());
    this.resourceServerURL = resourceServerURL;
    this.restTemplate = restTemplateBuilder.build();
    this.userRepository = userRepository;

    this.restTemplate
        .getMessageConverters()
        .add(0, new OidcClientRegistrationHttpMessageConverter());

    this.restClient =
        RestClient.builder()
            .baseUrl(resourceServerURL)
            .messageConverters(
                converter -> converter.add(0, new OidcClientRegistrationHttpMessageConverter()))
            .build();
  }

  @NotNull
  private String getClientRegistrationToken() {
    var headers = new HttpHeaders();
    headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
    headers.set(HttpHeaders.AUTHORIZATION, "Basic " + encodedCredentials);

    var body = new LinkedMultiValueMap<String, String>(2);
    body.add("grant_type", "client_credentials");
    body.add("scope", "client.create");

    var requestEntity =
        RequestEntity.post(resourceServerURL + "/oauth2/token").headers(headers).body(body);

    var responseEntity =
        restTemplate.exchange(
            requestEntity, new ParameterizedTypeReference<Map<String, String>>() {});

    if (responseEntity.getStatusCode() != HttpStatus.OK) {
      throw new IllegalStateException(
          String.format(
              "The token wasn't receieved form auth-server: %s due to [%s]: %s",
              resourceServerURL, responseEntity.getStatusCode(), responseEntity.getBody()));
    }

    log.debug(
        "Received Response: {}{}{}",
        responseEntity.getBody(),
        System.lineSeparator(),
        responseEntity.getHeaders());

    var response = responseEntity.getBody();
    Objects.requireNonNull(response, "Response body cannot be empty");

    if (!response.containsKey("access_token")
        || !response.containsKey("scope")
        || !response.containsKey("token_type")
        || !response.containsKey("expires_in")) {

      throw new IllegalStateException(
          String.format(
              "Responce body doesn't contain one of paraemters: access_token, scope, token_type or"
                  + " expires_in. Body: %s%s",
              response, System.lineSeparator()));
    }

    var token = response.get("access_token");
    Objects.requireNonNull(token, "Token cannot be null");

    return token;
  }

  public void createRegistereClient(User user) {
    Objects.requireNonNull(user);

    var headers = new HttpHeaders();
    headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + getClientRegistrationToken());
    headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

    var clientRegistration =
        OidcClientRegistration.builder()
            .clientName(user.getUsername())
            .redirectUri(baseURL + "/login/oauth2/code/" + user.getUsername())
            .build();

    var requestEntity =
        RequestEntity.post(resourceServerURL + "/connect/register")
            .headers(headers)
            .body(clientRegistration);

    var responseEntity = restTemplate.exchange(requestEntity, OidcClientRegistration.class);

    if (responseEntity.getStatusCode() != HttpStatus.CREATED) {
      throw new IllegalStateException(
          String.format(
              "The authorization server client wasn't created due to [%s]: %s",
              responseEntity.getStatusCode(), responseEntity.getBody()));
    }

    var sbBody = new StringBuilder();

    responseEntity
        .getBody()
        .getClaims()
        .forEach((k, v) -> sbBody.append(k + " " + v + System.lineSeparator()));
    sbBody.append(System.lineSeparator());
    responseEntity
        .getHeaders()
        .forEach((k, v) -> sbBody.append(k + " " + v + System.lineSeparator()));

    log.debug("Received Response: {}", sbBody.toString());

    var regClient = responseEntity.getBody();
    user.setClientId(regClient.getClientId());
    user.setRegistrationAccessToken(regClient.getRegistrationAccessToken());
    userRepository.save(user);
  }
}
