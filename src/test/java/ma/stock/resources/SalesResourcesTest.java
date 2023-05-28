package ma.stock.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ma.stock.entities.Category;
import ma.stock.entities.Product;
import ma.stock.entities.Sale;
import ma.stock.entities.enums.PaymentMethod;
import ma.stock.requests.SaleRequest;
import ma.stock.services.SalesService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class SalesResourcesTest {

    private final String prefix = "/api/v1/sales";

    @Autowired
    MockMvc mockMvc ;



    // mocked version of sale service
    @MockBean
    SalesService salesService;


    @Test
    void getAllSales() {
    }

    @Test
    @DisplayName("test find sale - (should be found)")
    void getSaleById() throws Exception {
        // TODO :  configure mocked service
        var mockedSale = mockedSale();
        doReturn(Optional.of(mockedSale)).when(salesService).findSale(1L);
        mockMvc.perform(MockMvcRequestBuilders.get(prefix+"/{id}",1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string(HttpHeaders.LOCATION,"/sales/1"))
                .andExpect(jsonPath("$.productQuantity").value(1))
                .andExpect(jsonPath("$.product.name").value("product 1"));

    }

    @Test
    @DisplayName("test find sale - (should return not found)")
    void shouldReturnNotFound() throws Exception {
        doReturn(Optional.empty()).when(salesService).findSale(1L);
        mockMvc.perform(MockMvcRequestBuilders.get(prefix+"/{id}",1L))
                .andExpect(status().isNotFound());
    }

    @Test
    void createNewSale() throws Exception {
        var mockedSale = mockedSale();
        var saleRequest = new SaleRequest(1L , 1L , 2L ,10,true , BigDecimal.valueOf(22),PaymentMethod.VISA,BigDecimal.TEN,BigDecimal.valueOf(199), LocalDate.parse("2023-05-09"));
        doReturn(mockedSale).when(salesService).saveSale(any());
        mockMvc.perform(post(prefix).content(mapToString(saleRequest)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION,"/sales/"+mockedSale.getId()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(mockedSale.getId()))
                .andExpect(jsonPath("$.product.name").value(mockedSale.getProduct().getName()))
                .andExpect(jsonPath("$.productQuantity").value(mockedSale.getProductQuantity()));
    }

    @Test
    void updateSale() {
    }



    @Test
    @DisplayName("should delete a sale ---SUCCESS")
    void shouldSucceedDeletingSale() throws Exception {
        var mockedSale = mockedSale();
        doReturn(Optional.of(mockedSale)).when(salesService).findSale(1L);
        doReturn(true).when(salesService).deleteSale(1L);
        mockMvc.perform(delete(prefix+"/{id}",1L))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("should faild deleting a sale")
    void shouldFaildDeletingSale() throws Exception {
        doReturn(Optional.empty()).when(salesService).findSale(1L);
        mockMvc.perform(delete(prefix+"/{id}",1L))
                .andExpect(status().isNotFound());
    }


    private Sale mockedSale() {
        return Sale.builder()
                .id(1L)
                .product(Product.builder()
                        .name("product 1")
                        .category(new Category(null,"uniuque"))
                        .purchasePrice(BigDecimal.valueOf(133))
                        .quantity(200)
                        .build())
                .productQuantity(1)
                .isDeliveryIncluded(true)
                .deliveryExpenses(BigDecimal.valueOf(22))
                .paymentMethod(PaymentMethod.VISA)
                .build();
    }
    private String mapToString(Object obj){
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}