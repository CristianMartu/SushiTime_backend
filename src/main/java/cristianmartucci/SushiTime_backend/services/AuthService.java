package cristianmartucci.SushiTime_backend.services;

import cristianmartucci.SushiTime_backend.entities.User;
import cristianmartucci.SushiTime_backend.exceptions.UnauthorizedException;
import cristianmartucci.SushiTime_backend.payloads.users.UserLoginDTO;
import cristianmartucci.SushiTime_backend.security.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTTools jwtTools;

    public String authenticateUserAndGenerateToken(UserLoginDTO payload){
        User user = userService.findByEmail(payload.email());
        if (passwordEncoder.matches(payload.password(), user.getPassword())){
            return jwtTools.createToken(user);
        } else {
            throw new UnauthorizedException("Credenziali non corrette");
        }
    }
}
