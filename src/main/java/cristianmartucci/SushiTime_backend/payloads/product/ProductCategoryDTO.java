package cristianmartucci.SushiTime_backend.payloads.product;

import jakarta.validation.constraints.NotEmpty;

public record ProductCategoryDTO(
        @NotEmpty(message = "Nome categoria obbligatorio")
        String name) {
}
