package resourceserver.security;

import jakarta.servlet.annotation.WebFilter;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.actuate.web.exchanges.HttpExchangeRepository;
import org.springframework.boot.actuate.web.exchanges.InMemoryHttpExchangeRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import resourceserver.data.UserRepository;
import resourceserver.domain.User;
import resourceserver.security.Authorities.Ingredients;
import resourceserver.service.UserAthorizationService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

   @Bean
   @Order(1)
   SecurityFilterChain oAuth2SecurityFilterChain(HttpSecurity http) throws Exception {
     return http.oauth2Client(Customizer.withDefaults()).build();
   }

  @Bean
  @Order(2)
  SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
    return http.authorizeHttpRequests(
            a ->
                a.requestMatchers("/actuator/**", "/error")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .csrf(c -> c.disable())
        .formLogin(Customizer.withDefaults())
        // .oauth2ResourceServer(r -> r.jwt(Customizer.withDefaults()))
        .build();
  }

  @Bean
  public ApplicationRunner onApplicationStartup(
      UserRepository userRepository,
      PasswordEncoder passwordEncoder,
      UserAthorizationService authorizationService) {

    return args -> {
      User admin =
          new User("res-admin", passwordEncoder.encode("password"), "ADMIN", Ingredients.ALL);
      User user = new User("user", passwordEncoder.encode("password"), "USER");
      userRepository.save(admin);
      userRepository.save(user);
    };
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public HttpExchangeRepository httpExchangesProperties() {
    return new InMemoryHttpExchangeRepository();
  }

  @Bean
  public CommonsRequestLoggingFilter logFilter() {

    @Order(value = Ordered.HIGHEST_PRECEDENCE)
    @WebFilter(filterName = "RequestCachingFilter", urlPatterns = "/*")
    class CustomFilter extends CommonsRequestLoggingFilter {}

    var filter = new CustomFilter();
    filter.setIncludeQueryString(true);
    filter.setIncludePayload(true);
    filter.setMaxPayloadLength(10000);
    filter.setIncludeHeaders(true);
    filter.setBeforeMessagePrefix("BEFORE:" + System.lineSeparator());
    filter.setAfterMessagePrefix("AFTER: " + System.lineSeparator());
    filter.setIncludeClientInfo(true);
    return filter;
  }
}
