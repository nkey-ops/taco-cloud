package tacos;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import tacos.data.IngredientRepository;
import tacos.data.TacoRepository;
import tacos.data.UserRepository;
import tacos.domain.Ingredient;
import tacos.domain.Ingredient.Type;
import tacos.domain.Taco;
import tacos.domain.User;

@SpringBootApplication
public class TacoCloudApplication {

	public static void main(String[] args) {
		SpringApplication.run(TacoCloudApplication.class, args);
	}

	@Bean
	InternalResourceViewResolver internalResourceViewResolver() {
		InternalResourceViewResolver internalResourceViewResolver = new InternalResourceViewResolver();
		internalResourceViewResolver.setSuffix(".html");
		return internalResourceViewResolver;
	}

	@Bean
	@Profile("!web-test")
	CommandLineRunner dataLoader(
					IngredientRepository inRepo,
					TacoRepository tacoRepo,
					UserRepository userRepo,
					PasswordEncoder encoder) {
		return args -> {
			Ingredient flourTortilla = new Ingredient(
			"FLTO", "Flour Tortilla", Type.WRAP);
			Ingredient cornTortilla = new Ingredient(
			"COTO", "Corn Tortilla", Type.WRAP);
			Ingredient groundBeef = new Ingredient(
			"GRBF", "Ground Beef", Type.PROTEIN);
			Ingredient carnitas = new Ingredient(
			"CARN", "Carnitas", Type.PROTEIN);
			Ingredient tomatoes = new Ingredient(
			"TMTO", "Diced Tomatoes", Type.VEGGIES);
			Ingredient lettuce = new Ingredient(
			"LETC", "Lettuce", Type.VEGGIES);
			Ingredient cheddar = new Ingredient(
			"CHED", "Cheddar", Type.CHEESE);
			Ingredient jack = new Ingredient(
			"JACK", "Monterrey Jack", Type.CHEESE);
			Ingredient salsa = new Ingredient(
			"SLSA", "Salsa", Type.SAUCE);
			Ingredient sourCream = new Ingredient(
			"SRCR", "Sour Cream", Type.SAUCE);

			inRepo.save(flourTortilla);
			inRepo.save(cornTortilla);
			inRepo.save(groundBeef);
			inRepo.save(carnitas);
			inRepo.save(tomatoes);
			inRepo.save(lettuce);
			inRepo.save(cheddar);
			inRepo.save(jack);
			inRepo.save(salsa);
			inRepo.save(sourCream);

			tacoRepo.save(
					new Taco("Carnivore",
							Arrays.asList(
									flourTortilla, groundBeef, carnitas,
									sourCream, salsa, cheddar))
					);
			tacoRepo.save(new Taco("Bovine Bounty",
					Arrays.asList(
							cornTortilla, groundBeef, cheddar,
						jack, sourCream))
					);
			tacoRepo.save(new Taco("Veg-Out",
								Arrays.asList(
										flourTortilla, cornTortilla, tomatoes,
										lettuce, salsa))
					);
		
			userRepo.save(
				new User("habuma", encoder.encode("password"), "ROLE_ADMIN"));
			userRepo.save(
				new User("tacochef", encoder.encode("password"), "ROLE_ADMIN"));
		};

	}
	

	@Bean
	UserDetailsService userDetailsService(UserRepository usersRepo) {
		return username -> 
			usersRepo.findByUsername(username)
					 .orElseThrow(() -> 
					 	new UsernameNotFoundException("User '" + username + "' not found"));
	}

}
