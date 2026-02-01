package cat.itacademy.s04.t02.n02.fruit.controllers;


import cat.itacademy.s04.t02.n02.fruit.dto.FruitRequestDTO;
import cat.itacademy.s04.t02.n02.fruit.model.Fruit;
import cat.itacademy.s04.t02.n02.fruit.model.Provider;
import cat.itacademy.s04.t02.n02.fruit.repository.FruitRepository;
import cat.itacademy.s04.t02.n02.fruit.repository.ProviderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import java.util.UUID;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
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

    @Autowired
    private ProviderRepository providerRepository;

    @Test
    void shouldCreateFruitWhenDataIsValid() throws Exception {
        Provider provider = saveProvider();
        FruitRequestDTO request = new FruitRequestDTO("Banana", 5, provider.getId());

        mockMvc.perform(post("/fruits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturnBadRequestWhenDataIsInvalid() throws Exception {
        Provider provider = saveProvider();
        FruitRequestDTO request = new FruitRequestDTO("",-1,provider.getId());

        mockMvc.perform(post("/fruits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenProviderIdIsMissing() throws Exception {
        FruitRequestDTO request = new FruitRequestDTO("Banana", 5, null);

        mockMvc.perform(post("/fruits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnNotFoundWhenProviderDoesNotExistOnCreate() throws Exception {
        FruitRequestDTO request = new FruitRequestDTO("Banana", 5, 999999L);

        mockMvc.perform(post("/fruits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnFruitWhenIdExists() throws Exception {
        Fruit saved = saveFruit("Orange", 9);

        mockMvc.perform(get("/fruits/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()))
                .andExpect(jsonPath("$.name").value(saved.getName()))
                .andExpect(jsonPath("$.weightInKilos").value(saved.getWeightInKilos()))
                .andExpect(jsonPath("$.providerId").value(saved.getProvider().getId()));
    }

    @Test
    void shouldReturn404WhenFruitDoesNotExist() throws Exception {
        mockMvc.perform(get("/fruits/222"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnFruitsFilteredByProviderId() throws Exception {
        Provider p1 = saveProvider();
        Provider p2 = saveProvider();

        saveFruit("Apple", 3, p1);
        saveFruit("Banana", 5, p1);
        saveFruit("Orange", 4, p2);

        mockMvc.perform(get("/fruits")
                        .param("providerId", String.valueOf(p1.getId()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder("Apple", "Banana")))
                .andExpect(jsonPath("$[*].providerId", containsInAnyOrder(p1.getId().intValue(), p1.getId().intValue())));
    }

    @Test
    void shouldReturn404WhenFilteringByNonExistingProvider() throws Exception {
        mockMvc.perform(get("/fruits")
                        .param("providerId", "999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnAllFruits() throws Exception {
        saveFruit("Banana", 3);
        saveFruit("Lemon", 4);

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
        Fruit saved = saveFruit("Apple", 3);
        Provider provider = saveProvider();
        FruitRequestDTO update = new FruitRequestDTO("Green Apple", 4, provider.getId());

        mockMvc.perform(put("/fruits/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Green Apple"))
                .andExpect(jsonPath("$.weightInKilos").value(4));
    }

    @Test
    void shouldReturn404WhenUpdatingNonExistentFruit() throws Exception {
        Provider provider = saveProvider();
        FruitRequestDTO update = new FruitRequestDTO("Kiwi", 1, provider.getId());

        mockMvc.perform(put("/fruits/900")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn400WhenUpdatingWithInvalidData() throws Exception {
        Fruit saved = saveFruit("Apple", 3);

        Provider provider = saveProvider();
        FruitRequestDTO update = new FruitRequestDTO("", -1, provider.getId());

        mockMvc.perform(put("/fruits/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldDeleteFruitWhenIdExists() throws Exception {
        Fruit saved = saveFruit("Apple", 3);

        mockMvc.perform(delete("/fruits/" + saved.getId()))
                .andExpect(status().isNoContent());

        assertFalse(repository.findById(saved.getId()).isPresent());
    }

    @Test
    void shouldReturn404WhenDeletingNonExistentFruit() throws Exception {
        mockMvc.perform(delete("/fruits/999"))
                .andExpect(status().isNotFound());
    }

    //HELPERS

    private Provider saveProvider() {
        return providerRepository.save(new Provider("Provider-" + UUID.randomUUID(), "Spain"));
    }

    private Fruit saveFruit(String name, int weightInKilos) {
        Provider provider = saveProvider();
        return repository.save(new Fruit(name, weightInKilos, provider));
    }

    private Fruit saveFruit(String name, int weightInKilos, Provider provider){
        return repository.save(new Fruit(name, weightInKilos, provider));
    }
}

