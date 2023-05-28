package ma.stock.services;

import ma.stock.entities.Agent;
import ma.stock.entities.enums.AgentType;
import ma.stock.repositories.AgentsRepository;
import ma.stock.requests.AgentRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class AgentServiceTest {

    @Autowired
    AgentsRepository repository ;

    @Autowired
    AgentsService service ;

    @Test
    void findAllAgents() {
    }

    @Test
    void findAgent() {
    }

    @Test
    void updateAgent() {
    }

    @Test
    void saveAgent() {
        var agentRequest = new AgentRequest("Mounir","0612254159",AgentType.VENDOR);
        Agent savedAgent = service.saveAgent(agentRequest);
        assertEquals(repository.findById(savedAgent.getId()).get().getName(),"Mounir");

    }

    @Test
    void deleteAgent() {
    }
}