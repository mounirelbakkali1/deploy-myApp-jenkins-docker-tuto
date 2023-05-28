package ma.stock.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import ma.stock.entities.enums.AgentType;
import ma.stock.utilities.Unique;

import static jakarta.persistence.EnumType.STRING;

@Entity
@Table(name = "agents")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Agent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id ;

    @NotBlank(message = "agent name is required")
            @Unique(value = "name",type = "agent")
    String name ;

    @Column(name = "phone_number")
            @NotBlank(message = "agent phone number is required")
    @Digits(message = "phone number is not valid (should be a 10 digits)",integer = 10,fraction = 0)
    String phoneNumber ;
    @Enumerated(STRING)
            @NotNull(message = "agent type is required")
    AgentType agentType ;

}
