package cristianmartucci.SushiTime_backend.payloads.product;

import jakarta.validation.constraints.NotEmpty;

public record ProductNameDTO(
        @NotEmpty(message = "Nome obbligatorio")
        String name) {
}
