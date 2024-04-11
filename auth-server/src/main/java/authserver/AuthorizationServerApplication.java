package authserver;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@SpringBootApplication
public class AuthorizationServerApplication {
  public static void main(String[] args) {
    SpringApplication.run(AuthorizationServerApplication.class, args);
  }

  @Bean
  public ApplicationRunner dataLoader2(
      InMemoryUserDetailsManager uManager, PasswordEncoder encoder) {
    return args -> {
      uManager.createUser(new User("habuma", encoder.encode("password"), "ROLE_ADMIN"));
      uManager.createUser(new User("tacochef", encoder.encode("password"), "ROLE_ADMIN"));
    };
  }


  @Bean
  InternalResourceViewResolver internalResourceViewResolver(){
    var internalResourceViewResolver = new InternalResourceViewResolver();
    internalResourceViewResolver.setSuffix(".html");
    return internalResourceViewResolver;
  }

}

