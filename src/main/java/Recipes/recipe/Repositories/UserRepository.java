package Recipes.recipe.Repositories;

import Recipes.recipe.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, String> {
    User findByEmail(String email);

}
