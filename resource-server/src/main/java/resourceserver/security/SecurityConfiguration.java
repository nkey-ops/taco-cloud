package resourceserver.security;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.actuate.web.exchanges.HttpExchangeRepository;
import org.springframework.boot.actuate.web.exchanges.InMemoryHttpExchangeRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

  @Bean
  SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
    return http.authorizeHttpRequests(
            a -> a.requestMatchers("/actuator/**").permitAll().anyRequest().authenticated())
        .formLogin(Customizer.withDefaults())
        .csrf(c -> c.disable())
        .build();
  }

  @Bean
  public ApplicationRunner onApplicationStartup(
      UserRepository userRepository, PasswordEncoder passwordEncoder) {

    return args -> {
      userRepository.save(
          new User(
              "res-admin",
              passwordEncoder.encode("password"),
              "ADMIN",
              Ingredients.ALL.authorities()));

      userRepository.save(
          new User(
              "user", passwordEncoder.encode("password"), "USER", Ingredients.READ.authorities()));
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
    CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
    filter.setIncludeQueryString(true);
    filter.setIncludePayload(true);
    filter.setMaxPayloadLength(10000);
    filter.setIncludeHeaders(false);
    filter.setAfterMessagePrefix("REQUEST DATA: ");
    filter.setIncludeClientInfo(true);
    fi
    return filter;
  }
}
