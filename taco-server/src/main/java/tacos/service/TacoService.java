package tacos.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import tacos.domain.Taco;

/**
 * TacoService
 */
@Service
public class TacoService {

    public boolean doesExist(Long tacoId) {
        return false;
    }

    public List<Taco> get(PageRequest page) {
        return null;
    }

    public Optional<Taco> get(Long id) {
        return null;
    }

    public Taco save(Taco taco) {
        return null;
    }

  
}
