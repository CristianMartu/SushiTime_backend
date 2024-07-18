package cristianmartucci.SushiTime_backend.controllers;

import cristianmartucci.SushiTime_backend.entities.Category;
import cristianmartucci.SushiTime_backend.entities.Product;
import cristianmartucci.SushiTime_backend.exceptions.BadRequestException;
import cristianmartucci.SushiTime_backend.payloads.categories.NewCategoryDTO;
import cristianmartucci.SushiTime_backend.payloads.product.NewProductDTO;
import cristianmartucci.SushiTime_backend.payloads.product.NewProductResponseDTO;
import cristianmartucci.SushiTime_backend.payloads.product.UpdateProductDTO;
import cristianmartucci.SushiTime_backend.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
    @ResponseStatus(HttpStatus.CREATED)
    public NewProductResponseDTO save(@RequestBody @Validated NewProductDTO body, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            System.out.println(bindingResult.getAllErrors());
            throw new BadRequestException(bindingResult.getAllErrors());
        }
        return new NewProductResponseDTO(this.productService.save(body).getId());
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
    public Page<Product> getAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "category.name") String sortBy){
        return this.productService.getAll(page, size, sortBy);
    }

    @GetMapping("/{productId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
    public Product getProduct(@PathVariable UUID productId){
        return this.productService.findById(productId);
    }


    @PutMapping("/{productId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
    public Product update(@PathVariable UUID productId, @RequestBody @Validated UpdateProductDTO body, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            System.out.println(bindingResult.getAllErrors());
            throw new BadRequestException(bindingResult.getAllErrors());
        }
        return this.productService.update(productId, body);
    }

    @DeleteMapping("/{productId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID productId){
        this.productService.delete(productId);
    }
}
