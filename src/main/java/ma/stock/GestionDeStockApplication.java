package ma.stock;

import ma.stock.entities.Agent;
import ma.stock.entities.Category;
import ma.stock.entities.Product;
import ma.stock.entities.enums.AgentType;
import ma.stock.repositories.AgentsRepository;
import ma.stock.repositories.CategoryRepository;
import ma.stock.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

@SpringBootApplication
public class GestionDeStockApplication {

    public static void main(String[] args) {
        SpringApplication.run(GestionDeStockApplication.class, args);
    }

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    AgentsRepository agentsRepository ;
    @Autowired
    CategoryRepository categoryRepository ;
    @Autowired
    ProductRepository productRepository ;

    @Bean
    CommandLineRunner runner(){
        return args -> {
            /*var product1  = productRepository.save(new Product(null,"produit1", BigDecimal.valueOf(100),100,"E-7676",new Category(null,"category 1")));
            var product2  = productRepository.save(new Product(null,"produit2", BigDecimal.valueOf(199),200,"E-7677",new Category(null,"category 2")));
            var product3  = productRepository.save(new Product(null,"produit3", BigDecimal.valueOf(49),300,"E-7678",new Category(null,"category 3")));
            var product4  = productRepository.save(new Product(null,"produit4", BigDecimal.valueOf(299),100,"E-7679",new Category(null,"category 4")));
            var vendor = agentsRepository.save(new Agent(null,"Vendor x","0646198282", AgentType.VENDOR));
            var guide = agentsRepository.save(new Agent(null,"Guide y","07873273822", AgentType.GUIDE));*/

        };
    }
}
