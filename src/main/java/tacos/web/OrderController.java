package tacos.web;

import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import tacos.data.OrderRepository;
import tacos.data.User;
import tacos.domain.TacoOrder;

@Slf4j
@Controller
@RequestMapping("/orders")
@SessionAttributes("tacoOrder")
public class OrderController {

	private final OrderRepository orderRepository;

	public OrderController(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	@GetMapping("/current")
	public String orderForm() {
		return "orderForm";
	}

	@PostMapping
	public String processOrder(
			@Valid TacoOrder order, Errors errors,
			SessionStatus sessionStatus,
			@AuthenticationPrincipal User user) {
		
		if (errors.hasErrors()) 
			return "orderForm";
		
		
		order.setUser(user);
		orderRepository.save(order);


		log.info("Order submitted: {}", order);
		sessionStatus.setComplete();
		
		return "redirect:/";
	}
}




















