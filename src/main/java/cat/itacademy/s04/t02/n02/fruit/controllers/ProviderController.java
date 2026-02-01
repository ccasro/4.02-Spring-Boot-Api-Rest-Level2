package cat.itacademy.s04.t02.n02.fruit.controllers;

import cat.itacademy.s04.t02.n02.fruit.dto.ProviderRequestDTO;
import cat.itacademy.s04.t02.n02.fruit.dto.ProviderResponseDTO;
import cat.itacademy.s04.t02.n02.fruit.services.ProviderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/providers")
@RequiredArgsConstructor
public class ProviderController {

    private final ProviderService providerService;

    @PostMapping
    public ResponseEntity<ProviderResponseDTO> createProvider(@Valid @RequestBody ProviderRequestDTO request) {
        ProviderResponseDTO response = providerService.createProvider(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ProviderResponseDTO>> getAllProviders() {
        List<ProviderResponseDTO> response = providerService.getAllProviders();
        return ResponseEntity.ok(response);
    }
}
