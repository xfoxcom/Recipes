package Recipes.recipe.RecipeController;

import Recipes.recipe.Entities.Recipe;
import Recipes.recipe.Entities.Response;
import Recipes.recipe.Entities.User;
import Recipes.recipe.Repositories.RecipeRepository;
import Recipes.recipe.Repositories.UserRepository;
import Recipes.recipe.Service.Implementation.RecipeServiceImp;
import Recipes.recipe.Service.Implementation.UserServiceImp;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
public class Controller {

    private RecipeRepository repository;

    private UserRepository userRepository;

    private final UserServiceImp userServiceImp;

    private final RecipeServiceImp recipeServiceImp;

    public Controller(RecipeRepository repository, UserRepository userRepository, UserServiceImp userServiceImp, RecipeServiceImp recipeServiceImp) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.userServiceImp = userServiceImp;
        this.recipeServiceImp = recipeServiceImp;
    }

    @PostMapping("/api/recipe/new")
    public ResponseEntity<Integer> addRecipe(@Valid @RequestBody Recipe recipe, Authentication auth) {

        String email = auth.getName();

        return ResponseEntity.ok(recipeServiceImp.addRecipeToUser(email, recipe));
    }

    @GetMapping("/api/recipe/{id}")
    public Recipe getRecipe(@PathVariable int id) {

        return recipeServiceImp.getRecipeById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    }

    @DeleteMapping("/api/recipe/{id}")
    public void deleteRecipe(@PathVariable int id, Authentication auth) {
        String email = auth.getName();

        recipeServiceImp.deleteRecipeById(id, email);

    }

    @PutMapping("/api/recipe/{id}")
    public ResponseEntity<Recipe> updateRecipe(@PathVariable int id, @Valid @RequestBody Recipe recipe, Authentication auth) {

        String email = auth.getName();

        Recipe newRecipe = recipeServiceImp.updateRecipeById(id, email, recipe);

        return ResponseEntity.ok(newRecipe);

    }

    @GetMapping("/api/recipe/search")
    public List<Recipe> findByCatOrName(@RequestParam(name = "category", defaultValue = "none") String category, @RequestParam(name = "name", defaultValue = "none") String name) {

        return recipeServiceImp.findByCategoryOrName(category, name);

    }

    @PostMapping("/api/register")
    public void register(@Valid @RequestBody User user) {

        userServiceImp.registerUser(user);

    }
}
