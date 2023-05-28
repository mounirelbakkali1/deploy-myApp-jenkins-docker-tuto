package ma.stock.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import ma.stock.utilities.Unique;

import java.util.ArrayList;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;


@Entity
@Table(name = "categories", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Category
{
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id  ;
    @NotBlank(message = "Please provide a name for the category")
    @Column(nullable = false,unique = true)
    @Unique(value = "name",message = "There already a category with the same name !")
    private String name ;




}
