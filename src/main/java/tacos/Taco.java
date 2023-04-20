package tacos;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class Taco implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;
	private Long id;
	private Date createdAt = new Date();

	@NotNull
	@Size(min = 5, message="Name must be at least 5 character long")
	private String name;

	@NotNull
	@Size(min = 1, message="You must choose at least 1 ingriedient")
	private List<Ingredient> ingredients;
}
	