package cristianmartucci.SushiTime_backend.payloads.orders;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record NewOrderDTO(
        @NotEmpty(message = "Id tavolo obbligatorio")
        @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$", message = "Formato UUID non valido")
        String tableId,
        @NotNull(message = "Prezzo del menu obbligatorio")
        Double menuPrice) {
}
