package Recipes.recipe.Service;


import Recipes.recipe.Entities.Recipe;

import java.util.List;
import java.util.Optional;

public interface RecipeService {

    int addRecipeToUser(String email, Recipe recipe);

    Optional<Recipe> getRecipeById(int id);

    void deleteRecipeById(int id, String email);

    Recipe updateRecipeById(int id, String email, Recipe newRecipe);

    List<Recipe> findByCategoryOrName(String category, String name);

}
