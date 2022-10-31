package Recipes.recipe.Service.Implementation;

import Recipes.recipe.Entities.Recipe;
import Recipes.recipe.Entities.User;
import Recipes.recipe.Repositories.RecipeRepository;
import Recipes.recipe.Repositories.UserRepository;
import Recipes.recipe.Service.RecipeService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RecipeServiceImp implements RecipeService {

    private RecipeRepository recipeRepository;

    private UserRepository userRepository;

    public RecipeServiceImp(RecipeRepository recipeRepository, UserRepository userRepository) {
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
    }

    @Override
    public int addRecipeToUser(String email, Recipe recipe) {

        recipe.setDate(LocalDateTime.now());
        recipeRepository.save(recipe);
        User user = userRepository.findByEmail(email);
        user.getList().add(recipe);
        userRepository.save(user);

        return recipe.getId();
    }

    @Override
    public Optional<Recipe> getRecipeById(int id) {
        return recipeRepository.findById(id);
    }

    @Override
    public void deleteRecipeById(int id, String email) {

        int count = 0;
        List<Recipe> recipes = userRepository.findByEmail(email).getList();


        for (Recipe recipe : recipes) {
            if (recipe.getId() == id) count++;
        }

        if (recipeRepository.existsById(id) & count == 0) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        if (count == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        recipes.stream()
                .map(Recipe::getId)
                .forEach(number -> recipeRepository.deleteById(number));

    }

    @Override
    public Recipe updateRecipeById(int id, String email, Recipe newRecipe) {

        Recipe recipe = getRecipeById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        List<Recipe> recipes = userRepository.findByEmail(email).getList();

        if (!recipes.contains(recipe)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        newRecipe.setDate(LocalDateTime.now());
        newRecipe.setId(id);

        User user = userRepository.findByEmail(email);

        user.getList().remove(recipe);
        user.getList().add(newRecipe);

        userRepository.save(user);

        recipeRepository.save(newRecipe);

        return newRecipe;

    }

    @Override
    public List<Recipe> findByCategoryOrName(String category, String name) {

        if ((category.equals("none") & name.equals("none")) | (!category.equals("none") & !name.equals("none"))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        if (!category.equals("none")) {
            return recipeRepository.findByCategoryIgnoreCaseOrderByDateDesc(category);
        }
        return recipeRepository.findByNameContainingIgnoreCaseOrderByDateDesc(name);
    }
}
