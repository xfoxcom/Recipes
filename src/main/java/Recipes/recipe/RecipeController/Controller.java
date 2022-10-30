package Recipes.recipe.RecipeController;

import Recipes.recipe.Entities.Recipe;
import Recipes.recipe.Entities.Response;
import Recipes.recipe.Entities.User;
import Recipes.recipe.Repositories.RecipeRepository;
import Recipes.recipe.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    public Controller (RecipeRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }
    @PostMapping("/api/recipe/new")
    public Response addRecipe(@Valid @RequestBody Recipe recipe, Authentication auth) {
        String email = auth.getName();
        recipe.setDate(LocalDateTime.now());
        repository.save(recipe);
        User user = userRepository.findByEmail(email);
        user.getList().add(recipe);
        userRepository.save(user);
        return new Response(recipe.getId());
    }
    @GetMapping("/api/recipe/{id}")
    public Recipe getRecipe(@PathVariable int id) {
        if (repository.existsById(id)) {
            return repository.findById(id).get();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    @DeleteMapping("/api/recipe/{id}")
    public void deleteRecipe(@PathVariable int id, Authentication auth) {
        String email = auth.getName();
        int count = 0;
        List<Recipe> recipes = userRepository.findByEmail(email).getList();
        for (Recipe recipe : recipes) {
            if(recipe.getId() == id) count++;
        }
        if (repository.existsById(id) & count == 0) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        if (count == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        for (Recipe recipe : recipes) {
            if (recipe.getId() == id) {
                recipes.remove(recipe);
                repository.deleteById(id);
                throw new ResponseStatusException(HttpStatus.NO_CONTENT);
            }
        }
    }
    @PutMapping("/api/recipe/{id}")
    public void updateRecipe (@PathVariable int id, @Valid @RequestBody Recipe recipe, Authentication auth) {
    if (!repository.existsById(id)) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    String email = auth.getName();
    List<Recipe> recipes = userRepository.findByEmail(email).getList();
    if (!recipes.contains(repository.getById(id))) {
        throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }
    recipe.setDate(LocalDateTime.now());
    recipe.setId(id);
    userRepository.findByEmail(auth.getName()).getList().remove(repository.getById(id));
    userRepository.findByEmail(auth.getName()).getList().add(recipe);
    repository.save(recipe);
    throw new ResponseStatusException(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/api/recipe/search")
    public List<Recipe> findByCatOrName (@RequestParam(name = "category", defaultValue = "none") String category, @RequestParam(name = "name", defaultValue = "none") String name) {
        if ((category.equals("none") & name.equals("none")) | (!category.equals("none") & !name.equals("none"))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (!category.equals("none")) {
           return repository.findByCategoryIgnoreCaseOrderByDateDesc(category);
        }
        if (!name.equals("none")) {
            return repository.findByNameContainingIgnoreCaseOrderByDateDesc(name);
        }
        return List.of();
    }
    @PostMapping("/api/register")
    public void register (@Valid @RequestBody User user) {
        if (userRepository.existsById(user.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (!user.getEmail().matches(".+@.+\\..+")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong format");
        }
        if (user.getPassword().length() < 8) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong format");
        }
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        userRepository.save(new User(user.getEmail(), encoder.encode(user.getPassword()), new ArrayList<>()));
    }
}
