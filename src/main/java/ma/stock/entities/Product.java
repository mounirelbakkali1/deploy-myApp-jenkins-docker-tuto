package ma.stock.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ma.stock.utilities.Unique;
import org.hibernate.validator.constraints.ScriptAssert;

import java.math.BigDecimal;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "products")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id  ;
    @NotBlank(message = "product name is required")
    @Unique(value = "name",type = "product")

    String name ;
    @Column(name = "purchase_price",nullable = false,columnDefinition = "DECIMAL(10,2)")
            @Min(0)
    BigDecimal purchasePrice;

    @Min(1)
            @NotNull
    int quantity ;

    String reference  ;

    @ManyToOne(cascade = {MERGE, REFRESH,PERSIST},fetch = FetchType.EAGER,optional = false)
    @JoinColumn(name = "category_id")
    Category category ;


}
