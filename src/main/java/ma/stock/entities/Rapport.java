package ma.stock.entities;

import lombok.*;
import ma.stock.dto.ProfitByMonthDTO;
import ma.stock.dto.SalesByCategoryDTO;
import ma.stock.dto.SalesByMonthDTO;
import ma.stock.dto.SalesByYearDTO;

import java.math.BigDecimal;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Rapport {
    private BigDecimal chiffreDAffaires;
    private Long  totalNumberOfSalesLastMonth;
    private List<SalesByYearDTO> salesBetweenTwoYears;
    private long  totalNumberOfProducts  ;
    private long  totalNumberOfAgents ;
    private List<SalesByCategoryDTO> SalesByCategoryReport;
    private List<SalesByMonthDTO> findTotalSalesOfAYear;
    private List<?> profitInTwoYears ;
}
