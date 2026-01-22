package cat.itacademy.s04.t02.n02.fruit.controllers;

import cat.itacademy.s04.t02.n02.fruit.dto.FruitRequestDTO;
import cat.itacademy.s04.t02.n02.fruit.dto.FruitResponseDTO;
import cat.itacademy.s04.t02.n02.fruit.model.Fruit;
import cat.itacademy.s04.t02.n02.fruit.services.FruitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fruits")
@RequiredArgsConstructor
public class FruitController {

    private final FruitService service;

    @PostMapping
    public ResponseEntity<FruitResponseDTO> createFruit(@Valid @RequestBody FruitRequestDTO request) {
        Fruit saved = service.createFruit(request);
        FruitResponseDTO response = new FruitResponseDTO(saved.getId(), saved.getName(), saved.getWeightInKilos());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FruitResponseDTO> getFruitById(@PathVariable Long id) {
        Fruit fruit = service.getFruitById(id);
        FruitResponseDTO response = new FruitResponseDTO(fruit.getId(), fruit.getName(), fruit.getWeightInKilos());
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<FruitResponseDTO>> getAllFruits() {
        List<Fruit> fruits = service.getAllFruits();

        List<FruitResponseDTO> response = fruits.stream().map(fruit -> new FruitResponseDTO(fruit.getId(), fruit.getName(), fruit.getWeightInKilos())).toList();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FruitResponseDTO> updateFruit(@PathVariable Long id, @Valid @RequestBody FruitRequestDTO request) {
        Fruit updated = service.updateFruit(id,request);
        FruitResponseDTO response = new FruitResponseDTO(updated.getId(),updated.getName(), updated.getWeightInKilos());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFruit(@PathVariable Long id) {
        service.deleteFruit(id);

        return ResponseEntity.noContent().build();
    }
}