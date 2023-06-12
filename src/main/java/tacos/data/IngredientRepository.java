package tacos.data;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

import tacos.domain.Ingredient;

public interface IngredientRepository extends CrudRepository<Ingredient, String>{

	List<Ingredient> findAll();
	Optional<Ingredient> findById(String id);
}
