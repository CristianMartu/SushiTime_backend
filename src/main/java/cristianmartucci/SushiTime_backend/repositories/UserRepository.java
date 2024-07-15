package cristianmartucci.SushiTime_backend.repositories;

import cristianmartucci.SushiTime_backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmailOrName(String email, String name);

    Optional<User> findByEmail(String email);
}
