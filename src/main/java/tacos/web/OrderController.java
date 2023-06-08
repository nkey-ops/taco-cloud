package tacos.web;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
}




















