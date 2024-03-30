package resourceserver.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import resourceserver.domain.User;
import resourceserver.sevice.AuthorizationServerService;

@RestController
@RequestMapping(path = "/auth")
public class AuthorizatinController {

  private final AuthorizationServerService authorizationServerService;

  public AuthorizatinController(AuthorizationServerService authorizationServerService) {
    this.authorizationServerService = authorizationServerService;
  }

  @GetMapping("/clientinfo")
  public ResponseEntity<RegisteredClient> getAuthorizationServerUserInfo(
      @AuthenticationPrincipal User user) {

    return ResponseEntity.ok(authorizationServerService.getClientRest(user));
  }
}
