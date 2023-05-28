package ma.stock.events;


import ma.stock.entities.Sale;
import org.springframework.context.ApplicationEvent;

public class SalesMadeEvent extends ApplicationEvent {

    private Sale sale ;
    public SalesMadeEvent(Object source, Sale sale) {
        super(source);
        this.sale= sale ;
    }
    public Sale getSale(){
        return sale;
    }
}
