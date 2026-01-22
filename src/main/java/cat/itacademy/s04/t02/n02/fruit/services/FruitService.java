package cat.itacademy.s04.t02.n02.fruit.services;

import cat.itacademy.s04.t02.n02.fruit.dto.FruitRequestDTO;
import cat.itacademy.s04.t02.n02.fruit.exception.FruitNotFoundException;
import cat.itacademy.s04.t02.n02.fruit.model.Fruit;
import cat.itacademy.s04.t02.n02.fruit.repository.FruitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FruitService {

    private final FruitRepository repository;

    public Fruit createFruit(FruitRequestDTO dto) {
        Fruit fruit = new Fruit(dto.name(), dto.weightInKilos());
        return repository.save(fruit);
    }

    public Fruit getFruitById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new FruitNotFoundException(id));
    }

    public List<Fruit> getAllFruits() {
        return repository.findAll();
    }

    public Fruit updateFruit(Long id, FruitRequestDTO dto) {
        Fruit fruit = repository.findById(id)
                .orElseThrow(() -> new FruitNotFoundException(id));

        fruit.setName(dto.name());
        fruit.setWeightInKilos(dto.weightInKilos());

        return repository.save(fruit);
    }

    public void deleteFruit(Long id) {
        Fruit fruit = repository.findById(id)
                .orElseThrow(() -> new FruitNotFoundException(id));
        repository.delete(fruit);
    }
}