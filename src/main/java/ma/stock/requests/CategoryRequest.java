package ma.stock.requests;

import jakarta.validation.constraints.NotNull;
import ma.stock.utilities.Unique;

public record CategoryRequest(@NotNull @Unique(value = "category name") String  name) {
}
