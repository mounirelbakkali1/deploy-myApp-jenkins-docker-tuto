package ma.stock.repositories;


import ma.stock.dto.ProfitByMonthDTO;
import ma.stock.dto.SalesByCategoryDTO;
import ma.stock.dto.SalesByMonthDTO;
import ma.stock.dto.SalesByYearDTO;
import ma.stock.entities.Category;
import ma.stock.entities.Sale;
import ma.stock.services.ProductServiceImpl;
import ma.stock.services.SalesServiceImpl;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Repository
public interface SalesRepository extends JpaRepository<Sale,Long> {
    /*default Sale updateSale(Sale sale){
        var existingSale = findById(sale.getId()).orElseThrow(() -> new IllegalArgumentException("sale passed to update is not exist"));
        existingSale.setDateOfSale(sale.getDateOfSale());
        existingSale.setDeliveryIncluded(sale.isDeliveryIncluded());
        existingSale.setDeliveryExpenses(sale.getDeliveryExpenses());
        existingSale.setPaymentMethod(sale.getPaymentMethod());
        existingSale.setProfit(SalesServiceImpl.calclulateProfit(sale.getProduct().getPurchasePrice(), sale.getProductSellingPrice()));
        existingSale.setProductQuantity(sale.getProductQuantity());
        return save(existingSale);
    }*/



    @Query("""
        SELECT new ma.stock.dto.SalesByCategoryDTO(p.category.name, COUNT(s)) AS salesCount FROM Sale s
            JOIN s.product p
            WHERE MONTH(s.dateOfSale) = :date
            GROUP BY p.category.name
        """)
    List<SalesByCategoryDTO> countSalesByProductCategoryAndDateOfSale(@Param("date")Integer date);

    List<Sale> findAllByDateOfSale(LocalDate date);

    @Query("""
    select count(s) from Sale  s where month(s.dateOfSale) =:month
    """)
    long countByDateOfSale(@Param("month") Integer month);


    /*SELECT extract (month from s.date_of_sale) as date, COUNT(s) as salesCount
    FROM sales s
    WHERE extract(month from s.date_of_sale) BETWEEN 5 AND 6
    GROUP BY extract(month from  s.date_of_sale)*/

   /* @Query(value = """
       SELECT new ma.stock.dto.SalesByYearDTO(
              year(s.dateOfSale),
              new ma.stock.dto.SalesByMonthDTO(month(s.dateOfSale), COUNT(s))
          )
                FROM Sale s
                WHERE s.dateOfSale BETWEEN :startDate AND :endDate
                GROUP BY s.dateOfSale
    """)
    List<SalesByYearDTO> countSalesByDateOfSaleEeachMonth(@Param("startDate") LocalDate startDate,
                                                          @Param("endDate") LocalDate endDate);*/


    @Query(value = """
     select new ma.stock.dto.SalesByMonthDTO(extract(month from s.dateOfSale) , count(s))   as salesCount
        from Sale s 
        WHERE extract( year from s.dateOfSale) = :year
        group by extract(month from s.dateOfSale)
    """)
    List<SalesByMonthDTO> countSalesByDateOfSaleInGivenYear(@Param("year") Integer year);




    @Query("""
    SELECT new ma.stock.dto.ProfitByMonthDTO(EXTRACT(MONTH FROM s.dateOfSale), SUM(s.profit))
        FROM Sale s
        WHERE  EXTRACT(YEAR FROM s.dateOfSale) = :year
        GROUP BY EXTRACT(MONTH FROM s.dateOfSale)
    """)
    List<ProfitByMonthDTO> calculateProfitInGivenMonth(@Param("year") Integer year);


    @Query("""
       select s from Sale  s where EXTRACT(YEAR FROM s.dateOfSale) = :year
""")
    Collection<Sale> findAllByYear(@Param("year") Integer year);
}
