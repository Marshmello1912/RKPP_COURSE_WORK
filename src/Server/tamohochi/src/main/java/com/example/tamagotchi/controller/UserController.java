// src/main/java/com/example/tamagotchi/controller/UserController.java
package com.example.tamagotchi.controller;

import com.example.tamagotchi.dto.UserDTO;
import com.example.tamagotchi.model.User;
import com.example.tamagotchi.service.PetService;
import com.example.tamagotchi.service.UserService;
import com.example.tamagotchi.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private PetService petService;
    @Autowired
    private JWTUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDTO userDTO) {
        User user = userService.register(userDTO);
        return ResponseEntity.ok("Registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO) {
        User user = userService.authenticate(userDTO.getUsername(), userDTO.getPassword());
        String token = jwtUtil.generateToken(user);
        return ResponseEntity.ok(token);
    }
}
