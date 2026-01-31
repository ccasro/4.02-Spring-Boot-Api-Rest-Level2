package cat.itacademy.s04.t02.n02.fruit.dto;

import jakarta.validation.constraints.NotBlank;

public record ProviderRequestDTO(@NotBlank(message = "Name must not be blank") String name, @NotBlank(message = "Country must not be blank") String country) {
}
