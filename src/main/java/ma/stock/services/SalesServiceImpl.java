package ma.stock.services;

import jakarta.persistence.criteria.CriteriaBuilder;
import ma.stock.dto.ProfitByMonthDTO;
import ma.stock.dto.SalesByCategoryDTO;
import ma.stock.dto.SalesByMonthDTO;
import ma.stock.dto.SalesByYearDTO;
import ma.stock.entities.Product;
import ma.stock.entities.Sale;
import ma.stock.entities.enums.PaymentMethod;
import ma.stock.events.SalesMadeEvent;
import ma.stock.repositories.AgentsRepository;
import ma.stock.repositories.CategoryRepository;
import ma.stock.repositories.ProductRepository;
import ma.stock.repositories.SalesRepository;
import ma.stock.requests.SaleRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Year;
import java.util.*;


@Service
@Transactional
public class SalesServiceImpl implements SalesService{
    @Autowired
    SalesRepository repository;

    @Autowired
    AgentsRepository agentsRepository ;

    @Autowired
    ProductRepository productRepository ;

    @Autowired
    CategoryRepository categoryRepository ;

    @Autowired
    private ApplicationEventPublisher eventPublisher;



    @Override
    public Sale saveSale(SaleRequest sale) {
        var guide = agentsRepository.findById(sale.guideId()).orElseThrow(()->new IllegalArgumentException("guide id can not be null"));
        var vendor = agentsRepository.findById(sale.vendorId()).orElseThrow(()->new IllegalArgumentException("vendor id can not be null"));
        var product = productRepository.findById(sale.productId()).orElseThrow(()->new IllegalArgumentException("product id can not be null"));
        if(product.getPurchasePrice().compareTo(sale.productSellingPrice())==1) throw  new IllegalArgumentException("selling price can not be less than or equal purchase price");
        if(sale.productQuantity() > product.getQuantity()) throw  new IllegalArgumentException("out of stock ! available ("+product.getQuantity()+" pieces)");
        var newSale = Sale.builder()
                .productQuantity(sale.productQuantity())
                .paymentMethod(sale.paymentMethod())
                .commissionGuidePrcentage(sale.commission())
                .commissionVendorPrcentage(BigDecimal.TEN)
                .isDeliveryIncluded(sale.isDeliveryIncluded());
        if (sale.isDeliveryIncluded()) newSale.deliveryExpenses(sale.deliveryExpenses());
        newSale
                .product(product)
                .dateOfSale(sale.dateOfSale())
                .guide(guide)
                .productPurchasePrice(product.getPurchasePrice())
                .vendor(vendor)
                .productSellingPrice(sale.productSellingPrice())
                .profit(calclulateProfit(product , sale))
                .commissionGuideValue((sale.commission().divide(BigDecimal.valueOf(100))).multiply(calculateProfitInit(product,sale)))
                .commissionVendorValue(BigDecimal.valueOf(0.1).multiply(calculateProfitInit(product,sale)));
        var sale1 = repository.save(newSale.build());
        eventPublisher.publishEvent(new SalesMadeEvent(this,sale1));
        return sale1;
    }

