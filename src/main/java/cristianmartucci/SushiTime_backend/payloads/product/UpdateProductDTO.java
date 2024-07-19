package cristianmartucci.SushiTime_backend.payloads.product;

public record UpdateProductDTO(
        String name,
        String description,
        Double price,
        String image,
        Integer number,
        String category) {
}
