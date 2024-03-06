package tacos.web;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import tacos.domain.Taco;
import tacos.service.TacoService;

@RestController
@RequestMapping(path = "/api/tacos", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin("http://localhost:8080")
public class TacoController {
  private final TacoService tacoService;

  public TacoController(TacoService tacoService) {
    this.tacoService = tacoService;
  }

  @GetMapping(params = "recent")
  public List<Taco> recentTacos() {
    PageRequest page = PageRequest.of(0, 12, Sort.by("createdAt").descending());
    PageRequest.of(1, 2);

    return tacoService.get(page);
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> tacoById(@PathVariable("id") Long id) {
    Optional<Taco> optTaco = tacoService.get(id);

    return optTaco.isPresent()
        ? ResponseEntity.ok(optTaco.get())
        : ResponseEntity.notFound().build();
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public Taco postTaco(@RequestBody Taco taco) {

    return tacoService.save(taco);
  }

  @DeleteMapping("/{tacoId}/ingredients/{ingredientId}")
  public ResponseEntity<?> removeIngredient(
      @PathVariable("tacoId") long tacoId, @PathVariable("ingredientId") String ingredientId) {

    Optional<Taco> tacoOpt = tacoService.get(tacoId);
    if (tacoOpt.isEmpty()) return ResponseEntity.status(NOT_FOUND).body("Taco wasn't found");

    Taco taco = tacoOpt.get();
    int size = taco.getIngredients().size();

    if (size == 1)
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Last ingredient remains");
    taco.getIngredients().removeIf(in -> in.getId().equals(ingredientId));

    if (size == taco.getIngredients().size())
      return ResponseEntity.status(NOT_FOUND).body("Ingredient wasn't found");

    tacoService.save(taco);
    return ResponseEntity.noContent().build();
  }
}
