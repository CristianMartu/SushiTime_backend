package cristianmartucci.SushiTime_backend.payloads.tables;

import jakarta.validation.constraints.NotEmpty;

public record NewTableStateResponseDTO(
        @NotEmpty(message = "Stato tavolo obbligatorio")
        String state) {
}
