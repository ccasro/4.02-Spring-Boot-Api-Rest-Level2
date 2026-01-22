package cat.itacademy.s04.t02.n02.fruit.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record FruitRequestDTO( @NotBlank(message = "Name must not be blank") String name, @NotNull(message = "Weight is required") @Positive(message = "Weight must be greater than zero") Integer weightInKilos) {
}
