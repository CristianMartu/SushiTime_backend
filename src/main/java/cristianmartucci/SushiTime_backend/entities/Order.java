package cristianmartucci.SushiTime_backend.entities;

import cristianmartucci.SushiTime_backend.enums.OrderState;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private LocalDateTime date;
    private OrderState state;

    @ManyToOne
    @JoinColumn(name = "table_id")
    private cristianmartucci.SushiTime_backend.entities.Table table;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails;

    public Order(LocalDateTime date, cristianmartucci.SushiTime_backend.entities.Table table) {
        this.date = date;
        this.table = table;
        this.state = OrderState.IN_PROGRESS;
    }
}
