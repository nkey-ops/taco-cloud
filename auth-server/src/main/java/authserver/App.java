package authserver;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;

@SpringBootApplication
public class App {
  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }

  @Bean
  public ApplicationRunner dataLoader(UserRepository userRepository, PasswordEncoder encoder) {
    return args -> {
      userRepository.save(new User("habuma",   encoder.encode("password"), "ROLE_ADMIN"));
      userRepository.save(new User("tacochef", encoder.encode("password"), "ROLE_ADMIN"));
    }; 
  }
}
