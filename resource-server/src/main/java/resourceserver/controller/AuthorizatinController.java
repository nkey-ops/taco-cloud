package resourceserver.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import resourceserver.sevice.AuthorizationServerService;

@RestController
@RequestMapping(path = "/auth")
public class AuthorizatinController {

  private final AuthorizationServerService authorizationServerService;

  public AuthorizatinController(AuthorizationServerService authorizationServerService) {
    this.authorizationServerService = authorizationServerService;
  }

}
