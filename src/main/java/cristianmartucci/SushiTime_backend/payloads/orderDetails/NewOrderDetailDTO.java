package cristianmartucci.SushiTime_backend.payloads.orderDetails;

import cristianmartucci.SushiTime_backend.entities.Order;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record NewOrderDetailDTO(
        Integer quantity,
        Double price,
//        @NotEmpty(message = "Id ordine obbligatorio")
//        @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$", message = "Formato UUID non valido")
//        String orderId,
        @NotEmpty(message = "Nome prodotto obbligatorio")
        String productName) {
}
