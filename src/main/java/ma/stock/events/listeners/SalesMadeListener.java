package ma.stock.events.listeners;


import ma.stock.entities.Product;
import ma.stock.events.SalesMadeEvent;
import ma.stock.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SalesMadeListener {

    @Autowired
    ProductRepository productRepository ;


    @EventListener
    public void handleSaleMade(SalesMadeEvent event){
        // update stock
        var sale = event.getSale() ;
        var newQuantity = sale.getProduct().getQuantity() - sale.getProductQuantity();
        productRepository.updateStock(sale.getProduct().getId(),newQuantity);
        Optional<Product> product = productRepository.findById(sale.getProduct().getId());
        System.out.println("--------- new stock  : "+newQuantity);
    }
}
