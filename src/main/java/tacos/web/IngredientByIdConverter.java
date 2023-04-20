package tacos.web;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import tacos.Ingredient;
import tacos.data.JdbcIngredientRepository;

@Component
public class IngredientByIdConverter implements Converter<String, Ingredient> {

	private final JdbcIngredientRepository ingredientRepository;
	
	public IngredientByIdConverter(JdbcIngredientRepository ingredientRepository) {
		this.ingredientRepository = ingredientRepository;
	}

	@Override
	public Ingredient convert(String id) {
		return ingredientRepository.findById(id)
				.orElseThrow(() -> 
				new RuntimeException("Row with id was not found"));
	}
}
