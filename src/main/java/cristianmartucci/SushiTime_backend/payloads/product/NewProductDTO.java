package cristianmartucci.SushiTime_backend.payloads.product;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record NewProductDTO(
        @NotEmpty(message = "Nome obbligatorio")
        String name,
        @NotEmpty(message = "Descrizione obbligatoria")
        String description,
        @NotNull(message = "Prezzo obbligatorio")
        Double price,
        @NotEmpty(message = "Image obbligatorio")
        String image,
        @NotEmpty(message = "Nome categoria obbligatorio")
        String category) {
}
