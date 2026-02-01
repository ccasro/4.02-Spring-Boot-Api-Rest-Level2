package cat.itacademy.s04.t02.n02.fruit.services;

import cat.itacademy.s04.t02.n02.fruit.dto.ProviderRequestDTO;
import cat.itacademy.s04.t02.n02.fruit.dto.ProviderResponseDTO;
import cat.itacademy.s04.t02.n02.fruit.exception.ConflictException;
import cat.itacademy.s04.t02.n02.fruit.exception.ProviderNotFoundException;
import cat.itacademy.s04.t02.n02.fruit.mapper.ProviderMapper;
import cat.itacademy.s04.t02.n02.fruit.model.Provider;
import cat.itacademy.s04.t02.n02.fruit.repository.ProviderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProviderServiceImpl implements ProviderService {

    private final ProviderRepository providerRepository;

    public ProviderServiceImpl(ProviderRepository providerRepository){
        this.providerRepository = providerRepository;
    }

    @Override
    public ProviderResponseDTO createProvider(ProviderRequestDTO dto){
        if(providerRepository.existsByName(dto.name())) {
            throw new ConflictException("Provider with name: " + dto.name() + " already exists");
        }

        Provider entity = ProviderMapper.toEntity(dto);
        Provider saved = providerRepository.save(entity);
        return ProviderMapper.toResponseDTO(saved);
    }

    @Override
    public List<ProviderResponseDTO> getAllProviders() {
        return providerRepository.findAll().stream().map(ProviderMapper::toResponseDTO).toList();
    }

    @Override
    public ProviderResponseDTO updateProvider(Long id, ProviderRequestDTO dto){
        Provider provider = providerRepository.findById(id)
                .orElseThrow(() -> new ProviderNotFoundException(id));

        if (providerRepository.existsByNameAndIdNot(dto.name(),id)){
            throw new ConflictException("Provider with name: " + dto.name() + " already exists");
        }

        provider.setName(dto.name());
        provider.setCountry(dto.country());

        Provider saved = providerRepository.save(provider);
        return ProviderMapper.toResponseDTO(saved);
    }
}
