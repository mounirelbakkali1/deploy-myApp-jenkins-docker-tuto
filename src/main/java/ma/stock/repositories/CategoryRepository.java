package ma.stock.repositories;

import ma.stock.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Long> {

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN TRUE ELSE FALSE END FROM Category  c WHERE c.name = :name")
    boolean existsByName(String name);


    Optional<Category> findByName(String name);

}
