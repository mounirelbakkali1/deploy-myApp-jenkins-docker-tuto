package ma.stock.resources;


import ma.stock.entities.Rapport;
import ma.stock.requests.ReportRequest;
import ma.stock.services.AgentsService;
import ma.stock.services.ProductService;
import ma.stock.services.SalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/report")
public class SalesAndResourceReportController {



    @Autowired
    SalesService salesService ;

    @Autowired
    ProductService productService;

    @Autowired
    AgentsService agentsService ;




    @PostMapping
    public ResponseEntity<?> generateReport(@RequestBody ReportRequest request){
        var totalNumberOfSalesLastMonth = salesService.findTotalSalesOfMonth(LocalDate.now().getMonthValue());
        var salesInTwoLastYears = salesService.findSalesByDateRange(List.of(request.getBeginYear(),request.getEndYear()));
        var totalNumberOfProduct = productService.countAllProducts() ;
        var totalNumberOfAgents = agentsService.countAllAgents() ;
        var SalesByCategoryReport = salesService.findSalesByCategoryInGivenDate(LocalDate.now());
        var salesByYear = salesService.findTotalSalesOfAYear(request.getEndYear());
        var chiffreAffaires = salesService.calculerChiffredAffaires(request.getEndYear());
        var profitInTwoYears = List.of(salesService.calculateProfitInGivenYear(request.getBeginYear()),salesService.calculateProfitInGivenYear(request.getEndYear()));
        return ResponseEntity.ok(
                Rapport.builder()
                        .salesBetweenTwoYears(salesInTwoLastYears)
                        .SalesByCategoryReport(SalesByCategoryReport)
                        .totalNumberOfSalesLastMonth(totalNumberOfSalesLastMonth)
                        .totalNumberOfAgents(totalNumberOfAgents)
                        .totalNumberOfProducts(totalNumberOfProduct)
                        .findTotalSalesOfAYear(salesByYear)
                        .chiffreDAffaires(chiffreAffaires)
                        .profitInTwoYears(profitInTwoYears)
                        .build()
        ) ;
    }
}
