package authserver.controller;

import org.springframework.http.MediaType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import authserver.controller.model.ClientRegistration;

@RestController("/client")
public class ClientRegistrationRestController {

  private final RegisteredClientRepository registeredClientRepository;

  public ClientRegistrationRestController(RegisteredClientRepository registeredClientRepository) {
    this.registeredClientRepository = registeredClientRepository;
  }

  @PostMapping(
      consumes = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
      produces = MediaType.APPLICATION_PROBLEM_JSON_VALUE)
  public void register(ClientRegistration clientRegistration) {}
}
