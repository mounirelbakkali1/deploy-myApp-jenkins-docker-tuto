package ma.stock.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ma.stock.entities.Agent;
import ma.stock.entities.enums.AgentType;
import ma.stock.repositories.AgentsRepository;
import ma.stock.requests.AgentRequest;
import ma.stock.services.AgentsService;
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

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import  java.util.List;
import java.util.Optional;


@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
class AgentsResourcesUnitTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AgentsService service ;

    @MockBean
    AgentsRepository repository ;

    @Test
    void findAllAgents() throws Exception {
        doReturn(List.of()).when(service).findAllAgents();
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/agents"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void shouldSuccessfullySaveAnAgent() throws Exception {
        var agentRequest = new AgentRequest("Mounir","06732637232",AgentType.VENDOR);
        doReturn(new Agent(1L,"Mounir","06732637232", AgentType.VENDOR)).when(service).saveAgent(agentRequest);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/agents").contentType(MediaType.APPLICATION_JSON).content(asJsonString(agentRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldFailSavingNewAgent() throws Exception{
        var agentRequest = new AgentRequest("Mounir","06732637232",AgentType.VENDOR);
        doReturn(true).when(repository).existsByName("Mounir");
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/agents").content(asJsonString(agentRequest)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    private String asJsonString(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void deleteAgent() throws Exception{
        Agent agent = new Agent(1L, "Mounir", "06732637232", AgentType.VENDOR);
        doReturn(Optional.of(agent)).when(service).findAgent(1L);
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/agents/1"))
                .andExpect(status().isOk());
    }
    @Test
    void shouldFailDeletingAnExistingAgent() throws  Exception{
        doReturn(Optional.empty()).when(service).findAgent(1L);
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/agents/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateAgent() throws Exception{
        var agentRequest = new AgentRequest("Mounir","06732637232",AgentType.VENDOR);
        Agent agent = new Agent(1L, "Mounir", "06732637232", AgentType.VENDOR);
        Agent updatedAgent = new Agent(1L, "Mounir El Bakkali", "06732637232", AgentType.VENDOR);
        doReturn(Optional.of(agent)).when(service).findAgent(1L);
        doReturn(updatedAgent).when(service).updateAgent(agentRequest);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/agents/1").contentType(MediaType.APPLICATION_JSON).content(asJsonString(agentRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Mounir El Bakkali"));
    }


    @Test
    void shouldFailUpdatingAnExistingAgent() throws Exception{
        var agentRequest = new AgentRequest("Mounir","06732637232",AgentType.VENDOR);
        doReturn(Optional.empty()).when(service).findAgent(1L);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/agents/1").contentType(MediaType.APPLICATION_JSON).content(asJsonString(agentRequest)))
                .andExpect(status().isNotFound());
    }
    @Test
    void ShouldFailedUpdatingAgentDueToConstraintsValidation() throws  Exception{
        var agentRequest = new AgentRequest(null,"06732637232",AgentType.VENDOR);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/agents/1").contentType(MediaType.APPLICATION_JSON).content(asJsonString(agentRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Error Accurate :("))
                .andExpect(jsonPath("$.data[*].name").exists())
                .andExpect(jsonPath("$.data[*].name").value("must not be null"));
    }

}