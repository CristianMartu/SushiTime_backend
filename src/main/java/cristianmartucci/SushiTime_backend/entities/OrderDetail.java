package cristianmartucci.SushiTime_backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import cristianmartucci.SushiTime_backend.enums.OrderDetailState;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "order_details")
@Getter
@Setter
@NoArgsConstructor
@ToString
@JsonIgnoreProperties({"order"})
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private int quantity;
    private double price;
    @Enumerated(value = EnumType.STRING)
    private OrderDetailState state;
    @Column(name = "order_time")
    private LocalDateTime orderTime;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public OrderDetail(int quantity, double price, Order order, Product product, LocalDateTime orderTime) {
        this.quantity = quantity;
        this.price = price;
        this.order = order;
        this.state = OrderDetailState.IN_PROGRESS;
        this.product = product;
        this.orderTime = orderTime;
    }
}
