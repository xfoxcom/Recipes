package recipes;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Integer> {
    List<Recipe> findRecipeByCategory (String category);
    List<Recipe> findRecipeByNameContaining (String name);
}
