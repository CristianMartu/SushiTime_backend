package cristianmartucci.SushiTime_backend.entities;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String name;
    private String description;
    private double price;
    private String image;
    protected int number;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public Product(String name, String description, double price, String image, int number, Category category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
        this.number = number;
        this.category = category;
    }
}
