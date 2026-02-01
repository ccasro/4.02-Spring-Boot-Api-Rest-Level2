package cat.itacademy.s04.t02.n02.fruit.mapper;

import cat.itacademy.s04.t02.n02.fruit.dto.FruitRequestDTO;
import cat.itacademy.s04.t02.n02.fruit.dto.FruitResponseDTO;
import cat.itacademy.s04.t02.n02.fruit.model.Fruit;
import cat.itacademy.s04.t02.n02.fruit.model.Provider;

public final class FruitMapper {

        private FruitMapper() {}

    public static Fruit toEntity(FruitRequestDTO dto, Provider provider) {
        return new Fruit(dto.name(), dto.weightInKilos(), provider);
    }

    public static FruitResponseDTO toResponseDTO(Fruit entity) {
        return new FruitResponseDTO(
               entity.getId(),
               entity.getName(),
               entity.getWeightInKilos(),
               entity.getProvider().getId()
        );
    }
}
