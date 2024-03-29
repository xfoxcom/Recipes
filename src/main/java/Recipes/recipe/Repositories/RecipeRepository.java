package Recipes.recipe.Repositories;

import Recipes.recipe.Entities.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Integer> {
    List<Recipe> findByCategoryIgnoreCaseOrderByDateDesc (String category);
    List<Recipe> findByNameContainingIgnoreCaseOrderByDateDesc (String name);
}
