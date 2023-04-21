package tacos;

import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@Table("ingredients")
public class Ingredient implements Persistable<String>{

	private final String id;
	private final String name;
	private final Type type;

	public enum Type {
		WRAP, PROTEIN, VEGGIES, CHEESE, SAUCE
	}

	@Override
	public boolean isNew() {
		// TODO Auto-generated method stub
		return false;
	}
}
