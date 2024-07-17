package cristianmartucci.SushiTime_backend.payloads.tables;

import jakarta.validation.constraints.NotEmpty;

public record TableStateResponseDTO(
        @NotEmpty(message = "Stato tavolo obbligatorio")
        String state) {
}
