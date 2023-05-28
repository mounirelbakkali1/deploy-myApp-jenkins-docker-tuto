package ma.stock.resources;


import jakarta.validation.Valid;
import ma.stock.entities.Agent;
import ma.stock.requests.AgentRequest;
import ma.stock.services.AgentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/agents")
@CrossOrigin(origins = "http://127.0.0.1:5173/")

public class AgentsResources {

    @Autowired
    AgentsService service ;


    @GetMapping
    ResponseEntity<?> findAllAgents(){
        return ResponseEntity.ok(service.findAllAgents());
    }

    @PostMapping
    ResponseEntity<?> saveNewAgent(@RequestBody @Valid AgentRequest agent){
        service.saveAgent(agent);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteAgent(@PathVariable Long id){
        return service.findAgent(id).map(agent -> {
                service.deleteAgent(agent.getId());
                return ResponseEntity.ok().build();
                }
        ).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    ResponseEntity<?> updateAgent(@RequestBody @Valid AgentRequest agent,@PathVariable Long id){
        return service.findAgent(id)
                .map(agent1 -> {
                    var updated = service.updateAgent(agent);
                    return ResponseEntity.ok(updated);
                }).orElse(ResponseEntity.notFound().build());
    }

}
