package tacos.web;

import java.util.Optional;

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

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import tacos.data.OrderRepository;
import tacos.data.TacoRepository;
import tacos.domain.TacoOrder;
import tacos.domain.User;

@Slf4j
@Controller
@RequestMapping("/orders")
@SessionAttributes("tacoOrder")
public class OrderController {

	private final OrderRepository orderRepo;
    private final TacoRepository tacoRepo;
    private final OrderProps orderProps;


    public OrderController(OrderRepository orderRepo, 
            TacoRepository tacoRepo, OrderProps orderProps) {
        this.orderRepo = orderRepo;
        this.tacoRepo = tacoRepo;
        this.orderProps = orderProps;
    }

	@GetMapping("/current")
	public String orderForm() {
		return "orderForm";
	}
	
	@GetMapping("/{id}")
	public TacoOrder getOrder(@PathVariable long id) {
        Optional<TacoOrder> order = orderRepo.findById(id);
		if (order.isEmpty()) 
            throw new RuntimeException("Order with id: " + id + " wasn't found"); 

        return order.get();
	}

	@GetMapping
	public String ordersForUser(
			@AuthenticationPrincipal User user, Model model) {
		Pageable pageable = PageRequest.of(0, orderProps.getPageSize());
        model.addAttribute("orders", 
                orderRepo.findByUserOrderByPlacedAtDesc(user, pageable));
		
		log.info("Orders requested: {}", pageable);
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
		
		return "redirect:/orders";
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

	@PatchMapping(path="/{orderId}/{tacoId}", consumes="application/json")
	public ResponseEntity<?> removeTaco(
			@PathVariable("orderId") Long orderId,
			@PathVariable("tacoId") Long tacoId) {

        if(!orderRepo.existsById(orderId) 
                || !tacoRepo.existsById(tacoId))
                return ResponseEntity.notFound().build();

		return ResponseEntity.noContent().build() ;
	}

	@DeleteMapping("/{orderId}")
	public ResponseEntity<?> deleteOrder(@PathVariable("orderId") Long orderId) {
		if(!orderRepo.existsById(orderId))
			return ResponseEntity.notFound().build();

		orderRepo.deleteById(orderId);
		return ResponseEntity.noContent().build();
	}
}

















