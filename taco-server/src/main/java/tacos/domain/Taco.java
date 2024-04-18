package tacos.domain;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Taco {

  private Date createdAt = new Date();
  private String name;

  private List<Ingredient> ingredients = new ArrayList<>();

  public Taco(
      @NotNull @Size(min = 5, message = "Name must be at least 5 character long") String name,
      @NotNull @Size(min = 1, message = "You must choose at least 1 ingriedient")
          List<Ingredient> ingredients) {
    this.name = name;
    this.ingredients = ingredients;
  }

  public Taco() {
  }
}
