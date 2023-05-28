package ma.stock.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import ma.stock.utilities.Unique;

import java.math.BigDecimal;

public record ProductRequest(@Unique(value = "name",type = "product") @NotNull String name , @NotNull @Min(1) int quantity , @NotNull @Unique(value = "product reference",type = "product_reference") String reference , @NotNull @Min(1) BigDecimal purchasePrice , @NotNull Long categoryId) {
}
