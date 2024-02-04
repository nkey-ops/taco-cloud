package tacos.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

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
        .authorizeHttpRequests()
        .requestMatchers("/login", "/error", "/register", "/", "/images/**")
        .anonymous()
        .requestMatchers("/**")
        .hasAnyRole("USER", "ADMIN")
        .and()
        .formLogin(
            login -> login.loginPage("/login").defaultSuccessUrl("/design").failureUrl("/error"))
        .build();
  }
}
