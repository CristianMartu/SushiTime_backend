package cristianmartucci.SushiTime_backend.payloads.orders;

import jakarta.validation.constraints.NotEmpty;

public record OrderStateResponseDTO(
        @NotEmpty(message = "Stato ordine obbligatorio")
        String state) {
}
