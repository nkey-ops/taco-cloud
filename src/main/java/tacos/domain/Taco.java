package tacos.domain;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import jakarta.annotation.Generated;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants.Exclude;

@Getter @Setter
@NoArgsConstructor
@Entity(name = "tacos")
public class Taco implements Serializable {

	@Serial
	@Setter(value = AccessLevel.NONE)
	@Getter(value = AccessLevel.NONE)
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long  id;
	private Date createdAt = new Date();

	@NotNull
	@Size(min = 5, message="Name must be at least 5 character long")
	private String name;

	@NotNull
	@Size(min = 1, message="You must choose at least 1 ingriedient")
	@ManyToMany
	private List<Ingredient> ingredients;

	public Taco(
			@NotNull @Size(min = 5, message = "Name must be at least 5 character long") String name,
			@NotNull @Size(min = 1, message = "You must choose at least 1 ingriedient") List<Ingredient> ingredients) {
		this.name = name;
		this.ingredients = ingredients;
	}
}
	