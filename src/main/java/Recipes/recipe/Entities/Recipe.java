package Recipes.recipe.Entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "recipes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotEmpty
    @Size(min = 1)
    private String[] ingredients;
    @NotEmpty
    @Size(min = 1)
    private String[] directions;
    @NotBlank
    private String category;
    private LocalDateTime date;
}
