package recipes;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class Controller {
    Map<Integer, Recipe> map = new ConcurrentHashMap<>();
    @PostMapping("/api/recipe/new")
    public Map<String, Integer> addRecipe(@RequestBody Recipe recipe) {
        int id = map.size() + 1;
        if (map.isEmpty()) {
            map.put(1, recipe);
            return Map.of("id", 1);
        } else map.put(id, recipe);
        return Map.of("id", id);
    }
    @GetMapping("/api/recipe/{id}")
    public Recipe getRecipe(@PathVariable int id) {
        if (!map.containsKey(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
return map.get(id);
    }
}
