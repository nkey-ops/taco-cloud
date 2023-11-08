package tacos.domain;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.rest.core.annotation.RestResource;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@RestResource(rel = "tacos", path = "tacos")
@Entity(name = "tacos")
public class Taco implements Serializable {

	@Serial
	@Setter(value = AccessLevel.NONE)
	@Getter(value = AccessLevel.NONE)
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Setter(value = AccessLevel.NONE)
	private Long  id;
	private Date createdAt = new Date();

	@NotNull
	@Size(min = 5, message="Name must be at least 5 character long")
	private String name;

	@NotNull
	@Size(min = 1, message="You must choose at least 1 ingriedient")
	@ManyToMany
	private List<Ingredient> ingredients = new ArrayList<>();



	public Taco(
			@NotNull @Size(min = 5, message = "Name must be at least 5 character long") String name,
			@NotNull @Size(min = 1, message = "You must choose at least 1 ingriedient") List<Ingredient> ingredients) {
		this.name = name;
		this.ingredients = ingredients;
	}
}
	
