package cat.itacademy.s04.t02.n02.fruit.mapper;

import cat.itacademy.s04.t02.n02.fruit.dto.ProviderRequestDTO;
import cat.itacademy.s04.t02.n02.fruit.dto.ProviderResponseDTO;
import cat.itacademy.s04.t02.n02.fruit.model.Provider;

public final class ProviderMapper {

    private ProviderMapper() {}

    public static Provider toEntity(ProviderRequestDTO dto) {
        return new Provider(dto.name(), dto.country());
    }

    public static ProviderResponseDTO toResponseDTO(Provider entity) {
        return new ProviderResponseDTO(entity.getId(), entity.getName(), entity.getCountry());
    }
}
