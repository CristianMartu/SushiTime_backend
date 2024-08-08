package cristianmartucci.SushiTime_backend.repositories;

import cristianmartucci.SushiTime_backend.entities.Order;
import cristianmartucci.SushiTime_backend.enums.OrderState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    @Query("SELECT o FROM Order o WHERE o.state = :state")
    Page<Order> getAllOrderByState(OrderState state, Pageable pageable);
}
