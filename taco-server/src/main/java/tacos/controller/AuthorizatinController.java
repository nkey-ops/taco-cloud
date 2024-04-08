package tacos.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tacos.domain.User;
import tacos.service.AuthorizationService;

@RestController
@RequestMapping(path = "/auth")
public class AuthorizatinController {

  private final AuthorizationService authorizationServerService;

  public AuthorizatinController(AuthorizationService authorizationServerService) {
    this.authorizationServerService = authorizationServerService;
  }

  @GetMapping("/clientinfo")
  public ResponseEntity<RegisteredClient> getAuthorizationServerUserInfo(
      @AuthenticationPrincipal User user) {

    return ResponseEntity.ok(authorizationServerService.getClientInfo(user));
  }
}
