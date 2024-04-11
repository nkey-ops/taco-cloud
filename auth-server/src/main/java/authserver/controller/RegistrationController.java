package authserver.controller;

import java.util.Objects;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import authserver.User;
import jakarta.websocket.server.PathParam;

@Controller
public class RegistrationController {
  private PasswordEncoder passwordEncoder;
  private InMemoryUserDetailsManager userService;

  public RegistrationController(
      InMemoryUserDetailsManager userDetailsManager, PasswordEncoder passwordEncoder) {
    this.userService = userDetailsManager;
    this.passwordEncoder = passwordEncoder;
  }

  @GetMapping("/register")
  public String registrationForm() {
    return "registration";
  }

  @PostMapping("/register")
  public String processRegistration(
      @PathParam("username") String username, @PathParam("password") String password) {
    Objects.requireNonNull(password);

    if (username.isBlank() || password.isBlank()) {
      throw new IllegalArgumentException("Username or password cannot be blank");
    }

    if (username.length() < 3
        || username.length() > 10
        || password.length() < 3
        || password.length() > 10) {
      throw new IllegalArgumentException(
          "Username's or password's lengths cannot be shorter than 4 or longer than 10 characters");
    }

    if (!username.matches("[\\w0-9]{4,10}")) {
      throw new IllegalArgumentException("Doesn't match pattern [\\w0-9]{4,10}");
    }

    userService.createUser(new User(username, passwordEncoder.encode(password), "USER"));
    return "redirect:/login";
  }

  @GetMapping("/login")
  public String login() {
    return "login";
  }
  
  @GetMapping("/")
  public ModelAndView home(@AuthenticationPrincipal UserDetails user) {
    ModelAndView modelAndView = new ModelAndView("home");
    modelAndView.addObject("username", user.getUsername());

    return modelAndView;
  }
}
