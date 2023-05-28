package ma.stock.resources;


import jakarta.validation.Valid;
import ma.stock.requests.ProductRequest;
import ma.stock.requests.UpdateProductRequest;
import ma.stock.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/v1/products")
@CrossOrigin(origins = "http://127.0.0.1:5173/")
public class ProductResource {

    @Autowired
    ProductService service ;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getAllProducts(){
        return ResponseEntity.ok(service.findAllProducts());
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Object> createProduct(@RequestBody @Valid ProductRequest product){
        try {
            var newPrd = service.saveProduct(product) ;
            return ResponseEntity
                    .created(new URI("/"+newPrd.getId()))
                    .eTag(newPrd.getId().toString())
                    .body(newPrd);
        } catch (URISyntaxException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @PutMapping(value = "/{id}")
    @Transactional
    public ResponseEntity<Object> updateProduct(@RequestBody  @Valid UpdateProductRequest product , @PathVariable Long id ){
        if (service.findProduct(id).isEmpty()) return ResponseEntity.notFound().build();
        var updatedProduct =  service.updateProduct(product,id);
        try {
            return  ResponseEntity.ok()
                    .eTag(updatedProduct.getId().toString())
                    .location(new URI("/"+updatedProduct.getId()))
                    .body(updatedProduct);

        } catch (URISyntaxException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping(value = "/{id}")
    @Transactional
    public  ResponseEntity<Object> deleteCategory(@PathVariable Long id){
        if (service.findProduct(id).isEmpty()) return ResponseEntity.notFound().build();
        service.deleteProductById(id);
        return ResponseEntity.ok().build();
    }
}
