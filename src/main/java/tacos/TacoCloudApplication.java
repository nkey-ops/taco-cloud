package tacos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@SpringBootApplication
public class TacoCloudApplication {

	public static void main(String[] args) {
		SpringApplication.run(TacoCloudApplication.class, args);
	}
	
	@Bean
	public InternalResourceViewResolver internalResourceViewResolver() {
	    InternalResourceViewResolver internalResourceViewResolver = new InternalResourceViewResolver();
//	    internalResourceViewResolver.setPrefix("pages/");
	    internalResourceViewResolver.setSuffix(".html");
	    return internalResourceViewResolver;
	}
}
