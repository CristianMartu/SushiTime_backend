package cristianmartucci.SushiTime_backend.services;

import cristianmartucci.SushiTime_backend.entities.Category;
import cristianmartucci.SushiTime_backend.entities.Product;
import cristianmartucci.SushiTime_backend.exceptions.BadRequestException;
import cristianmartucci.SushiTime_backend.exceptions.NotFoundException;
import cristianmartucci.SushiTime_backend.payloads.product.NewProductDTO;
import cristianmartucci.SushiTime_backend.payloads.product.UpdateProductDTO;
import cristianmartucci.SushiTime_backend.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryService categoryService;

    public Product findById(UUID id){
        return this.productRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public Product findByName(String name){
        return this.productRepository.findByName(name.toUpperCase()).orElseThrow(() -> new NotFoundException("Prodotto " + name + " non trovato"));
    }

    public Product save(NewProductDTO body){
        if (this.productRepository.findByNameOrImage(body.name().toUpperCase(), body.image()).isPresent()){
            throw new BadRequestException("Prodotto già presente");
        }
        Product product = new Product(body.name().toUpperCase(), body.description(), body.price(), body.image(), this.categoryService.findByName(body.category().toUpperCase()));
        return this.productRepository.save(product);
    }

    public Page<Product> getAll(int pageNumber, int pageSize, String sortBy){
        if (pageSize > 50) pageSize = 50;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        return this.productRepository.findAll(pageable);
    }

    public Product update(UUID id, UpdateProductDTO body){
        Product product = this.findById(id);
        if (body.name() != null){
            if (this.productRepository.findByName(body.name().toUpperCase()).isPresent()){
                throw new BadRequestException("Nome già presente");
            }
            product.setName(body.name().toUpperCase());
        }
        if (body.description() != null) product.setDescription(body.description());
        if (body.price() != null) product.setPrice(body.price());
        if (body.image() != null) {
            if (this.productRepository.findByImage(body.image()).isPresent()){
                throw new BadRequestException("Immagine già presente");
            }
            product.setImage(body.image());
        }
        if (body.category() != null) {
             Category category = this.categoryService.findByName(body.category().toUpperCase());
             product.setCategory(category);
        }
        return this.productRepository.save(product);
    }

    public void delete(UUID id){
        this.productRepository.delete(this.findById(id));
    }
}
