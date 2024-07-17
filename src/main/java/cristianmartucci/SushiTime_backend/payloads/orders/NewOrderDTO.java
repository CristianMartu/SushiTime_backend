package cristianmartucci.SushiTime_backend.payloads.orders;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record NewOrderDTO(
        //@NotNull(message = "Id del tavolo obbligatorio")
        UUID tableId) {
}
