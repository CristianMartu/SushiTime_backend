package cristianmartucci.SushiTime_backend.repositories;

import cristianmartucci.SushiTime_backend.entities.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TableRepository extends JpaRepository<Table, UUID> {
}
