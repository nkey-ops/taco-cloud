package resourceserver.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import resourceserver.data.IngredientRepository;
import resourceserver.domain.Ingredient;

@RestController
@RequestMapping(path = "/ingredients", produces = MediaType.APPLICATION_JSON_VALUE)
public class IngredientsRestController {

  private final IngredientRepository ingredientRepository;

  public IngredientsRestController(IngredientRepository ingredientRepository) {
    this.ingredientRepository = ingredientRepository;
  }

  @GetMapping
  @PreAuthorize("hasAuthority('read:allIngredients')")
  public List<Ingredient> getAllIngridients() {
    return ingredientRepository.findAll();
  }


  @PutMapping
  @PreAuthorize("hasAuthority('create:ingredient')")
  public Ingredient createIngredient(@RequestBody @Valid Ingredient ingredient) {
    return ingredientRepository.save(ingredient);    
  }


  @DeleteMapping("/{id}")
  @PreAuthorize("hasAuthority('delete:ingredient')") 
  public void delteIngredient(@PathVariable("id") String id) {
    ingredientRepository.deleteById(id);;
  }
}
