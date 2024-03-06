package tacos.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import tacos.domain.Ingredient;

@Service
public class IngridientsService {

  public List<Ingredient> get() {
    return new ArrayList<>();
  }

  public Ingredient save(Ingredient ingredient) {
    return null;
  }

  public void delete(String ingredientId) {}
}
