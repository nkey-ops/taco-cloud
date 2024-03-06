package tacos.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import tacos.domain.TacoOrder;
import tacos.domain.User;

/** OrderService */
@Service
public class OrderService {

  public Optional<TacoOrder> get(long id) {
    return Optional.empty();
  }

  public List<TacoOrder> get(User user) {
    return new ArrayList<>();
  }

  public TacoOrder save(TacoOrder tacoOrder) {
    return new TacoOrder();
  }

  public boolean doesExist(Long orderId) {
    return false;
  }

  public void delete(Long orderId) {}
}
