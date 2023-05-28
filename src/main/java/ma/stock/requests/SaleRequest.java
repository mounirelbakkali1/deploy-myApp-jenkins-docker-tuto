package ma.stock.requests;

import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import ma.stock.entities.enums.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SaleRequest(@NotNull Long productId, @NotNull Long guideId , @NotNull Long vendorId , @NotNull @Min(1) int productQuantity , boolean isDeliveryIncluded, BigDecimal deliveryExpenses ,
                          @NotNull  PaymentMethod paymentMethod , @NotNull @Min(1) @Max(40) BigDecimal commission, @NotNull @Min(1) BigDecimal productSellingPrice,
                         @NotNull LocalDate dateOfSale){

}
