package resourceserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@SpringBootApplication
public class ResourceServerApplication {
  public static void main(String[] args) {
    SpringApplication.run(ResourceServerApplication.class, args);
  }

  @Bean
  InternalResourceViewResolver internalResourceViewResolver() {
    var internalResourceViewResolver = new InternalResourceViewResolver();
    internalResourceViewResolver.setSuffix(".html");
    return internalResourceViewResolver;
  }
}
