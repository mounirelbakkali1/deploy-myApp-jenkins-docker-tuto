package ma.stock.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import  java.util.List ;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SalesByYearDTO {
    Integer year ;
    List<SalesByMonthDTO> sales;
}
