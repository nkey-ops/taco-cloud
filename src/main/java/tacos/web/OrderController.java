package tacos.web;

import org.springframework.beans.BeanUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import tacos.data.OrderRepository;
import tacos.domain.OrderProps;
import tacos.domain.TacoOrder;
import tacos.domain.User;

@Slf4j
@Controller
@RequestMapping("/orders")
@SessionAttributes("tacoOrder")
public class OrderController {

	private final OrderRepository orderRepo;
	private final OrderProps orderProps;

	public OrderController(OrderRepository orderRepository,
							OrderProps orderProps) {
		this.orderRepo = orderRepository;
		this.orderProps = orderProps;
	}

	@GetMapping("/current")
	public String orderForm() {
		return "orderForm";
	}
	
	@GetMapping
	public String ordersForUser(
			@AuthenticationPrincipal User user, Model model) {
		Pageable pageable = PageRequest.of(0, orderProps.getPageSize());
		model.addAttribute("order", 
				orderRepo.findByUserOrderByPlacedAtDesc(user, pageable));
		
		return "orderList";
	}

	@PostMapping
	public String processOrder(
			@Valid TacoOrder order, Errors errors,
			SessionStatus sessionStatus,
			@AuthenticationPrincipal User user) {
		
		if (errors.hasErrors()) 
			return "orderForm";
		
		
		order.setUser(user);
		orderRepo.save(order);


		log.info("Order submitted: {}", order);
		sessionStatus.setComplete();
		
		return "redirect:/";
	}

	@PatchMapping(path="/{orderId}", consumes="application/json")
	public TacoOrder patchOrder(
			@PathVariable("orderId") Long orderId,
			@RequestBody TacoOrder patch) {

		TacoOrder order = orderRepo.findById(orderId)
				.orElseThrow(() -> new RuntimeException("Order wasn't found"));

		BeanUtils.copyProperties(patch, order);

		
		return orderRepo.save(order);
	}

	@DeleteMapping("/{orderId}")
	public ResponseEntity<?> deleteOrder(@PathVariable("orderId") Long orderId) {
		if(!orderRepo.existsById(orderId))
			return ResponseEntity.notFound().build();

		orderRepo.deleteById(orderId);
		return ResponseEntity.noContent().build();
	}
}




















