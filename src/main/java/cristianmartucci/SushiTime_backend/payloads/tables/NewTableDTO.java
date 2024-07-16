package cristianmartucci.SushiTime_backend.payloads.tables;

import jakarta.validation.constraints.NotNull;

public record NewTableDTO(
        @NotNull(message = "Numero di persone obbligatorio")
        int number) {
}
