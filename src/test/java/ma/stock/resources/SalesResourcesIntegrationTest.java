package ma.stock.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ma.stock.entities.Agent;
import ma.stock.entities.Category;
import ma.stock.entities.Product;
import ma.stock.entities.enums.AgentType;
import ma.stock.entities.enums.PaymentMethod;
import ma.stock.repositories.AgentsRepository;
import ma.stock.repositories.ProductRepository;
import ma.stock.requests.SaleRequest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SalesResourcesIntegrationTest {

    private final  String ENDPOINT = "/api/v1/sales";

    @Autowired
    MockMvc mockMvc ;

    @Autowired
    AgentsRepository agentsRepository ;

    @Autowired
    ProductRepository productRepository ;


    @Test
    @Order(1)
    void shouldRetreiveAllSales() throws Exception{
        mockMvc
                .perform(MockMvcRequestBuilders.get(ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }
    @Test
    void shouldRetriveExistingSales() throws  Exception{
        // save one sale ;
        Agent x = Agent.builder()
                .name("x")
                .phoneNumber("0999999999")
                .agentType(AgentType.GUIDE)
                .build();
        Agent y = Agent.builder()
                .name("y")
                .phoneNumber("0999999999")
                .agentType(AgentType.VENDOR)
                .build();
        Agent savedX = agentsRepository.save(x);
        Agent savedY = agentsRepository.save(y);
        Product newPrd = Product.builder()
                .id(1L)
                .name("product 1")
                .category(new Category(1L, "unique"))
                .purchasePrice(BigDecimal.valueOf(133))
                .quantity(200)
                .build();
        Product product = productRepository.save(newPrd);
        var saleRequest = new SaleRequest(product.getId(),savedX.getId(),savedY.getId(),1,true, BigDecimal.valueOf(15), PaymentMethod.VISA,BigDecimal.TEN,BigDecimal.valueOf(199), LocalDate.parse("2023-05-09"));
        mockMvc
                .perform(
                        MockMvcRequestBuilders.post(ENDPOINT)
                                .content(asJsonString(saleRequest))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists());
    }
    @Test
    void shouldFailSavingSaleDueToValidationConstraint() throws Exception{
        Agent x = Agent.builder()
                .name("x")
                .phoneNumber("0999999999")
                .agentType(AgentType.GUIDE)
                .build();
        Agent y = Agent.builder()
                .name("y")
                .phoneNumber("0999999999")
                .agentType(AgentType.VENDOR)
                .build();
        Agent savedX = agentsRepository.save(x);
        Agent savedY = agentsRepository.save(y);
        Product newPrd = Product.builder()
                .id(1L)
                .name("product 1")
                .category(new Category(1L, "unique"))
                .purchasePrice(BigDecimal.valueOf(133))
                .quantity(100)
                .build();
        Product product = productRepository.save(newPrd);
        var saleRequest1 = new SaleRequest(null,savedX.getId(),savedY.getId(),1,true, BigDecimal.valueOf(15), PaymentMethod.VISA,BigDecimal.TEN,BigDecimal.valueOf(99), LocalDate.parse("2023-05-09"));
        var saleRequest2 = new SaleRequest(1000L,savedX.getId(),savedY.getId(),1,true, BigDecimal.valueOf(15), PaymentMethod.VISA,BigDecimal.TEN,BigDecimal.valueOf(99), LocalDate.parse("2023-05-09"));

        var saleRequestOutOfStock = new SaleRequest(product.getId(),savedX.getId(),savedY.getId(),101,true, BigDecimal.valueOf(15), PaymentMethod.VISA,BigDecimal.TEN,BigDecimal.valueOf(199), LocalDate.parse("2023-05-09"));

        mockMvc
                .perform(
                        MockMvcRequestBuilders.post(ENDPOINT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(saleRequest1))
                ).andExpect(status().isBadRequest());
        mockMvc
                .perform(
                        MockMvcRequestBuilders.post(ENDPOINT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(saleRequest2))
                ).andExpect(status().isBadRequest());
        mockMvc
                .perform(
                        MockMvcRequestBuilders.post(ENDPOINT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(saleRequestOutOfStock))
                ).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data[*].error").value("out of stock ! available (100 pieces)"))
                .andExpect(jsonPath("$.data[*].message").value("can't perform this operation"));
    }

    @Test
    void shouldSucceedSavingASale() throws Exception{
        // 100

        //200 100

        //100 10 10 5 15
        Agent x = Agent.builder()
                .name("x")
                .phoneNumber("0999999999")
                .agentType(AgentType.GUIDE)
                .build();
        Agent y = Agent.builder()
                .name("y")
                .phoneNumber("0999999999")
                .agentType(AgentType.VENDOR)
                .build();
        Agent savedX = agentsRepository.save(x);
        Agent savedY = agentsRepository.save(y);
        Product newPrd = Product.builder()
                .id(1L)
                .name("product 1")
                .category(new Category(1L, "unique"))
                .purchasePrice(BigDecimal.valueOf(133))
                .quantity(200)
                .build();
        Product product = productRepository.save(newPrd);

        var saleRequest = new SaleRequest(product.getId(),savedX.getId(),savedY.getId(),1,true, BigDecimal.valueOf(15), PaymentMethod.VISA,BigDecimal.TEN,BigDecimal.valueOf(200), LocalDate.parse("2023-05-09"));
        mockMvc
                .perform(
                        MockMvcRequestBuilders.post(ENDPOINT)
                                .content(asJsonString(saleRequest))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isCreated())
                .andExpect(jsonPath("$.profit").value(35));

    }

    private String asJsonString(Object obj){
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