package cristianmartucci.SushiTime_backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import cristianmartucci.SushiTime_backend.enums.OrderDetailState;
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
@JsonIgnoreProperties({"order"})
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private int quantity;
    private double price;
    @Enumerated(value = EnumType.STRING)
    private OrderDetailState state;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public OrderDetail(int quantity, double price, Order order, Product product) {
        this.quantity = quantity;
        this.price = price;
        this.order = order;
        this.product = product;
        this.state = OrderDetailState.IN_PROGRESS;
    }
}
