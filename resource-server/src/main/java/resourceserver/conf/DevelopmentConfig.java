package resourceserver.conf;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import resourceserver.data.IngredientRepository;
import resourceserver.data.OrderRepository;
import resourceserver.data.TacoRepository;
import resourceserver.data.UserRepository;
import resourceserver.domain.Ingredient;
import resourceserver.domain.Ingredient.Type;
import resourceserver.domain.Taco;
import resourceserver.domain.TacoOrder;
import resourceserver.domain.User;

@Configuration
public class DevelopmentConfig {

  @Bean
  CommandLineRunner dataLoader(
      IngredientRepository inRepo,
      TacoRepository tacoRepo,
      UserRepository userRepo,
      OrderRepository orderRepo,
      PasswordEncoder encoder) {

    return args -> {
      Ingredient flourTortilla = new Ingredient("FLTO", "Flour Tortilla", Type.WRAP);
      Ingredient cornTortilla = new Ingredient("COTO", "Corn Tortilla", Type.WRAP);
      Ingredient groundBeef = new Ingredient("GRBF", "Ground Beef", Type.PROTEIN);
      Ingredient carnitas = new Ingredient("CARN", "Carnitas", Type.PROTEIN);
      Ingredient tomatoes = new Ingredient("TMTO", "Diced Tomatoes", Type.VEGGIES);
      Ingredient lettuce = new Ingredient("LETC", "Lettuce", Type.VEGGIES);
      Ingredient cheddar = new Ingredient("CHED", "Cheddar", Type.CHEESE);
      Ingredient jack = new Ingredient("JACK", "Monterrey Jack", Type.CHEESE);
      Ingredient salsa = new Ingredient("SLSA", "Salsa", Type.SAUCE);
      Ingredient sourCream = new Ingredient("SRCR", "Sour Cream", Type.SAUCE);

      flourTortilla = inRepo.save(flourTortilla);
      cornTortilla = inRepo.save(cornTortilla);
      groundBeef = inRepo.save(groundBeef);
      carnitas = inRepo.save(carnitas);
      tomatoes = inRepo.save(tomatoes);
      lettuce = inRepo.save(lettuce);
      cheddar = inRepo.save(cheddar);
      jack = inRepo.save(jack);
      salsa = inRepo.save(salsa);
      sourCream = inRepo.save(sourCream);

      Taco taco =
          tacoRepo.save(
              new Taco(
                  "Carnivore",
                  Arrays.asList(flourTortilla, groundBeef, carnitas, sourCream, salsa, cheddar)));

      tacoRepo.save(
          new Taco(
              "Bovine Bounty", Arrays.asList(cornTortilla, groundBeef, cheddar, jack, sourCream)));

      tacoRepo.save(
          new Taco(
              "Veg-Out", Arrays.asList(flourTortilla, cornTortilla, tomatoes, lettuce, salsa)));

      User hab = new User("habuma", encoder.encode("password"), "ROLE_ADMIN");
      userRepo.save(hab);
      userRepo.save(new User("tacochef", encoder.encode("password"), "ROLE_ADMIN"));

      TacoOrder order =
          new TacoOrder(
              "Colorado",
              "Green Line",
              "Kansas",
              "Colorado",
              "20202",
              "12312312311231",
              "10/25",
              "123",
              userRepo.findById(hab.getId()).get());

      orderRepo.save(order);
      order.addTaco(taco);
      orderRepo.save(order);
    };
  }

  @Bean
  UserDetailsService userDetailsService(UserRepository usersRepo) {
    return username ->
        usersRepo
            .findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User '" + username + "' not found"));
  }
}
