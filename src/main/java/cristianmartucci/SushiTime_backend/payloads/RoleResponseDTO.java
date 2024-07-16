package cristianmartucci.SushiTime_backend.payloads;

import jakarta.validation.constraints.NotEmpty;

public record RoleResponseDTO(
        @NotEmpty(message = "Ruolo obbligatorio")
        String role) {
}
