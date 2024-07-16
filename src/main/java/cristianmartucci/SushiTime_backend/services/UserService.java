package cristianmartucci.SushiTime_backend.services;

import cristianmartucci.SushiTime_backend.entities.User;
import cristianmartucci.SushiTime_backend.enums.Role;
import cristianmartucci.SushiTime_backend.exceptions.BadRequestException;
import cristianmartucci.SushiTime_backend.exceptions.NotFoundException;
import cristianmartucci.SushiTime_backend.payloads.users.NewUserDTO;
import cristianmartucci.SushiTime_backend.payloads.RoleResponseDTO;
import cristianmartucci.SushiTime_backend.payloads.users.UpdateUserDTO;
import cristianmartucci.SushiTime_backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User findById(UUID id){
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public User findByEmail(String email){
        return this.userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Utente con email: "+ email + " non trovato"));
    }

    public User save(NewUserDTO body){
        if (this.userRepository.findByEmail(body.email()).isPresent()){
            throw new BadRequestException("Email già in uso");
        }
        User user = new User(body.name(), body.surname(), body.email(), this.passwordEncoder.encode(body.password()));
        return this.userRepository.save(user);
    }

    public Page<User> getAll(int pageNumber, int pageSize, String sortBy){
        if (pageSize > 50) pageSize = 50;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        return this.userRepository.findAll(pageable);
    }

    public User changeRuolo(RoleResponseDTO body, UUID id){
        User user = this.findById(id);
        user.setRole(stringToRole(body.role()));
        return this.userRepository.save(user);
    }

    public User updateUser(User user, UpdateUserDTO body){
        if (this.userRepository.findByEmail(body.email()).isPresent()){
            throw new BadRequestException("Email già in uso");
        }
        if (body.name() != null) user.setName(body.name());
        if (body.surname() != null) user.setSurname(body.surname());
        if (body.email() != null) user.setEmail(body.email());
        if (body.password() != null) user.setPassword(this.passwordEncoder.encode(body.password()));
        return this.userRepository.save(user);
    }

    public void delete(User user){
        this.userRepository.delete(user);
    }

    private static Role stringToRole(String role){
        try {
            return Role.valueOf(role.toUpperCase());
        }catch (IllegalArgumentException error){
            throw new BadRequestException("Ruolo inserito non corretto! Ruoli disponibili: USER, ADMIN, STAFF");
        }
    }
}
