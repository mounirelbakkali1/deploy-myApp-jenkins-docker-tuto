package ma.stock.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Getter
@NoArgsConstructor
public class ProfitByMonthDTO {
    private String month ;
    private BigDecimal profit ;

    public ProfitByMonthDTO(Integer month, BigDecimal profit) {
        this.month = SalesByMonthDTO.months.get(month);
        this.profit = profit;
    }

    public void setMonth(Integer month){
        this.month = SalesByMonthDTO.months.get(month-1);
    }

    public void setProfit(BigDecimal profit){
        this.profit = profit ;
    }
    public void setMonth(String month){
        this.month = month ;
    }
}
