package authserver.controller.model;

import java.util.Set;

import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

import lombok.Getter;

@Getter
public class ClientRegistration {

  private final String clientId;
  private final String clientSecret;
  private final Set<String> scopes;

  private String clientName;
  private Set<ClientAuthenticationMethod> clientAuthenticationMethods;
  private Set<AuthorizationGrantType> authorizationGrantTypes;
  private Set<String> redirectUris;

  public ClientRegistration(
      String clientId, String clientSecret, Set<String> redirectUris, Set<String> scopes) {
    this.clientId = clientId;
    this.clientSecret = clientSecret;
    this.redirectUris = redirectUris;
    this.scopes = scopes;
  }
}
