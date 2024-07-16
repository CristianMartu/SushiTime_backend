package cristianmartucci.SushiTime_backend.payloads.categories;

import jakarta.validation.constraints.NotEmpty;

public record NewCategoryDTO(
        @NotEmpty(message = "Nome obbligatorio")
        String name) {
}
