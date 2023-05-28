package ma.stock.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import ma.stock.entities.Category;
import ma.stock.entities.Product;
import ma.stock.requests.ProductRequest;
import ma.stock.requests.UpdateProductRequest;
import ma.stock.services.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
class ProductResourceTest {

    @Autowired
    MockMvc mockMvc ;


    @MockBean
    ProductServiceImpl service ;








    @Test
    void createProduct() throws Exception {
        Product newPrd = Product.builder()
                .id(1L)
                .name("product 1")
                .category(new Category(1L, "unique"))
                .purchasePrice(BigDecimal.valueOf(133))
                .quantity(200)
                .build();
        var productRequest = new ProductRequest("prdName",10, "e-68",BigDecimal.TEN,1L);
        doReturn(newPrd).when(service).saveProduct(productRequest);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/products")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(asJsonString(
                                productRequest
                        ))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("product 1"));
    }

    @Test
    void shouldNotSaveProductDueToValidationConstraints() throws Exception {
        var productRequest = new ProductRequest(null,1,"",BigDecimal.TEN,1L);
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/products")
                                .accept(MediaType.APPLICATION_JSON)
                                .content(asJsonString(
                                        productRequest
                                ))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data[*].name").exists())
                .andExpect(jsonPath("$.data[*].name").value("must not be null"));

    }
/*    @Test
    void shouldReturnBadRequest() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/v1/products")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(
                                Product.builder()
                                        .name("product")
                                        .purchasePrice(BigDecimal.valueOf(199f))
                        ))
        ).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data").isString())
                .andExpect(jsonPath("$.data").value("the selling price should be greater than the purchase price"));
    }*/
    @Test
    void getAllProducts() throws Exception {
        Product newPrd = Product.builder()
                .name("product 1")
                .category(new Category(1L, "unique"))
                .purchasePrice(BigDecimal.valueOf(133))
                .quantity(200)
                .build();
        doReturn(List.of(newPrd)).when(service).findAllProducts();
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/v1/products")
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[*].id").exists());
    }

    @Test
    void updateProduct() throws Exception {
        Product existingPrd = Product.builder()
                .id(1L)
                .name("product 1")
                .category(new Category(1L, "unique"))
                .purchasePrice(BigDecimal.valueOf(133))
                .reference("E-65")
                .quantity(200)
                .build();
        Product updatedPrd = Product.builder()
                .id(1L)
                .name("product x")
                .category(new Category(2L, "new category"))
                .purchasePrice(BigDecimal.valueOf(133))
                .quantity(100)
                .reference("E-66")
                .build();
       var productRequest = new UpdateProductRequest("product x",100,"E-66",BigDecimal.valueOf(133),2L);
       doReturn(Optional.of(existingPrd)).when(service).findProduct(1L);
       doReturn(updatedPrd).when(service).updateProduct(productRequest,1L);
        mockMvc.perform(
               MockMvcRequestBuilders.put("/api/v1/products/1")
                       .contentType(MediaType.APPLICATION_JSON)
                       .accept(MediaType.APPLICATION_JSON)
                       .content(asJsonString(productRequest))
       ).andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.name").value("product x"))
                .andExpect(jsonPath("$.purchasePrice").value(BigDecimal.valueOf(133)))
                .andExpect(jsonPath("$.quantity").value(100))
                .andExpect(jsonPath("$.reference").value("E-66"))
                .andExpect(jsonPath("$.category.id").value(2L))
                .andExpect(jsonPath("$.category.name").value("new category"));
    }

    @Test
    void shouldDeleteProduct() throws Exception {
        Product existing = Product.builder()
                .name("product 1")
                .category(new Category(1L, "unique"))
                .purchasePrice(BigDecimal.valueOf(133))
                .quantity(200)
                .build();
        doReturn(Optional.of(existing)).when(service).findProduct(1L);
        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/api/v1/products/1")
                ).andExpect(status().isOk());
    }

    @Test
    void shouldReturnNotFound() throws  Exception{
        doReturn(Optional.empty()).when(service).findProduct(1L);
        mockMvc.perform(
                MockMvcRequestBuilders
                        .delete("/api/v1/products/1")
        ).andExpect(status().isNotFound());
    }

    private String asJsonString(Object obj){
        try{
            return  new ObjectMapper().writeValueAsString(obj);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}