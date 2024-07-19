package cristianmartucci.SushiTime_backend.payloads.orderDetails;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record UpdateOrderDetailDTO(
        Integer quantity,
        Double price,
        @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$", message = "Formato UUID non valido")
        String orderId,
        String productName,
        String state) {
}
