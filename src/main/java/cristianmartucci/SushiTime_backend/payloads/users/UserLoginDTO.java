package cristianmartucci.SushiTime_backend.payloads.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record UserLoginDTO(
        @NotEmpty(message = "Email obbligatoria")
        @Email(message = "Formato email non valido")
        String email,
        @NotEmpty(message = "Password obbligatoria")
        String password){
}
