package Recipes.recipe.Service;


import Recipes.recipe.Entities.User;

import java.util.Optional;

public interface UserService {

    void registerUser(User user);

    void saveUser(User user);

    User getUserByEmail(String email);

}
