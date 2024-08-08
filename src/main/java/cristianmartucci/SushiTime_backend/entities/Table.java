package cristianmartucci.SushiTime_backend.entities;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import cristianmartucci.SushiTime_backend.enums.TableState;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;
import java.util.UUID;

@Entity
@jakarta.persistence.Table(name = "tables")
@Getter
@Setter
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(value = {"orders"})
public class Table {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private int number;
    @Column(name = "max_capacity")
    private int maxCapacity;
    @Column(name = "current_people")
    private int currentPeople;
    @Enumerated(value = EnumType.STRING)
    private TableState state;

    @OneToMany(mappedBy = "table", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Order> orders;

    public Table(int number, int maxCapacity) {
        this.number = number;
        this.maxCapacity = maxCapacity;
        this.currentPeople = maxCapacity;
        this.state = TableState.AVAILABLE;
    }
}
