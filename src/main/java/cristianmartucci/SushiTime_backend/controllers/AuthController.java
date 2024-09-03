package cristianmartucci.SushiTime_backend.controllers;

import cristianmartucci.SushiTime_backend.exceptions.BadRequestException;
import cristianmartucci.SushiTime_backend.payloads.users.UserLoginDTO;
import cristianmartucci.SushiTime_backend.payloads.users.UserLoginResponseDTO;
import cristianmartucci.SushiTime_backend.services.AuthService;
import cristianmartucci.SushiTime_backend.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
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

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.CREATED)
    public UserLoginResponseDTO login(@RequestBody @Validated UserLoginDTO body, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult.getAllErrors());
            throw new BadRequestException(bindingResult.getAllErrors());
        }
        return new UserLoginResponseDTO(this.authService.authenticateUserAndGenerateToken(body));
    }

//    @PostMapping("/login")
//    @ResponseStatus(HttpStatus.CREATED)
//    public UserLoginResponseDTO login(@RequestBody @Validated UserLoginDTO body, BindingResult bindingResult, HttpServletResponse response){
//        if (bindingResult.hasErrors()) {
//            System.out.println(bindingResult.getAllErrors());
//            throw new BadRequestException(bindingResult.getAllErrors());
//        }
//        String token = this.authService.authenticateUserAndGenerateToken(body);
//        Cookie jwtCookie = new Cookie("jwtToken", token);
//        jwtCookie.setHttpOnly(false);
//        jwtCookie.setSecure(true);
//        jwtCookie.setPath("/");
//        jwtCookie.setMaxAge(7 * 24 * 60 * 60);
//        response.addCookie(jwtCookie);
//
////        String setCookieHeader = "jwtToken=" + token
////                + "; Max-Age=" + (7 * 24 * 60 * 60)
////                + "; Path=/"
//////                + "; HttpOnly"
////                + "; Secure" // Usa Secure se stai testando su HTTPS
////                + "; SameSite=None"; // None è richiesto per i cookie cross-site
////        response.setHeader("Set-Cookie", setCookieHeader);
//        return new UserLoginResponseDTO(token);
////        return new UserLoginResponseDTO("Accesso eseguito");
////        return new UserLoginResponseDTO(jwtCookie.toString());
//    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.CREATED)
    public UserLoginResponseDTO logout(){
        Cookie cookie = new Cookie("jwtToken", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        return new UserLoginResponseDTO("Logout effettuato con successo");
    }
}
