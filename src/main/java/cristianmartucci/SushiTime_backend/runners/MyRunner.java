package cristianmartucci.SushiTime_backend.runners;

import com.fasterxml.jackson.databind.ObjectMapper;
import cristianmartucci.SushiTime_backend.entities.Category;
import cristianmartucci.SushiTime_backend.entities.Product;
import cristianmartucci.SushiTime_backend.exceptions.BadRequestException;
import cristianmartucci.SushiTime_backend.repositories.CategoryRepository;
import cristianmartucci.SushiTime_backend.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.InputStream;

import org.springframework.stereotype.Component;



@Component
public class MyRunner implements CommandLineRunner {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {
        try {
            Resource resource = new ClassPathResource("static/products.json");
            InputStream inputStream = resource.getInputStream();

            ObjectMapper objectMapper = new ObjectMapper();

            ProductList productList = objectMapper.readValue(inputStream, ProductList.class);

            // Salvataggio categorie
            for (Product product : productList.getProducts()) {
                if (this.categoryRepository.findByName(product.getCategory().getName()).isPresent()){
                    continue;
                }
                this.categoryRepository.save(new Category(product.getCategory().getName().toUpperCase()));
            }

            // Salvataggio prodotti
            for (Product product : productList.getProducts()) {
                if (this.productRepository.findByNameAndCategoryOrImage(product.getImage(),product.getCategory().getName(), product.getImage()).isPresent() ||
                        this.productRepository.findByNumber(product.getNumber()).isPresent()){
                    continue;
                }
                Product saveProduct = new Product();
                saveProduct.setName(product.getName());
                saveProduct.setDescription(product.getDescription());
                saveProduct.setPrice(product.getPrice());
                saveProduct.setImage(product.getImage());
                saveProduct.setNumber(product.getNumber());
                saveProduct.setCategory(this.categoryRepository.findByName(product.getCategory().getName()).orElseThrow(() -> new BadRequestException("Categoria non trovata")));
                this.productRepository.save(saveProduct);
            }
            System.out.println("Prodotti totali: " + productList.getProducts().size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

