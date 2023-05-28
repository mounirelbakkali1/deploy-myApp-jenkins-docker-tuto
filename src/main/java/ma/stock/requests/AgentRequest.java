package ma.stock.requests;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import ma.stock.entities.enums.AgentType;
import ma.stock.utilities.Unique;
import org.hibernate.validator.constraints.Length;

public record AgentRequest(@NotNull @Unique(type = "agent",value = "agent ame") String name , @Length(min = 10) String phoneNumber ,@Enumerated(EnumType.STRING) AgentType agentType) {
}
