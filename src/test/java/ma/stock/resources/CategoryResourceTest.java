package ma.stock.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import ma.stock.entities.Category;
import ma.stock.requests.CategoryRequest;
import ma.stock.services.CategoryService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CategoryResourceTest {


    @MockBean
    CategoryService mockedService ;

    @Test
    @Order(1)
    void createCategory() throws Exception {
        // configure the mockService ;
        doReturn(new Category(1L,"category")).when(mockedService).saveCategory(any());
        var request = MockMvcRequestBuilders
                .post("/api/v1/categories")
                .content(asJsonString(new CategoryRequest("category")))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("category"));
    }


    @Test
    void shouldFailOnSavingNewCategory() throws Exception {
        var categoryRequest = new CategoryRequest("name");
        doReturn(null).when(mockedService).saveCategory(categoryRequest);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/categories"))
                .andExpect(status().isInternalServerError());
    }

    @Autowired
    MockMvc mockMvc ;

    @Test
    @Order(2)
    void findAllCategories() throws Exception {
        doReturn(List.of(new Category(1L,"category 1"))).when(mockedService).findAllCategories();
        var request = MockMvcRequestBuilders.get("/api/v1/categories");
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[*].id").exists());
    }



    @Test
    @Order(3)
    void deleteCategory() throws Exception {
        var categgory1 = new Category(1L,"category1");
        var category3 = new Category(3L,"category2");
        doReturn(Optional.of(categgory1)).when(mockedService).findCategory(1L);
        doReturn(Optional.empty()).when(mockedService).findCategory(2L);
        doReturn(Optional.of(category3)).when(mockedService).findCategory(3L);
        doReturn(true).when(mockedService).deleteCategory(1L);
        doReturn(false).when(mockedService).deleteCategory(2L);
        doReturn(false).when(mockedService).deleteCategory(3L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/categories/1"))
                .andExpect(status().isOk());
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/categories/2"))
                .andExpect(status().isNotFound());
    }

    private String asJsonString(Object obj){
        try{
            return  new ObjectMapper().writeValueAsString(obj);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}