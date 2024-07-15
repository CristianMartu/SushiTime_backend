package cristianmartucci.SushiTime_backend.controllers;

import cristianmartucci.SushiTime_backend.exceptions.BadRequestException;
import cristianmartucci.SushiTime_backend.payloads.users.NewUserDTO;
import cristianmartucci.SushiTime_backend.payloads.users.NewUserResponseDTO;
import cristianmartucci.SushiTime_backend.payloads.users.UserLoginDTO;
import cristianmartucci.SushiTime_backend.payloads.users.UserLoginResponseDTO;
import cristianmartucci.SushiTime_backend.services.AuthService;
import cristianmartucci.SushiTime_backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public NewUserResponseDTO register(@RequestBody @Validated NewUserDTO body, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult.getAllErrors());
            throw new BadRequestException(bindingResult.getAllErrors());
        }
        return new NewUserResponseDTO(this.userService.save(body).getId());
    }

    @PostMapping("/login")
    public UserLoginResponseDTO login(@RequestBody @Validated UserLoginDTO body, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult.getAllErrors());
            throw new BadRequestException(bindingResult.getAllErrors());
        }
        return new UserLoginResponseDTO(this.authService.authenticateUserAndGenerateToken(body));
    }
}
