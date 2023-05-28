package ma.stock.services;

import ma.stock.entities.Agent;
import ma.stock.repositories.AgentsRepository;
import ma.stock.requests.AgentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class AgentServiceImpl implements AgentsService{

    @Autowired
    AgentsRepository repository ;


    @Override
    public List<Agent> findAllAgents() {
        return repository.findAll();
    }

    @Override
    public Optional<Agent> findAgent(Long id) {
        return repository.findById(id);
    }

    @Override
    public Agent updateAgent(AgentRequest agent) {
        return null;
    }

    @Override
    public Agent saveAgent(AgentRequest agent) {
        var newAgent = Agent.builder()
                .name(agent.name())
                .phoneNumber(agent.phoneNumber())
                .agentType(agent.agentType())
                .build();
        return repository.save(newAgent);
    }

    @Override
    public boolean deleteAgent(Long id) {
        repository.deleteById(id);
        return repository.findById(id).isEmpty();
    }

    @Override
    public long countAllAgents() {
        return repository.count();
    }
}
