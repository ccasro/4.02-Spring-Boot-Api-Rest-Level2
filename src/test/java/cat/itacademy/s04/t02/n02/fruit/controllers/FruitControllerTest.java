package cat.itacademy.s04.t02.n02.fruit.controllers;


import cat.itacademy.s04.t02.n02.fruit.dto.FruitRequestDTO;
import cat.itacademy.s04.t02.n02.fruit.model.Fruit;
import cat.itacademy.s04.t02.n02.fruit.repository.FruitRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class FruitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FruitRepository repository;

    @Test
    void shouldCreateFruitWhenDataIsValid() throws Exception {
        FruitRequestDTO request = new FruitRequestDTO("Banana", 5);

        mockMvc.perform(post("/fruits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturnBadRequestWhenDataIsInvalid() throws Exception {
        FruitRequestDTO request = new FruitRequestDTO("",-1);

        mockMvc.perform(post("/fruits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnFruitWhenIdExists() throws Exception {
        Fruit saved = repository.save(new Fruit("Orange", 9));

        mockMvc.perform(get("/fruits/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()))
                .andExpect(jsonPath("$.name").value(saved.getName()))
                .andExpect(jsonPath("$.weightInKilos").value(saved.getWeightInKilos()));
    }

    @Test
    void shouldReturn404WhenFruitDoesNotExist() throws Exception {
        mockMvc.perform(get("/fruits/222"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnAllFruits() throws Exception {
        repository.save(new Fruit("Banana", 3));
        repository.save(new Fruit("Lemon", 4));

        mockMvc.perform(get("/fruits")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").isNumber())
                .andExpect(jsonPath("$[0].name").value("Banana"))
                .andExpect(jsonPath("$[0].weightInKilos").value(3))
                .andExpect(jsonPath("$[1].id").isNumber())
                .andExpect(jsonPath("$[1].name").value("Lemon"))
                .andExpect(jsonPath("$[1].weightInKilos").value(4));
    }

    @Test
    void shouldReturnEmptyListWhenNoFruitsExist() throws Exception {

        mockMvc.perform(get("/fruits"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void shouldUpdateFruitWhenDataIsValid() throws Exception {
        Fruit saved = repository.save(new Fruit("Apple", 3));
        FruitRequestDTO update = new FruitRequestDTO("Green Apple", 4);

        mockMvc.perform(put("/fruits/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Green Apple"))
                .andExpect(jsonPath("$.weightInKilos").value(4));
    }

    @Test
    void shouldReturn404WhenUpdatingNonExistentFruit() throws Exception {
        FruitRequestDTO update = new FruitRequestDTO("Kiwi", 1);

        mockMvc.perform(put("/fruits/900")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn400WhenUpdatingWithInvalidData() throws Exception {
        Fruit saved = repository.save(new Fruit("Apple", 3));
        FruitRequestDTO update = new FruitRequestDTO("", -1);

        mockMvc.perform(put("/fruits/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldDeleteFruitWhenIdExists() throws Exception {
        Fruit saved = repository.save(new Fruit("Apple", 3));

        mockMvc.perform(delete("/fruits/" + saved.getId()))
                .andExpect(status().isNoContent());

        assertFalse(repository.findById(saved.getId()).isPresent());
    }

    @Test
    void shouldReturn404WhenDeletingNonExistentFruit() throws Exception {
        mockMvc.perform(delete("/fruits/999"))
                .andExpect(status().isNotFound());
    }
}

