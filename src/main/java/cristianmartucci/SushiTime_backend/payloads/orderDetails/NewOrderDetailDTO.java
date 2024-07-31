package cristianmartucci.SushiTime_backend.payloads.orderDetails;

import jakarta.validation.constraints.NotNull;

public record NewOrderDetailDTO(
        Integer quantity,
        Double price,
        @NotNull(message = "Numero prodotto obbligatorio")
        Integer number) {
}
