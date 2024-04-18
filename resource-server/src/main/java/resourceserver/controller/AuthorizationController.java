package resourceserver.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import resourceserver.domain.User;
import resourceserver.service.UserAthorizationService;

@Controller
public class AuthorizationController {

  private final UserAthorizationService userAthorizationService;

  public AuthorizationController(UserAthorizationService userAthorizationService) {
    this.userAthorizationService = userAthorizationService;
  }

  @GetMapping("/oauth2/link")
  public String link() {
    userAthorizationService.askUserToGrantAccessRequest();

    return "/home";
  }
}
