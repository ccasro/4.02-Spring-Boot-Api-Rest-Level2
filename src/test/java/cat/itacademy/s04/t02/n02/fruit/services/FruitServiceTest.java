package cat.itacademy.s04.t02.n02.fruit.services;

import cat.itacademy.s04.t02.n02.fruit.dto.FruitRequestDTO;
import cat.itacademy.s04.t02.n02.fruit.dto.FruitResponseDTO;
import cat.itacademy.s04.t02.n02.fruit.exception.FruitNotFoundException;
import cat.itacademy.s04.t02.n02.fruit.exception.ProviderNotFoundException;
import cat.itacademy.s04.t02.n02.fruit.model.Fruit;
import cat.itacademy.s04.t02.n02.fruit.model.Provider;
import cat.itacademy.s04.t02.n02.fruit.repository.FruitRepository;
import cat.itacademy.s04.t02.n02.fruit.repository.ProviderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FruitServiceTest {

    @Mock
    private FruitRepository repository;

    @Mock
    private ProviderRepository providerRepository;

    @InjectMocks
    private FruitService service;

    @Test
    void createFruitShouldSaveAndReturnFruit() {
        Provider provider = new Provider("Provider-1", "Spain");
        provider.setId(10L);

        FruitRequestDTO request = new FruitRequestDTO("Banana", 3, 10L);

        Fruit savedFruit = new Fruit("Banana", 3, provider);
        savedFruit.setId(1L);

        when(providerRepository.findById(10L)).thenReturn(Optional.of(provider));
        when(repository.save(any(Fruit.class))).thenReturn(savedFruit);

        FruitResponseDTO result = service.createFruit(request);

        assertEquals(1L, result.id());
        assertEquals("Banana", result.name());
        assertEquals(3, result.weightInKilos());
        assertEquals(10L, result.providerId());

        verify(providerRepository, times(1)).findById(10L);
        verify(repository, times(1)).save(any(Fruit.class));
    }

    @Test
    void createFruitShouldThrowWhenProviderDoesNotExist() {
        FruitRequestDTO request = new FruitRequestDTO("Banana", 3, 999L);

        when(providerRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ProviderNotFoundException.class, () -> service.createFruit(request));

        verify(providerRepository).findById(999L);
        verify(repository, never()).save(any());
    }

    @Test
    void shouldReturnFruitWhenExists() {
        Provider provider = new Provider("Provider-1", "Spain");
        provider.setId(10L);

        Fruit fruit = new Fruit("Apple", 3, provider);
        fruit.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(fruit));

        FruitResponseDTO result = service.getFruitById(1L);

        assertEquals(1L, result.id());
        assertEquals("Apple", result.name());
        assertEquals(3, result.weightInKilos());
        assertEquals(10L, result.providerId());
    }

    @Test
    void shouldThrowExceptionWhenNotExists() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(FruitNotFoundException.class, () -> service.getFruitById(1L));
    }

    @Test
    void shouldReturnAllFruitsFromRepository() {
        Provider p1 = new Provider("P1", "Spain"); p1.setId(10L);
        Provider p2 = new Provider("P2", "France"); p2.setId(11L);

        Fruit f1 = new Fruit("Apple", 3, p1);  f1.setId(1L);
        Fruit f2 = new Fruit("Orange", 4, p2); f2.setId(2L);

        when(repository.findAll()).thenReturn(List.of(f1, f2));

        List<FruitResponseDTO> result = service.getAllFruits();

        assertEquals(2, result.size());
        assertEquals("Apple", result.get(0).name());
        assertEquals(10L, result.get(0).providerId());

        verify(repository).findAll();
    }

    @Test
    void shouldUpdateFruitWhenIdExists() {
        Provider oldProvider = new Provider("Old", "ES"); oldProvider.setId(10L);
        Provider newProvider = new Provider("New", "UK"); newProvider.setId(20L);

        Fruit existingFruit = new Fruit("Apple", 4, oldProvider);
        existingFruit.setId(1L);

        FruitRequestDTO dto = new FruitRequestDTO("Green Apple", 5, 20L);

        when(repository.findById(1L)).thenReturn(Optional.of(existingFruit));
        when(providerRepository.findById(20L)).thenReturn(Optional.of(newProvider));
        when(repository.save(any(Fruit.class))).thenAnswer(invocation -> invocation.getArgument(0));

        FruitResponseDTO updated = service.updateFruit(1L, dto);

        assertEquals("Green Apple", updated.name());
        assertEquals(5, updated.weightInKilos());
        assertEquals(20L, updated.providerId());

        verify(repository).findById(1L);
        verify(providerRepository).findById(20L);
        verify(repository).save(any());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentFruit() {
        FruitRequestDTO dto = new FruitRequestDTO("Kiwi", 2, 10L);

        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(FruitNotFoundException.class, () -> service.updateFruit(99L, dto));

        verify(repository).findById(99L);
        verify(providerRepository, never()).findById(any());
        verify(repository, never()).save(any());
    }

    @Test
    void shouldThrowWhenUpdatingWithNonExistingProvider() {
        Provider oldProvider = new Provider("Old", "ES"); oldProvider.setId(10L);
        Fruit existingFruit = new Fruit("Apple", 4, oldProvider);
        existingFruit.setId(1L);

        FruitRequestDTO dto = new FruitRequestDTO("Apple 2", 5, 999L);

        when(repository.findById(1L)).thenReturn(Optional.of(existingFruit));
        when(providerRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ProviderNotFoundException.class, () -> service.updateFruit(1L, dto));

        verify(repository).findById(1L);
        verify(providerRepository).findById(999L);
        verify(repository, never()).save(any());
    }

    @Test
    void shouldDeleteFruitWhenIdExists() {
        Provider provider = new Provider("P1", "ES"); provider.setId(10L);
        Fruit fruit = new Fruit("Apple", 3, provider);
        fruit.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(fruit));
        doNothing().when(repository).delete(fruit);

        service.deleteFruit(1L);

        verify(repository).findById(1L);
        verify(repository).delete(fruit);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentFruit() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(FruitNotFoundException.class, () -> service.deleteFruit(99L));

        verify(repository).findById(99L);
        verify(repository, never()).delete(any());
    }
}
