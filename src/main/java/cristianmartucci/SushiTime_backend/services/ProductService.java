package cristianmartucci.SushiTime_backend.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import cristianmartucci.SushiTime_backend.entities.Category;
import cristianmartucci.SushiTime_backend.entities.Product;
import cristianmartucci.SushiTime_backend.exceptions.BadRequestException;
import cristianmartucci.SushiTime_backend.exceptions.NotFoundException;
import cristianmartucci.SushiTime_backend.payloads.product.NewProductDTO;
import cristianmartucci.SushiTime_backend.payloads.product.ProductCategoryDTO;
import cristianmartucci.SushiTime_backend.payloads.product.UpdateProductDTO;
import cristianmartucci.SushiTime_backend.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private Cloudinary cloudinary;

    public Product findById(UUID id){
        return this.productRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public Product findByName(String name){
        return this.productRepository.findByName(name.toUpperCase()).orElseThrow(() -> new NotFoundException("Prodotto " + name + " non trovato"));
    }

    public Product findByNumber(int number){
        return this.productRepository.findByNumber(number).orElseThrow(() -> new NotFoundException("Prodotto " + number + " non trovato"));
    }

    public Product save(NewProductDTO body, String imageUrl) {
        if (this.productRepository.findByNumber(body.number()).isPresent()) {
            throw new BadRequestException("Prodotto numero " + body.number() + " già presente");
        }
        if (this.productRepository.findByNameAndCategory(body.name().toUpperCase(), body.category().toUpperCase()).isPresent()) {
            throw new BadRequestException("Prodotto con nome " + body.name().toUpperCase() + " e categoria " + body.category().toUpperCase() + " già presente");
        }
        Product product = new Product(
                body.name().toUpperCase(),
                body.description(),
                body.price(),
                imageUrl,
                body.number(),
                this.categoryService.findByName(body.category().toUpperCase())
        );
        return this.productRepository.save(product);
    }

    public Page<Product> getAll(int pageNumber, int pageSize, String sortBy){
        if (pageSize > 50) pageSize = 50;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        return this.productRepository.findAll(pageable);
    }

    public Product update(UUID id, UpdateProductDTO body, String image) {
        Product product = this.findById(id);
        if (body.number() != null) {
            if (this.productRepository.findByNumber(body.number()).isPresent()) {
                throw new BadRequestException("Prodotto numero " + body.number() + " già presente");
            }
            product.setNumber(body.number());
        }
        if (body.category() != null && !body.category().isEmpty()) {
            Category category = this.categoryService.findByName(body.category().toUpperCase());
            product.setCategory(category);
        }
        if (body.name() != null && !body.name().isEmpty()) {
            if (this.productRepository.findByNameAndCategory(body.name().toUpperCase(), product.getCategory().getName().toUpperCase()).isPresent()) {
                throw new BadRequestException("Nome già presente nella categoria " + product.getCategory().getName());
            }
            product.setName(body.name().toUpperCase());
        }
        if (body.description() != null && !body.description().isEmpty()) {
            product.setDescription(body.description());
        }
        if (body.price() != null) {
            product.setPrice(body.price());
        }
        if (image != null && !image.isEmpty()) {
            if (this.productRepository.findByImage(image).isPresent()) {
                throw new BadRequestException("Immagine già presente");
            }
            product.setImage(image);
        }
        return this.productRepository.save(product);
    }

    public void delete(UUID id){
        this.productRepository.delete(this.findById(id));
    }

    public Page<Product> findByCategory(ProductCategoryDTO body, int pageNumber, int pageSize, String sortBy){
        if (pageSize > 50) pageSize = 50;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        return this.productRepository.findByCategory(body.name().toUpperCase(), pageable);
    }

    public String uploadImage(MultipartFile file) throws IOException {
        return (String) this.cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("url");
    }
}
