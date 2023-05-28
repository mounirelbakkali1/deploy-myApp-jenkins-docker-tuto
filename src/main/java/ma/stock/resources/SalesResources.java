package ma.stock.resources;


import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import ma.stock.entities.Sale;
import ma.stock.entities.enums.PaymentMethod;
import ma.stock.requests.SaleRequest;
import ma.stock.services.SalesService;
import ma.stock.utilities.CustomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/v1/sales")
@CrossOrigin(origins = "http://127.0.0.1:5173/")
public class SalesResources {

    @Autowired
    SalesService service ;


    @GetMapping
    public ResponseEntity<?> getAllSales(){
        return ResponseEntity.ok(service.findAllSales());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getSaleById(@PathVariable Long id){
        return service.findSale(id)
                .map(sale -> {
                    try {
                        return ResponseEntity.ok()
                                .location(new URI("/sales/"+sale.getId()))
                                .body(sale);
                    }catch (URISyntaxException e){
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                    }
                }).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createNewSale(@RequestBody @Valid SaleRequest sale){
        var newSale = service.saveSale(sale);
        try {
            return ResponseEntity
                    .created(new URI("/sales/"+newSale.getId()))
                    .eTag(newSale.getId().toString())
                    .body(newSale);
        } catch (URISyntaxException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> updateSale(@RequestBody @Valid SaleRequest sale ,@PathVariable Long id){
        var existingSale = service.findSale(id);
        return existingSale.map(s->{
            service.updateSale(sale);
            try {
                return ResponseEntity.ok()
                        .location(new URI("/"+s.getId()))
                        .eTag(s.getId().toString())
                        .body(s);
            } catch (URISyntaxException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }).orElse(ResponseEntity.noContent().build());
    }

    // TODO : DELETE methode will be created after testing ---> TDD principle
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSale(@PathVariable Long id){
        var existingSale = service.findSale(id);
        return existingSale.map(sale -> {
                service.deleteSale(sale.getId());
                        return ResponseEntity.ok()
                                .build();
                }
        ).orElse(ResponseEntity.notFound().build());
    }
}


