package tacos.data;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import tacos.domain.TacoOrder;

public interface OrderRepository extends CrudRepository<TacoOrder, Long>{

	List<TacoOrder> findByUserOrderByPlacedAtDesc(User user, Pageable pageable);

}
