package ma.stock.dto;


import lombok.*;

import java.math.BigInteger;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
public class SalesByCategoryDTO {
     String category ;
     long salesCount ;

    public SalesByCategoryDTO(String category, long salesCount) {
        this.category = category;
        this.salesCount = salesCount;
    }
}
