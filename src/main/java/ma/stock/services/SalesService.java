package ma.stock.services;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import ma.stock.dto.ProfitByMonthDTO;
import ma.stock.dto.SalesByCategoryDTO;
import ma.stock.dto.SalesByMonthDTO;
import ma.stock.dto.SalesByYearDTO;
import ma.stock.entities.Sale;
import ma.stock.requests.CategoryRequest;
import ma.stock.requests.SaleRequest;

import javax.swing.text.html.Option;

public interface SalesService {
    public Sale saveSale(SaleRequest sale);
    public Optional<Sale> findSale(Long id);
    public List<Sale> findAllSales();
    public Sale updateSale(SaleRequest sale);
    public boolean deleteSale(Long id);
    public List<Sale> findSalesByDate(LocalDate date);
    public List<SalesByCategoryDTO> findSalesByCategoryInGivenDate(LocalDate date);
    public List<SalesByYearDTO> findSalesByDateRange(List<Integer> range);
    public Long findTotalSalesOfMonth(Integer date);
    public List<SalesByMonthDTO> findTotalSalesOfAYear(Integer year);

    public List<ProfitByMonthDTO> calculateProfitInGivenYear(Integer year);
    BigDecimal calculerChiffredAffaires(Integer year);
}
