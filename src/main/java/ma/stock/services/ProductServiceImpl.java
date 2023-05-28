package ma.stock.services;

import ma.stock.entities.Category;
import ma.stock.entities.Product;
import ma.stock.repositories.CategoryRepository;
import ma.stock.repositories.ProductRepository;
import ma.stock.requests.ProductRequest;
import ma.stock.requests.UpdateProductRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    ProductRepository repository;

    @Autowired
    CategoryRepository categoryRepository ;


    @Override
    public Product saveProduct(ProductRequest product) {
        Category category = categoryRepository
                .findById(product.categoryId())
                .orElseThrow(()->new IllegalArgumentException("category can't not be null"));
        var newProduct = Product.builder()
                .name(product.name())
                .purchasePrice(product.purchasePrice())
                .quantity(product.quantity())
                .category(category)
                .reference(product.reference())
                .build();
        return repository.save(newProduct);
    }

    @Override
    public List<Product> findAllProducts() {
        return repository.findAll();
    }

    @Override
    public Optional<Product> findProduct(Long id) {
        return repository.findById(id);
    }

    @Override
    public Product updateProduct(UpdateProductRequest product , Long productId) {
        Category category = categoryRepository
                .findById(product.categoryId())
                .orElseThrow(()->new IllegalArgumentException("category can't not be null"));
        var updatedProduct = Product.builder()
                .id(productId)
                .name(product.name())
                .purchasePrice(product.purchasePrice())
                .quantity(product.quantity())
                .category(category)
                .reference(product.reference())
                .build();
        return repository.updateProduct(updatedProduct);
    }



    @Override
    public boolean deleteProductById(Long id) {
         repository.deleteById(id);
         return repository.findById(id).isEmpty();
    }

    @Override
    public long countAllProducts() {
        return repository.count();
    }
}
