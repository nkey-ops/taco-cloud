package tacos.security;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import tacos.domain.User;
import tacos.repo.UserRepository;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

  @Bean
  BCryptPasswordEncoder bCryptPasswrdEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http.csrf(c -> c.disable())
        .authorizeHttpRequests(
            a ->
                a.requestMatchers("/login", "/error", "/register", "/", "/images/**")
                    .permitAll()
                    // .requestMatchers(HttpMethod.GET, "/api/ingredients")
                    // .hasAuthority("SCOPE_readIngredients")
                    // .requestMatchers(HttpMethod.POST, "/api/ingredients")
                    // .hasAuthority("SCOPE_writeIngredients")
                    .requestMatchers(HttpMethod.DELETE, "/api/ingredients/**")
                    .hasAuthority("SCOPE_deleteIngredients")
                    // .requestMatchers("/actuator/**")
                    // .permitAll()
                    .requestMatchers("/**")
                    .hasAnyRole("USER", "ADMIN"))
        .formLogin(Customizer.withDefaults())
        .formLogin(login -> login.loginPage("/login").defaultSuccessUrl("/design"))
        .oauth2Login(l -> l.loginPage("/oauth2/authorization/taco-admin-client"))
        .oauth2Client(Customizer.withDefaults())
        .build();
  }

  @Bean
  UserDetailsService userDetailsManager(UserRepository userRepository) {
    return (username) -> userRepository.findByUsername(username).orElseThrow();
  }

  @Bean
  ApplicationRunner loadUsers(UserRepository manager, PasswordEncoder passwordEncoder) {
    return args -> {
      manager.save(
          new User(
              "habuma",
              passwordEncoder.encode("password"),
              "rest-admin",
              "JSESSIONID=051B566F211C83F9CAE6ECA5AF35E511;",
              "ROLE_ADMIN"));
    };
  }
}
