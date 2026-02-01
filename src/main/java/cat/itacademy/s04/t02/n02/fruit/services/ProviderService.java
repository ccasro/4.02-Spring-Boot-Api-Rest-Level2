package cat.itacademy.s04.t02.n02.fruit.services;

import cat.itacademy.s04.t02.n02.fruit.dto.ProviderRequestDTO;
import cat.itacademy.s04.t02.n02.fruit.dto.ProviderResponseDTO;

import java.util.List;

public interface ProviderService {
    ProviderResponseDTO createProvider(ProviderRequestDTO dto);
    List<ProviderResponseDTO> getAllProviders();
}
