package Recipes.recipe.Service.Implementation;

import Recipes.recipe.Entities.User;
import Recipes.recipe.Repositories.UserRepository;
import Recipes.recipe.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;

    public UserServiceImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void registerUser(User user) {

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

    @Override
    public void saveUser(User user) {

        userRepository.save(user);

    }

    @Override
    public User getUserByEmail(String email) {

        return userRepository.findByEmail(email);
    }
}
