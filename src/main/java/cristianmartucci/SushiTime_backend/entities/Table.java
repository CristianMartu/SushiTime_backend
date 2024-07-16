package cristianmartucci.SushiTime_backend.entities;



import cristianmartucci.SushiTime_backend.enums.TableState;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Entity
@jakarta.persistence.Table(name = "tables")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Table {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private int number;
    @Enumerated(value = EnumType.STRING)
    private TableState state;

    public Table(int number) {
        this.number = number;
        this.state = TableState.AVAILABLE;
    }
}
