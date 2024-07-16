package cristianmartucci.SushiTime_backend.entities;

import cristianmartucci.SushiTime_backend.enums.OrderState;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Entity
@Table(name = "order_details")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private int quantity;
    private double price;
    @Enumerated(value = EnumType.STRING)
    private OrderState state;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public OrderDetail(Order order) {
        this.state = OrderState.IN_PROGRESS;
        this.order = order;
    }
}
