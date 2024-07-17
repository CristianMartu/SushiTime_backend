package cristianmartucci.SushiTime_backend.controllers;

import cristianmartucci.SushiTime_backend.entities.Category;
import cristianmartucci.SushiTime_backend.entities.Table;
import cristianmartucci.SushiTime_backend.exceptions.BadRequestException;
import cristianmartucci.SushiTime_backend.payloads.categories.NewCategoryDTO;
import cristianmartucci.SushiTime_backend.payloads.categories.NewCategoryResponseDTO;
import cristianmartucci.SushiTime_backend.payloads.tables.NewTableDTO;
import cristianmartucci.SushiTime_backend.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
    @ResponseStatus(HttpStatus.CREATED)
    public NewCategoryResponseDTO save(@RequestBody @Validated NewCategoryDTO body, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            System.out.println(bindingResult.getAllErrors());
            throw new BadRequestException(bindingResult.getAllErrors());
        }
        return new NewCategoryResponseDTO(this.categoryService.save(body).getId());
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
    public Page<Category> getAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "name") String sortBy){
        return this.categoryService.getAll(page, size, sortBy);
    }

    @PatchMapping("/{categoryId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
    public Category update(@PathVariable UUID categoryId, @RequestBody @Validated NewCategoryDTO body, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            System.out.println(bindingResult.getAllErrors());
            throw new BadRequestException(bindingResult.getAllErrors());
        }
        return this.categoryService.update(categoryId, body);
    }

    @DeleteMapping("/{categoryId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
    public void delete(@PathVariable UUID categoryId){
        this.categoryService.delete(categoryId);
    }
}
