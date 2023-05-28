package ma.stock.repositories;

import ma.stock.entities.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AgentsRepository extends JpaRepository<Agent, Long> {
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN TRUE ELSE FALSE END FROM Agent  c WHERE c.name = :name")
    boolean existsByName(String name);
}
