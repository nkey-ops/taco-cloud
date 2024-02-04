package authserver.authorization;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import authserver.UserRepository;

@EnableWebSecurity

@Configuration

public class SecurityConfig {
  @Bean

  SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
    return http.authorizeHttpRequests(r -> r.anyRequest().authenticated())
        .formLogin(Customizer.withDefaults())
        .csrf(c -> c.disable())
        .build();
  }

  @Bean
  public UserDetailsService userDetailsManager(UserRepository userRepository) {
   return username -> { 
     var user = userRepository.findByName(username); 
     
     userRepository.findAll().forEach(System.out::println);

     System.out.println("USer is" + user);
     if(user.isEmpty())
       throw new UsernameNotFoundException("User wasn't found: " + username);

     System.out.println(user.get().getName());
     return user.get();
   };
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
