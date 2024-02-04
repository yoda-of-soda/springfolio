package com.yoda_of_soda.springfolio.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.yoda_of_soda.springfolio.models.User;
import com.yoda_of_soda.springfolio.services.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class UserController {
    
    @Autowired
    private UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/")
    public String Hello(){
        return "Hello World";
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getUsers();
    }
    
    @GetMapping("/auth/users")
    public List<User> getAllUsersAuth() {
        return userService.getUsers();
    }

    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping("/users")
    public ResponseEntity<User> postMethodName(@RequestBody User user) {
        User createdUser = userService.addUser(user);
        if(createdUser == null) return ResponseEntity.internalServerError().build();
        return ResponseEntity.ok(createdUser);
    }
    
}
