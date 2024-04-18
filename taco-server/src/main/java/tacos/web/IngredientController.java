package tacos.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import tacos.domain.Ingredient;
import tacos.domain.User;
import tacos.service.IngridientsService;

@RestController
@RequestMapping(
    path = "/api/ingredients",
    produces = MediaType.APPLICATION_JSON_VALUE)
// @CrossOrigin(origins = "http://localhost:8080")
public class IngredientController {
  private IngridientsService ingridientsService;

  @Autowired
  public IngredientController(IngridientsService ingridientsService) {
    this.ingridientsService = ingridientsService;
  }

  @GetMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public Iterable<Ingredient> allIngredients(@AuthenticationPrincipal UserDetails user) {
    return ingridientsService.get((User) user);
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public Ingredient saveIngredient(@RequestBody Ingredient ingredient) {
    return ingridientsService.save(ingredient);
  }

  @DeleteMapping(path = "/{id}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteIngredient(@PathVariable("id") String ingredientId) {
    ingridientsService.delete(ingredientId);
  }
}
