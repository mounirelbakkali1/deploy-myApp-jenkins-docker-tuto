package ma.stock.utilities;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ma.stock.repositories.AgentsRepository;
import ma.stock.repositories.CategoryRepository;
import ma.stock.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class UniqueValidator implements ConstraintValidator<Unique,String> {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    AgentsRepository agentsRepository ;

    @Autowired
    ProductRepository productRepository ;

    private String name;
    private String type  ;


    @Override
    public void initialize(Unique constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        name = constraintAnnotation.value();
        type = constraintAnnotation.type();
    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        var exist = switch (type) {
            case "category" -> categoryRepository.existsByName(name);
            case "agent" -> agentsRepository.existsByName(name);
            case "product" -> productRepository.existsByName(name);
            case "product_reference" -> productRepository.existsByReference(name);
            default -> false;
        };
        return !exist;
    }


}
