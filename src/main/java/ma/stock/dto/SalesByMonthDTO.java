package ma.stock.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List ;

@NoArgsConstructor
@Getter
public class SalesByMonthDTO {
    public static final List<String> months = List.of(
            "January",
            "February",
            "March",
            "April",
            "May",
            "June",
            "July",
            "August",
            "Septembre",
            "October",
            "November",
            "December"
    );
    private String month;
    private long salesCount;

    public SalesByMonthDTO(Integer month, long salesCount) {
        this.month = months.get(month-1);
        this.salesCount = salesCount;
    }



    public void setMonth(Integer month){
        this.month = months.get(month-1);
    }
    public void setMonth(String month){
        this.month = month ;
    }
    public void setSalesCount(long salesCount){
        this.salesCount = salesCount ;
    }


}
