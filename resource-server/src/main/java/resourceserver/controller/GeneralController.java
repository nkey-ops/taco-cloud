package resourceserver.controller;

import java.util.Objects;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import resourceserver.domain.User;
import resourceserver.service.UserAthorizationService;

@Controller
public class GeneralController {
  private final UserAthorizationService userAthorizationService;

  public GeneralController(UserAthorizationService userAthorizationService) {
    this.userAthorizationService = userAthorizationService;
  }

  @GetMapping("/oauth2/link")
  public String link() {
    userAthorizationService.askUserToGrantAccessRequest();

    return "/home";
  }

  @GetMapping({"/", "/home"})
  public ModelAndView home(@AuthenticationPrincipal User user) {
    Objects.requireNonNull(user);

    var modeAndView = new ModelAndView("home");

    modeAndView.addObject("username", user.getUsername());
    modeAndView.addObject("isAccountLinked", user.isAuthorizationServerLinked());


    return modeAndView;
  }
}
