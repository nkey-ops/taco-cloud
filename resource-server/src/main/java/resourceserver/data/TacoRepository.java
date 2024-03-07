package resourceserver.data;

import org.springframework.data.jpa.repository.JpaRepository;

import resourceserver.domain.Taco;

public interface TacoRepository extends JpaRepository<Taco, Long>{
}
