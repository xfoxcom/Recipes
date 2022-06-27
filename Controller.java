package recipes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        recipe.setDate(LocalDateTime.now());
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
    recipe.setDate(LocalDateTime.now());
    recipe.setId(id);
    repository.save(recipe);
       // ResponseEntity.noContent();
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
}
