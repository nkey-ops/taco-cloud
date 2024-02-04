package authserver;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

/**
 * UserRepository
 */
public interface UserRepository extends CrudRepository<User, Long> {
  Optional<User> findByName(String name);
}
