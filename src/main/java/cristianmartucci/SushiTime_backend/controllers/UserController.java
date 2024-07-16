package cristianmartucci.SushiTime_backend.controllers;

import cristianmartucci.SushiTime_backend.entities.User;
import cristianmartucci.SushiTime_backend.exceptions.BadRequestException;
import cristianmartucci.SushiTime_backend.payloads.RoleResponseDTO;
import cristianmartucci.SushiTime_backend.payloads.users.NewUserDTO;
import cristianmartucci.SushiTime_backend.payloads.users.UpdateUserDTO;
import cristianmartucci.SushiTime_backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
    public Page<User> getAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy){
        return this.userService.getAll(page, size, sortBy);
    }

    @PatchMapping("/{userId}/role")
    @PreAuthorize("hasAuthority('ADMIN')")
    public User changeRole(@PathVariable UUID userId , @RequestBody @Validated RoleResponseDTO body, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult.getAllErrors());
            throw new BadRequestException(bindingResult.getAllErrors());
        }
        return this.userService.changeRuolo(body , userId);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
    public User getUser(@PathVariable UUID userId){
        return this.userService.findById(userId);
    }

    @PutMapping("/profile")
    public User updateUser(@AuthenticationPrincipal User user, @RequestBody @Validated UpdateUserDTO body, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult.getAllErrors());
            throw new BadRequestException(bindingResult.getAllErrors());
        }
        return this.userService.updateUser(user, body);
    }

    @DeleteMapping("/profile")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal User user){
        this.userService.delete(user);
    }
}
