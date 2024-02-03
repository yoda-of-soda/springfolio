package com.yoda_of_soda.springfolio.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.yoda_of_soda.springfolio.models.User;
import com.yoda_of_soda.springfolio.services.UserService;

@RestController
public class UserController {
    
    // @Autowired
    // private UserService userService;

    @GetMapping("/")
    public String Hello(){
        return "Hello World";
    }

    // @GetMapping("/users/{id}")
    // public User getUserById(@PathVariable UUID id) {
    //     return userService.getUserById(id);
    // }
}
