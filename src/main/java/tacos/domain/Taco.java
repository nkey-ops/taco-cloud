package tacos.domain;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "tacos")
public class Taco implements Serializable {

	@Serial
	@Setter(value = AccessLevel.NONE)
	@Getter(value = AccessLevel.NONE)
	private static final long serialVersionUID = 1L;
	
	@Id
	private Long id;
	private Date createdAt = new Date();

	@NotNull
	@Size(min = 5, message="Name must be at least 5 character long")
	private String name;

	@NotNull
	@Size(min = 1, message="You must choose at least 1 ingriedient")
	@ManyToMany
	private List<Ingredient> ingredients;
}
	