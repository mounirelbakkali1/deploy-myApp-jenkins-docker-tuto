package ma.stock.services;


import ma.stock.entities.Category;
import ma.stock.repositories.CategoryRepository;
import ma.stock.requests.CategoryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    CategoryRepository repository ;



    @Transactional
    public Category saveCategory(CategoryRequest category){
        var newCategory = Category.builder()
                .name(category.name())
                .build();
       return repository.save(newCategory);
    }

    public List<Category> findAllCategories() {
        return repository.findAll();
    }

    public Optional<Category> findCategory(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public boolean deleteCategory(Long id) {
        repository.deleteById(id);
        return repository.findById(id).isEmpty();
    }
}
