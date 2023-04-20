package tacos.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import tacos.Ingredient;

@Repository
public class JdbcIngredientRepository implements IngredientRepository{

	private JdbcTemplate jdbcTemplate;

	public JdbcIngredientRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<Ingredient> findAll() {
		return jdbcTemplate.query(
				"SELECT id, name, type " + 
				"FROM Ingredient", 
				this::mapRowToIngredient);
	}

	@Override
	public Optional<Ingredient> findById(String id) {
		Ingredient ingredient = jdbcTemplate.queryForObject(
				"SELECT id, name, type " + 
				"FROM ingredient " + 
				"WHERE id = ?", 
				this::mapRowToIngredient, id);
		
		return Optional.ofNullable(ingredient);
	}

	@Override
	public Ingredient save(Ingredient ingredient) {
		jdbcTemplate.update(
				"INSERT INTO Ingredient (id, name, type) values (?, ?, ?)",
				ingredient.getId(),
				ingredient.getName(),
				ingredient.getType().toString());
	
		return ingredient; 
	}

	private Ingredient mapRowToIngredient(ResultSet row, int rowNum) throws SQLException {
		return new Ingredient(
				row.getString("id"),
				row.getString("name"),
				Ingredient.Type.valueOf(row.getString("type")));
	}
	
}