    @Override
    public Optional<Sale> findSale(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Sale> findAllSales() {
        return repository.findAll();
    }

    @Override
    public Sale updateSale(SaleRequest sale) {
        return null;
    }

    @Override
    public boolean deleteSale(Long id) {
        repository.deleteById(id);
        return repository.findById(id).isEmpty();
    }

    @Override
    public List<Sale> findSalesByDate(LocalDate date) {
        return repository.findAllByDateOfSale(date);
    }

    @Override
    public List<SalesByCategoryDTO> findSalesByCategoryInGivenDate(LocalDate date) {

        var categories = categoryRepository.findAll();
        var salesCategory =  repository.countSalesByProductCategoryAndDateOfSale(date.getMonthValue());
        Set<String> existingCategories = new HashSet<>();

        for (var dto : salesCategory) {
            existingCategories.add(dto.getCategory());
        }
        for (var category : categories) {
            String categoryName = category.getName();
            if (!existingCategories.contains(categoryName)) {
                SalesByCategoryDTO dto = new SalesByCategoryDTO();
                dto.setCategory(categoryName);
                dto.setSalesCount(0);
                salesCategory.add(dto);
            }
        }
        return salesCategory;
    }

    @Override
    public List<SalesByYearDTO> findSalesByDateRange(List<Integer> range) {
        if(range.size()!=2) throw new IllegalArgumentException("range of sale should be list of two dates (starting and ending date)");
        var startingDate = range.get(0);
        var endDate = range.get(1);
        var rst1 = findTotalSalesOfAYear(startingDate);
        var rest2 = findTotalSalesOfAYear(endDate);
        var year1  = new SalesByYearDTO(startingDate,rst1);
        var year2 = new SalesByYearDTO(endDate,rest2);
        return List.of(year1,year2);
    }

    @Override
    public Long findTotalSalesOfMonth(Integer date) {
        return repository.countByDateOfSale(date);
    }


    @Override
    public List<SalesByMonthDTO> findTotalSalesOfAYear(Integer year){
        List<SalesByMonthDTO> salesByMonthDTOS = repository.countSalesByDateOfSaleInGivenYear(year);
        Set<String> existingmonths  = new HashSet<>();
        for (var dto : salesByMonthDTOS) existingmonths.add(dto.getMonth());
        var months = SalesByMonthDTO.months;
        for (var m : months){
            if (!existingmonths.contains(m)){
                SalesByMonthDTO dto = new SalesByMonthDTO();
                dto.setSalesCount(0);
                dto.setMonth(m);
                salesByMonthDTOS.add(dto);
            }
        }
        salesByMonthDTOS.sort(new Comparator<>() {
            @Override
            public int compare(SalesByMonthDTO dto1, SalesByMonthDTO dto2) {
                int index1 = months.indexOf(dto1.getMonth());
                int index2 = months.indexOf(dto2.getMonth());
                return Integer.compare(index1, index2);
            }
        });
        return salesByMonthDTOS ;
    }

    @Override
    public List<ProfitByMonthDTO> calculateProfitInGivenYear(Integer year) {
        List<ProfitByMonthDTO> profit = repository.calculateProfitInGivenMonth(year);
        Set<String> existingMonths = new HashSet<>();
        for (var p : profit) existingMonths.add(p.getMonth());
        var months = SalesByMonthDTO.months;
        for (var m : months){
            if (!existingMonths.contains(m)) {
                var newDTO =  new ProfitByMonthDTO();
                newDTO.setMonth(m);
                newDTO.setProfit(BigDecimal.ZERO);
                profit.add(newDTO);
            }
        }
        profit.sort(new Comparator<>() {
            @Override
            public int compare(ProfitByMonthDTO dto1, ProfitByMonthDTO dto2) {
                int index1 = months.indexOf(dto1.getMonth());
                int index2 = months.indexOf(dto2.getMonth());
                return Integer.compare(index1, index2);
            }
        });
        return profit ;
    }



    @Override
    public BigDecimal calculerChiffredAffaires(Integer year) {
        return repository.findAllByYear(year)
                .stream()
                .map(sale -> sale.getProfit().multiply(BigDecimal.valueOf(sale.getProductQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    public static BigDecimal calclulateProfit(Product product , SaleRequest sale) {
        var profitInit =  calculateProfitInit(product,sale) ;
        var profit = profitInit;
        if (sale.isDeliveryIncluded()) {
            if(sale.deliveryExpenses().compareTo(sale.productSellingPrice())==1) throw new IllegalArgumentException("delivery expenses can not be greater or equals the selling price");
            profit =  profit.subtract(sale.deliveryExpenses());  // subtract delivery expenses if any
        }
        profit = profit.subtract(profitInit.multiply(sale.commission().divide(BigDecimal.valueOf(100)))) ; // subtract the guide commission
        profit  =  profit.subtract(profitInit.multiply(BigDecimal.valueOf( 0.1)));             // subtract the vendor commission  (5%)
        // double check profit validity
        // if(BigDecimal.ZERO.compareTo(profit)==1) throw new IllegalArgumentException("can not save sale with negative profit , please try to minimise costs !");
        return profit;
    }

    private static BigDecimal calculateProfitInit(Product product, SaleRequest sale) {
        var  purchasePrice  = product.getPurchasePrice();
        var  sellingPrice  = sale.productSellingPrice();
        var profitInit = sellingPrice.subtract(purchasePrice);// initial  profit value
        if(Objects.equals(sale.paymentMethod(),PaymentMethod.VISA)) profitInit = profitInit.subtract(profitInit.multiply(BigDecimal.valueOf(0.05f))); // subtract 5% if VISA is the PM
        return profitInit;
    }

}
