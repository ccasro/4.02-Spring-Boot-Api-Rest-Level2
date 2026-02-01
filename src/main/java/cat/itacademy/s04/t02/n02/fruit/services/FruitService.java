package cat.itacademy.s04.t02.n02.fruit.services;

import cat.itacademy.s04.t02.n02.fruit.dto.FruitRequestDTO;
import cat.itacademy.s04.t02.n02.fruit.dto.FruitResponseDTO;
import cat.itacademy.s04.t02.n02.fruit.exception.FruitNotFoundException;
import cat.itacademy.s04.t02.n02.fruit.exception.ProviderNotFoundException;
import cat.itacademy.s04.t02.n02.fruit.mapper.FruitMapper;
import cat.itacademy.s04.t02.n02.fruit.model.Fruit;
import cat.itacademy.s04.t02.n02.fruit.model.Provider;
import cat.itacademy.s04.t02.n02.fruit.repository.FruitRepository;
import cat.itacademy.s04.t02.n02.fruit.repository.ProviderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FruitService {

    private final FruitRepository repository;
    private final ProviderRepository providerRepository;

    public FruitResponseDTO createFruit(FruitRequestDTO dto) {
        Provider provider = providerRepository.findById(dto.providerId())
                .orElseThrow(() -> new ProviderNotFoundException(dto.providerId()));

        Fruit fruit = FruitMapper.toEntity(dto, provider);
        Fruit saved = repository.save(fruit);
        return FruitMapper.toResponseDTO(saved);
    }

    public FruitResponseDTO getFruitById(Long id) {
        Fruit fruit = repository.findById(id)
                .orElseThrow(() -> new FruitNotFoundException(id));

        return FruitMapper.toResponseDTO(fruit);
    }

    public List<FruitResponseDTO> getAllFruits() {
        return repository.findAll()
                .stream()
                .map(FruitMapper::toResponseDTO)
                .toList();
    }

    public FruitResponseDTO updateFruit(Long id, FruitRequestDTO dto) {
        Fruit fruit = repository.findById(id)
                .orElseThrow(() -> new FruitNotFoundException(id));

        Provider provider = providerRepository.findById(dto.providerId())
                .orElseThrow(() -> new ProviderNotFoundException(dto.providerId()));

        fruit.setName(dto.name());
        fruit.setWeightInKilos(dto.weightInKilos());
        fruit.setProvider(provider);

        Fruit saved = repository.save(fruit);
        return FruitMapper.toResponseDTO(saved);
    }

    public void deleteFruit(Long id) {
        Fruit fruit = repository.findById(id)
                .orElseThrow(() -> new FruitNotFoundException(id));
        repository.delete(fruit);
    }
}