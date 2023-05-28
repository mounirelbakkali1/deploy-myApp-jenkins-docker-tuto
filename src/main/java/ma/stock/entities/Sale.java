package ma.stock.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ma.stock.entities.enums.AgentType;
import ma.stock.entities.enums.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDate;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.GenerationType.IDENTITY;
import static jakarta.persistence.TemporalType.DATE;

@Entity
@Table(name = "sales")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Sale {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id  ;

    @ManyToOne(optional = false,cascade = {PERSIST,MERGE, REFRESH})
    @JoinColumn(name = "product_id")
    Product product ;


    @Column(nullable = false,name = "product_quantity")
            @Min(value = 1,message = "quantity of sold product should be at least one")
    int productQuantity ;

    @Enumerated(EnumType.STRING)
    PaymentMethod paymentMethod;

    @Column(name = "is_delivery_inclusion", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isDeliveryIncluded;

    @Temporal(DATE)
            @Column(columnDefinition = "DATE DEFAULT NOW()")
    LocalDate dateOfSale ;

    @Column(name = "delivery_expenses")
            @Min(0)
    BigDecimal deliveryExpenses ;

    @Column(name = "selling_price",nullable = false,columnDefinition = "DECIMAL(10,2)")
    @Min(0)
    BigDecimal productSellingPrice;
    @Column(name = "commission_guide",nullable = false )
    @Min(0)
    @NotNull
    BigDecimal commissionGuidePrcentage;
    @Column(name = "commission_vendor",nullable = false )
    @Min(0)
    @NotNull
    BigDecimal commissionVendorPrcentage;
    @Column(name = "commission_guide_value",nullable = false )
    @Min(0)
    @NotNull
    BigDecimal commissionGuideValue;
    @Column(name = "commission_vendor_value",nullable = false )
    @Min(0)
    @NotNull
    BigDecimal commissionVendorValue;
    @Column(name = "profit",nullable = false)
    @Min(0)
    BigDecimal profit ;

    @Column(name = "purchase_price",nullable = false)
    @Min(0)
    BigDecimal productPurchasePrice ;

    @ManyToOne(cascade = {REFRESH,PERSIST,MERGE})
    @JoinColumn(name = "guide_id")
    Agent guide = Agent.builder().agentType(AgentType.GUIDE).build();

    @ManyToOne(cascade = {REFRESH,PERSIST,MERGE})
    @JoinColumn(name = "vendor_id")
    Agent vendor  = Agent.builder().agentType(AgentType.GUIDE).build();



}
