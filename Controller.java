package recipes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
public class Controller {
    @Autowired
    private RecipeRepository repository;
    public Controller (RecipeRepository repository) {
        this.repository = repository;
    }
    @PostMapping("/api/recipe/new")
    public Response addRecipe(@Valid @RequestBody Recipe recipe) {
        recipe.setDateTime(LocalDateTime.now());
        repository.save(recipe);
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
    public void deleteRecipe(@PathVariable int id) {
        if (repository.findById(id).isPresent()) {
            repository.deleteById(id);
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    @PutMapping("/api/recipe/{id}")
    public void updateRecipe (@PathVariable int id, @Valid @RequestBody Recipe recipe) {
    if (!repository.existsById(id)) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    recipe.setDateTime(LocalDateTime.now());
    recipe.setId(id);
    repository.save(recipe);
    }

    @GetMapping("/api/recipe/search")
    public List<Recipe> findBy (@RequestParam(name = "category") String category, @RequestParam(name = "name") String name) {
        if ((category == null & name == null) | (category != null & name != null)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        if (category != null) {
           return repository.findRecipeByCategory(category);
        }
        if (name != null) {
            return repository.findRecipeByNameContaining(name);
        }
        return List.of();
    }
}
