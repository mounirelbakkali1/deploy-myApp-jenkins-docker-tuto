package ma.stock.repositories;

import jakarta.validation.constraints.Min;
import ma.stock.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    @Transactional
    @Modifying
    @Query("update Product p set p.quantity = :quantity where p.id = :id")
    void updateStock(@NonNull Long id, @Min(0) int quantity);
    default Product updateProduct(Product product){
        var existingProduct = findById(product.getId())
                .orElseThrow(()->new IllegalArgumentException("Product with id "+ product.getId() + "not found"));
        existingProduct.setName(product.getName());
        existingProduct.setCategory(product.getCategory());
        existingProduct.setQuantity(product.getQuantity());
        existingProduct.setPurchasePrice(product.getPurchasePrice());
        save(existingProduct);
        return existingProduct;
    }
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN TRUE ELSE FALSE END FROM Product  c WHERE c.name = :name")
    boolean existsByName(String name);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN TRUE ELSE FALSE END FROM Product  p WHERE p.reference= :reference")
    boolean existsByReference(String reference);
}
