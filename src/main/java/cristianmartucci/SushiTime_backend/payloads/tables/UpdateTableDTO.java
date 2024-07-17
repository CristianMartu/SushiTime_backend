package cristianmartucci.SushiTime_backend.payloads.tables;

import jakarta.validation.constraints.NotNull;

public record UpdateTableDTO(
        Integer number,
        Integer maxCapacity,
        Integer currentPeople) {
}
