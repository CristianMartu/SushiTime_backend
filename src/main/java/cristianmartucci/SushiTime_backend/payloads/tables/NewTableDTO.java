package cristianmartucci.SushiTime_backend.payloads.tables;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record NewTableDTO(
        @NotNull(message = "Numero tavolo obbligatorio")
        Integer number,
        @NotNull(message = "Numero massimo di persone obbligatorio")
        Integer maxCapacity){
}
