package cristianmartucci.SushiTime_backend.payloads.users;

import jakarta.validation.constraints.NotEmpty;

public record RoleResponseDTO(
        @NotEmpty(message = "Ruolo obbligatorio")
        String role) {
}
