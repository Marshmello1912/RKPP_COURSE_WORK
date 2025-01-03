// src/main/java/com/example/tamagotchi/service/UserService.java
package com.example.tamagotchi.service;

import com.example.tamagotchi.dto.UserDTO;
import com.example.tamagotchi.model.User;
import com.example.tamagotchi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User register(UserDTO userDTO) {
        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setEmail(userDTO.getEmail());
        return userRepository.save(user);
    }

    public User authenticate(String username, String rawPassword) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if(userOpt.isPresent()) {
            User user = userOpt.get();
            if(passwordEncoder.matches(rawPassword, user.getPassword())) {
                return user;
            }
        }
        throw new IllegalArgumentException("Invalid credentials");
    }

    public Optional<User> findByUsername(String username){
        return userRepository.findByUsername(username);
    }
}
