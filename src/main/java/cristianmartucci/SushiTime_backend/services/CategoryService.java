package cristianmartucci.SushiTime_backend.services;

import cristianmartucci.SushiTime_backend.entities.Category;
import cristianmartucci.SushiTime_backend.exceptions.BadRequestException;
import cristianmartucci.SushiTime_backend.exceptions.NotFoundException;
import cristianmartucci.SushiTime_backend.payloads.categories.NewCategoryDTO;
import cristianmartucci.SushiTime_backend.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public Category findById(UUID id){
        return this.categoryRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public Category findByName(String name){
        return this.categoryRepository.findByName(name.toUpperCase()).orElseThrow(() -> new NotFoundException("Categoria " + name.toUpperCase() + " non esistente"));
    }

    public Category save(NewCategoryDTO body){
        if (this.categoryRepository.findByName(body.name()).isPresent()){
            throw new BadRequestException("Nome categoria già esistente");
        }
        Category category = new Category(body.name().toUpperCase());
        return this.categoryRepository.save(category);
    }

    public Page<Category> getAll(int pageNumber, int pageSize, String sortBy){
        if (pageSize > 50) pageSize = 50;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        return this.categoryRepository.findAll(pageable);
    }

    public Category update(UUID id, NewCategoryDTO body){
        Category category = this.findById(id);
        if (this.categoryRepository.findByName(body.name()).isPresent()){
            throw new BadRequestException("Nome categoria già esistente");
        }
        category.setName(body.name().toUpperCase());
        return this.categoryRepository.save(category);
    }

    public void delete(UUID id){
        this.categoryRepository.delete(this.findById(id));
    }
}
