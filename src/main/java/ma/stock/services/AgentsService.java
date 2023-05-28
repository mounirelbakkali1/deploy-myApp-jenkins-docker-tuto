package ma.stock.services;

import ma.stock.entities.Agent;
import ma.stock.requests.AgentRequest;

import java.util.List;
import java.util.Optional;


public interface AgentsService {
    List<Agent> findAllAgents();
    Optional<Agent> findAgent(Long id);
    Agent updateAgent(AgentRequest agent);
    Agent saveAgent(AgentRequest agent);
    boolean deleteAgent(Long id);

    long countAllAgents();
}
