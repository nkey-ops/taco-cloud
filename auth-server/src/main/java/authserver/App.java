package authserver;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.server.servlet.OAuth2AuthorizationServerAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.server.servlet.OAuth2AuthorizationServerJwtAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@SpringBootApplication(
    exclude = {
      OAuth2AuthorizationServerJwtAutoConfiguration.class,
      OAuth2AuthorizationServerAutoConfiguration.class,
      OAuth2ResourceServerAutoConfiguration.class
    })
public class App {
  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }

  @Bean
  public ApplicationRunner dataLoader2(
      InMemoryUserDetailsManager uManager, PasswordEncoder encoder) {
    return args -> {
      uManager.createUser(new User("habuma", encoder.encode("password"), "ROLE_ADMIN"));
      uManager.createUser(new User("tacochef", encoder.encode("password"), "ROLE_ADMIN"));
    };
  }
}
