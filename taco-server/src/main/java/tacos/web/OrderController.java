package tacos.web;

import jakarta.validation.Valid;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import tacos.domain.TacoOrder;
import tacos.domain.User;
import tacos.service.OrderService;
import tacos.service.TacoService;

@Slf4j
@Controller
@RequestMapping("/orders")
@SessionAttributes("tacoOrder")
public class OrderController {

  private final OrderProps orderProps;
  private final OrderService orderService;
  private final TacoService tacoService;

  public OrderController(
      OrderService orderService, TacoService tacoService, OrderProps orderProps) {
    this.orderService = orderService;
    this.orderProps = orderProps;
    this.tacoService = tacoService;
  }

  @GetMapping("/current")
  public String orderForm() {
    return "orderForm";
  }

  @GetMapping("/{id}")
  public TacoOrder getOrder(@PathVariable long id) {
    Optional<TacoOrder> order = orderService.get(id);
    if (order.isEmpty()) throw new RuntimeException("Order with id: " + id + " wasn't found");

    return order.get();
  }

  @GetMapping
  public String ordersForUser(@AuthenticationPrincipal User user, Model model) {
    Pageable pageable = PageRequest.of(0, orderProps.getPageSize());
    model.addAttribute("orders", orderService.get(user));

    log.info("Orders requested: {}", pageable);
    return "orderList";
  }

  @PostMapping
  public String processOrder(
      @Valid TacoOrder order,
      Errors errors,
      SessionStatus sessionStatus,
      @AuthenticationPrincipal User user) {

    if (errors.hasErrors()) return "orderForm";

    order.setUser(user);
    orderService.save(order);

    log.info("Order submitted: {}", order);
    sessionStatus.setComplete();

    return "redirect:/orders";
  }

  @PatchMapping(path = "/{orderId}", consumes = "application/json")
  public TacoOrder patchOrder(@PathVariable("orderId") Long orderId, @RequestBody TacoOrder patch) {

    TacoOrder order =
        orderService.get(orderId).orElseThrow(() -> new RuntimeException("Order wasn't found"));

    BeanUtils.copyProperties(patch, order);
    return orderService.save(order);
  }

  @PatchMapping(path = "/{orderId}/{tacoId}", consumes = "application/json")
  public ResponseEntity<?> removeTaco(
      @PathVariable("orderId") Long orderId, @PathVariable("tacoId") Long tacoId) {

    if (!orderService.doesExist(orderId) || !tacoService.doesExist(tacoId))
      return ResponseEntity.notFound().build();

    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{orderId}")
  public ResponseEntity<?> deleteOrder(@PathVariable("orderId") Long orderId) {
    if (!orderService.doesExist(orderId)) return ResponseEntity.notFound().build();

    orderService.delete(orderId);
    return ResponseEntity.noContent().build();
  }
}
