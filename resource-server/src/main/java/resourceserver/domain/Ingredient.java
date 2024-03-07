package resourceserver.domain;

import java.io.Serial;
import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "ingredients")
public class Ingredient implements Serializable {

  @Serial
  private final static long serialVersionUID = 1; 

  @Id
  @Column(nullable = false)
  private final String id;

  @Column(nullable = false)
  private final String name;

  @Column(nullable = false)
  private final Type type;

  public enum Type {
    WRAP,
    PROTEIN,
    VEGGIES,
    CHEESE,
    SAUCE
  }

  Ingredient() {
    this.id = null;
    this.name = null;
    this.type = null;
  }

  public Ingredient(@NotNull String id, @NotNull String name, @NotNull Type type) {
    this.id = id;
    this.name = name;
    this.type = type;
  }

}
