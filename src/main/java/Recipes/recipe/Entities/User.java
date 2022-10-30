package Recipes.recipe.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @Email
    @NotBlank
    private String email;
    @NotBlank
    @Size(min = 8)
    private String password;
    @OneToMany
    @JoinColumn(name = "user_recipes")
    private List<Recipe> list = new ArrayList<>();
}
