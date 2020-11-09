package no.hvl.dat250.FeedApp.Controllers;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import no.hvl.dat250.FeedApp.Models.User;
import no.hvl.dat250.FeedApp.Service.UserService;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    List<User> getUsers() {
        return userService.readAllUsers();
    }

    @GetMapping("/users/{id}")
    User getUser(@PathVariable(name = "id") int id) {
        return userService.readUserById(id);
    }

    @PostMapping("/users")
    User userPoll(@RequestBody User user) {
        userService.createUser(user);
        return user;
    }

    @DeleteMapping("/users/{id}")
    void deleteUser(@PathVariable(name = "id") int id) {
        userService.deleteUser(id);
    }

    @PutMapping("/users")
    User putUser(@RequestBody User user) {
        userService.updateUser(user);
        return user;
    }
}
