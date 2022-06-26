package recipes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.Valid;

@RestController
public class Controller {
    @Autowired
    private RecipeRepository repository;
    public Controller (RecipeRepository repository) {
        this.repository = repository;
    }
    @PostMapping("/api/recipe/new")
    public Response addRecipe(@Valid @RequestBody Recipe recipe) {
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

}
