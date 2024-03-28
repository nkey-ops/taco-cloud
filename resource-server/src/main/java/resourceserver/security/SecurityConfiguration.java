package resourceserver.security;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtIssuerValidator;
import org.springframework.security.oauth2.jwt.JwtTimestampValidator;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import jakarta.servlet.annotation.WebFilter;
import resourceserver.data.UserRepository;
import resourceserver.domain.User;
import resourceserver.security.Authorities.Ingredients;
import resourceserver.sevice.AuthorizationServerService;

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
        .oauth2ResourceServer(r -> r.jwt(Customizer.withDefaults()))
        .build();
  }

  @Bean
  public ApplicationRunner onApplicationStartup(
      UserRepository userRepository,
      PasswordEncoder passwordEncoder,
      AuthorizationServerService authService) {

    return args -> {
      User admin =
          new User("res-admin", passwordEncoder.encode("password"), "ADMIN", Ingredients.ALL);
      User user = new User("user", passwordEncoder.encode("password"), "USER");
      userRepository.save(admin);
      userRepository.save(user);

      authService.createRegistereClient(user);
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
    class RFilter extends CommonsRequestLoggingFilter {}

    var filter = new RFilter();
    filter.setIncludeQueryString(true);
    filter.setIncludePayload(true);
    filter.setMaxPayloadLength(10000);
    filter.setIncludeHeaders(true);
    filter.setBeforeMessagePrefix("BEFORE:" + System.lineSeparator());
    filter.setAfterMessagePrefix("AFTER: " + System.lineSeparator());
    filter.setIncludeClientInfo(true);
    return filter;
  }

  @Bean
  public JwtAuthenticationConverter jAuthenticationConverter() {
    JwtGrantedAuthoritiesConverter jwtGrandConverter = new JwtGrantedAuthoritiesConverter();

    jwtGrandConverter.setAuthorityPrefix("");

    var jwtAuthenConverter = new JwtAuthenticationConverter();
    jwtAuthenConverter.setJwtGrantedAuthoritiesConverter(jwtGrandConverter);
    return jwtAuthenConverter;
  }

}
