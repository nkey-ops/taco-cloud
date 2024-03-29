package resourceserver.data;


import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import resourceserver.domain.TacoOrder;
import resourceserver.domain.User;

public interface OrderRepository extends CrudRepository<TacoOrder, Long> {

    List<TacoOrder> findByUserOrderByPlacedAtDesc(User user, Pageable pageable);

    boolean existsById(Long orderId);
}
