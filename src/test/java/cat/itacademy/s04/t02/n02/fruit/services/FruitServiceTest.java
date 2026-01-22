package cat.itacademy.s04.t02.n02.fruit.services;

import cat.itacademy.s04.t02.n02.fruit.dto.FruitRequestDTO;
import cat.itacademy.s04.t02.n02.fruit.exception.FruitNotFoundException;
import cat.itacademy.s04.t02.n02.fruit.model.Fruit;
import cat.itacademy.s04.t02.n02.fruit.repository.FruitRepository;
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

    @InjectMocks
    private FruitService service;

    @Test
    void createFruitShouldSaveAndReturnFruit() {
        FruitRequestDTO request = new FruitRequestDTO("Banana", 3);
        Fruit savedFruit = new Fruit("Banana", 3);
        savedFruit.setId(1L);

        when(repository.save(any(Fruit.class))).thenReturn(savedFruit);

        Fruit result = service.createFruit(request);

        assertNotNull(result.getId());
        assertEquals("Banana", result.getName());
        assertEquals(3, result.getWeightInKilos());
        verify(repository, times(1)).save(any(Fruit.class));
    }

    @Test
    void shouldReturnFruitWhenExists() {
        Fruit fruit = new Fruit("apple",3);
        fruit.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(fruit));

        Fruit result = service.getFruitById(1L);

        assertEquals("apple", result.getName());
    }

    @Test
    void shouldThrowExceptionWhenNotExists() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(FruitNotFoundException.class, () -> service.getFruitById(1L));
    }

    @Test
    void shouldReturnAllFruitsFromRepository() {
        List<Fruit> fruits = List.of( new Fruit(1L, "Apple", 3), new Fruit(2L, "Orange", 4));

        when(repository.findAll()).thenReturn(fruits);

        List<Fruit> result = service.getAllFruits();

        assertEquals(2,result.size());
        verify(repository).findAll();
    }

    @Test
    void shouldUpdateFruitWhenIdExists() {
        Fruit existingFruit = new Fruit("Apple", 4);
        FruitRequestDTO dto = new FruitRequestDTO("Green Apple", 5);
        when(repository.findById(1L)).thenReturn(Optional.of(existingFruit));
        when(repository.save(any(Fruit.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        Fruit updated = service.updateFruit(1L, dto);

        assertEquals("Green Apple", updated.getName());
        assertEquals(5,updated.getWeightInKilos());

        verify(repository).findById(1L);
        verify(repository).save(any());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentFruit() {
        FruitRequestDTO dto = new FruitRequestDTO("Kiwi", 2);

        when(repository.findById(99L)).thenReturn(Optional.empty());

        FruitNotFoundException ex = assertThrows(FruitNotFoundException.class, () -> service.updateFruit(99L, dto));

        assertEquals("Fruit not found with id: 99", ex.getMessage());
        verify(repository).findById(99L);
        verify(repository, never()).save(any());
    }

    @Test
    void shouldDeleteFruitWhenIdExists() {
        Fruit fruit = new Fruit(1L, "Apple", 3);

        when(repository.findById(1L)).thenReturn(Optional.of(fruit));
        doNothing().when(repository).delete(fruit);

        service.deleteFruit(1L);

        verify(repository).findById(1L);
        verify(repository).delete(fruit);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentFruit() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        FruitNotFoundException ex = assertThrows(FruitNotFoundException.class, () -> service.deleteFruit(99L));

        assertEquals("Fruit not found with id: 99", ex.getMessage());
        verify(repository).findById(99L);
        verify(repository, never()).delete(any());
    }
}
