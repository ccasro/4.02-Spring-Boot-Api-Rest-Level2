package cat.itacademy.s04.t02.n02.fruit.controllers;

import cat.itacademy.s04.t02.n02.fruit.dto.ProviderRequestDTO;
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


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProviderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProviderRepository repository;

    @Autowired
    private FruitRepository fruitRepository;

    @Test
    void shouldCreateProviderWhenDataIsValid() throws Exception {
        ProviderRequestDTO request = new ProviderRequestDTO("Banana", "fdfss");

        mockMvc.perform(post("/providers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturnBadRequestWhenDataIsInvalid() throws Exception {
        ProviderRequestDTO request = new ProviderRequestDTO("","fdfss");

        mockMvc.perform(post("/providers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnIsConflictWhenNameIsDuplicated() throws Exception {
        Provider saved = repository.save(new Provider("Provider", "Spain"));
        ProviderRequestDTO request = new ProviderRequestDTO("Provider","England");

        mockMvc.perform(post("/providers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldUpdateProviderWhenIdExistsAndDataIsValid() throws Exception {
        Provider saved = repository.save(new Provider("Provider-A", "Spain"));

        ProviderRequestDTO update = new ProviderRequestDTO("Provider-A-Updated", "France");

        mockMvc.perform(put("/providers/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()))
                .andExpect(jsonPath("$.name").value("Provider-A-Updated"))
                .andExpect(jsonPath("$.country").value("France"));
    }

    @Test
    void shouldReturn404WhenUpdatingNonExistingProvider() throws Exception {
        ProviderRequestDTO update = new ProviderRequestDTO("NewName", "Spain");

        mockMvc.perform(put("/providers/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn400WhenUpdatingProviderWithInvalidData() throws Exception {
        Provider saved = repository.save(new Provider("Provider-A", "Spain"));

        ProviderRequestDTO update = new ProviderRequestDTO("", "France");

        mockMvc.perform(put("/providers/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn409WhenUpdatingProviderWithDuplicatedName() throws Exception {
        Provider p1 = repository.save(new Provider("Provider-A", "Spain"));
        Provider p2 = repository.save(new Provider("Provider-B", "France"));

        ProviderRequestDTO update = new ProviderRequestDTO("Provider-A", "Italy");

        mockMvc.perform(put("/providers/" + p2.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldDeleteProviderWhenNoFruitsAssociated() throws Exception {
        Provider saved = repository.save(new Provider("Provider-A", "Spain"));

        mockMvc.perform(delete("/providers/" + saved.getId()))
                .andExpect(status().isNoContent());

        assertFalse(repository.findById(saved.getId()).isPresent());
    }

    @Test
    void shouldReturn404WhenDeletingNonExistingProvider() throws Exception {
        mockMvc.perform(delete("/providers/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn409WhenDeletingProviderWithAssociatedFruits() throws Exception {
        Provider provider = repository.save(new Provider("Provider-WithFruits", "Spain"));

        fruitRepository.save(new Fruit("Apple", 3, provider));

        mockMvc.perform(delete("/providers/" + provider.getId()))
                .andExpect(status().isConflict());
    }
}
