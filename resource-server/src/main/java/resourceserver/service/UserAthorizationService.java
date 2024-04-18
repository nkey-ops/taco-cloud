package resourceserver.service;

import java.util.Base64;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.oidc.http.converter.OidcClientRegistrationHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

import resourceserver.data.UserRepository;

@Component
/** */
public class UserAthorizationService {
  Logger log = LoggerFactory.getLogger(UserAthorizationService.class);

  private final String auhorizationServerUrl;
  private final String authorizationServerTokenEndpoint;
  private String authorizationServerAuthorizationEndpoint;

  private final String clientId;
  private final String redirectUri;
  private final String basicAuthorizaionEncodedClientCredentials;

  private final RestClient restClient;
  private final UserRepository userRepository;

  public UserAthorizationService(
      @Value("${spring.security.oauth2.client.provider.auth-server.issuer-uri}")
          String authorizationServerUrl,
      @Value("${spring.security.oauth2.client.registration.auth-server.client-id}") String clientId,
      @Value("${spring.security.oauth2.client.registration.auth-server.client-secret}")
          String clientSecret,
      @Value("${spring.security.oauth2.client.registration.auth-server.redirect-uri}")
          String redirectUri,
      RestClient.Builder restBuilder,
      UserRepository userRepository) {

    Objects.requireNonNull(authorizationServerUrl);
    Objects.requireNonNull(clientId);
    Objects.requireNonNull(clientSecret);

    this.auhorizationServerUrl = authorizationServerUrl;

    this.clientId = clientId;
    this.redirectUri = redirectUri;
    this.basicAuthorizaionEncodedClientCredentials =
        Base64.getEncoder()
            .encodeToString(String.format("%s:%s", clientId, clientSecret).getBytes());

    this.userRepository = userRepository;
    this.authorizationServerAuthorizationEndpoint = authorizationServerUrl + "/oauth2/authorize";
    this.authorizationServerTokenEndpoint = authorizationServerUrl + "/oauth2/token";

    var restClientHttpMessageConverters =
        List.of(
            new OidcClientRegistrationHttpMessageConverter(),
            new OAuth2AccessTokenResponseHttpMessageConverter());

    this.restClient =
        restBuilder
            .baseUrl(authorizationServerUrl)
            .messageConverters(converter -> converter.addAll(0, restClientHttpMessageConverters))
            .build();
  }

  public RegisteredClient askUserToGrantAccessRequest() {

    var httpHeaders = new HttpHeaders();
    httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);

    var body = new LinkedMultiValueMap<>();
    body.set("response_type", "code");
    body.set("client_id", clientId);
    body.set("redirect_uri", redirectUri);

    var responseEntity =
        restClient
            .post()
            .uri(authorizationServerAuthorizationEndpoint)
            .headers(h -> h.addAll(httpHeaders))
            .body(body)
            .retrieve()
            .toEntity(OAuth2AccessTokenResponse.class);

    // if (responseEntity.getStatusCode() == HttpStatus.FOUND) {
    //
    // responseEntity.getHeaders()
    //
    //   responseEntity = redirect(responseEntity);
    // }
    //
    if (responseEntity.getStatusCode() != HttpStatus.OK) {
      throw new RuntimeException(
          String.format(
              "Authorization Request wasn't successfull due to [%s]: %s ",
              responseEntity.getStatusCode(), responseEntity.getBody()));
    }

    var accessTokenResponse = responseEntity.getBody();

    log.debug(
        "Response: {}{}{}{}{}{}{}",
        String.format(
            "%s %s",
            accessTokenResponse.getAccessToken().getTokenValue(),
            System.lineSeparator(),
            accessTokenResponse.getAccessToken().getScopes()),
        System.lineSeparator(),
        accessTokenResponse.getRefreshToken(),
        System.lineSeparator(),
        accessTokenResponse.getAdditionalParameters(),
        System.lineSeparator(),
        responseEntity.getHeaders());

    return null;
  }

  private ResponseEntity<OAuth2AccessTokenResponse> redirect(
      ResponseEntity<OAuth2AccessTokenResponse> responseEntity) {
    Objects.requireNonNull(responseEntity);

    return null;
  }
}
