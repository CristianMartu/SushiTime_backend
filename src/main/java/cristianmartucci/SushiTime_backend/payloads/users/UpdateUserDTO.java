package cristianmartucci.SushiTime_backend.payloads.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record UpdateUserDTO(
        String name,
        String surname,
        @Email(message = "Formato email non valido")
        String email,
        String password) {
}
