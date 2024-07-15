package cristianmartucci.SushiTime_backend.payloads.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record NewUserDTO(
        @NotEmpty(message = "Nome obbligatorio")
        String name,
        @NotEmpty(message = "Email obbligatorio")
        @Email(message = "Formato email non valido")
        String email,
        @NotEmpty(message = "Password obbligatorio")
        String password) {
}
