package ma.stock.services;


import ma.stock.entities.Product;
import ma.stock.requests.ProductRequest;
import ma.stock.requests.UpdateProductRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


public interface  ProductService {

    public Product saveProduct(ProductRequest product);
    public List<Product> findAllProducts();
    public Optional<Product> findProduct(Long id);
    public Product updateProduct(UpdateProductRequest product, Long productId);
    public boolean deleteProductById(Long id);

    public long countAllProducts();


}
