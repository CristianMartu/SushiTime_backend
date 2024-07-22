package cristianmartucci.SushiTime_backend.repositories;

import cristianmartucci.SushiTime_backend.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    @Query("SELECT p FROM Product p WHERE p.name = :name AND p.category.name = :category OR p.image = :image")
    Optional<Product> findByNameAndCategoryOrImage(String name, String category, String image);

    @Query("SELECT p FROM Product p WHERE p.name = :name AND p.category.name = :category")
    Optional<Product> findByNameAndCategory(String name, String category);

    Optional<Product> findByName(String name);

    Optional<Product> findByNumber(int number);

    @Query("SELECT p FROM Product p WHERE p.category.name = :category")
    Page<Product> findByCategory(String category, Pageable pageable);
}
