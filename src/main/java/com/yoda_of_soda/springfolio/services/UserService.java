package com.yoda_of_soda.springfolio.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yoda_of_soda.springfolio.models.User;
// import com.yoda_of_soda.springfolio.repository.UserRepository;

@Service
public class UserService {

    // @Autowired
    // private final UserRepository userRepository;
    
    // public UserService(UserRepository userRepository){
    //     this.userRepository = userRepository;
    // }

    public UserService(){
    }

    // public User getUserById(UUID id) {
    //     return userRepository.findById(id).orElse(null);
    // }
}
