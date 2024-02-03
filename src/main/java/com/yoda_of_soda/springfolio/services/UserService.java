package com.yoda_of_soda.springfolio.services;

import java.util.List;
import org.springframework.stereotype.Service;
import com.yoda_of_soda.springfolio.models.User;
import com.yoda_of_soda.springfolio.repository.UserRepository;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public List<User> getUsers(){
        return userRepository.findAll();
    }
    
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User addUser(User user){
        return userRepository.save(user);
    }
}
