package ma.stock.resources;


import jakarta.validation.Valid;
import ma.stock.requests.CategoryRequest;
import ma.stock.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;


@RestController
@RequestMapping("/api/v1/categories")
@CrossOrigin(origins = "http://127.0.0.1:5173/")
public class CategoryResource {

    @Autowired
    CategoryService service ;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> findAllCategories(){
        return ResponseEntity.ok(service.findAllCategories());
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> createCategory(@RequestBody @Valid CategoryRequest category){
        /*service.saveCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED).build();*/
            var newCategory = Optional.of(service.saveCategory(category));
            return newCategory.map(cate->
            {
                try {
                    return ResponseEntity
                           .created(new URI("/"+cate.getId()))
                           .eTag(cate.getId().toString())
                           .body(cate);
                } catch (URISyntaxException e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
            }).orElseThrow(()->new RuntimeException("An Error Accurate when trying to save a category"));
    }

    @DeleteMapping(value = "/{id}")
    public  ResponseEntity<Object> deleteCategory(@PathVariable Long id){
        if (service.findCategory(id).isEmpty()) return ResponseEntity.notFound().build();
        if(service.deleteCategory(id)) return ResponseEntity.ok().build();
        else return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
