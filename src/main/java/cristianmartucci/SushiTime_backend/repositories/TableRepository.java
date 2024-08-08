package cristianmartucci.SushiTime_backend.repositories;

import cristianmartucci.SushiTime_backend.entities.Table;
import cristianmartucci.SushiTime_backend.enums.TableState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface TableRepository extends JpaRepository<Table, UUID> {
    Optional<Table> findByNumber(int number);

    @Query("SELECT t FROM Table t WHERE t.state = :state")
    Page<Table> getAllByState(TableState state, Pageable pageable);
}
