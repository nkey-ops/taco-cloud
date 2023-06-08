package tacos.web;

import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import tacos.data.TacoRepository;
import tacos.domain.Taco;
import tacos.domain.TacoOrder;

@RestController
@RequestMapping(path="/api/tacos",
				produces="application/json")
@CrossOrigin(origins="http://tacocloud:8080")
public class TacoController {
	private TacoRepository tacoRepo;

	public TacoController(TacoRepository tacoRepo) {
		this.tacoRepo = tacoRepo;
	}

	@GetMapping(params="recent")
	public Iterable<Taco> recentTacos() {
		PageRequest page = PageRequest.of(
				0, 12, Sort.by("createdAt").descending());
		return tacoRepo.findAll(page);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> tacoById(@PathVariable("id") Long id) {
		Optional<Taco> optTaco = tacoRepo.findById(id);

		return optTaco.isPresent() 
					?   ResponseEntity.ok(optTaco.get()): 
						ResponseEntity.notFound().build();
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public Taco postTaco(@RequestBody Taco taco) {
		return tacoRepo.save(taco);
	}

	@PatchMapping(path="/{orderId}", consumes="application/json")
	public TacoOrder patchOrder(
							@PathVariable("orderId") Long orderId,
							@RequestBody TacoOrder patch) {
		TacoOrder order = tacoRepo.findById(orderId)
			.get();

	}
}






















