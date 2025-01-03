// src/main/java/com/example/tamagotchi/controller/PetActionController.java
package com.example.tamagotchi.controller;

import com.example.tamagotchi.model.Pet;
import com.example.tamagotchi.model.User;
import com.example.tamagotchi.service.PetService;
import com.example.tamagotchi.service.UserService;
import com.example.tamagotchi.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер для действий с питомцем.
 * Все действия требуют токен пользователя.
 */
@RestController
@RequestMapping("/api/pet/action")
public class PetActionController {

    @Autowired
    private PetService petService;
    @Autowired
    private UserService userService;
    @Autowired
    private JWTUtil jwtUtil;

    private User getUserFromToken(String token) {
        String username = jwtUtil.extractUsername(token);
        return userService.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    @PostMapping("/play")
    public ResponseEntity<?> play(@RequestHeader("Authorization") String token) {
        User user = getUserFromToken(token);
        try {
            Pet pet = petService.playWithPet(user);
            return ResponseEntity.ok(pet);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/feed")
    public ResponseEntity<?> feed(@RequestHeader("Authorization") String token) {
        User user = getUserFromToken(token);
        try {
            Pet pet = petService.feedPet(user);
            return ResponseEntity.ok(pet);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/walk")
    public ResponseEntity<?> walk(@RequestHeader("Authorization") String token) {
        User user = getUserFromToken(token);
        try {
            Pet pet = petService.takeAWalk(user);
            return ResponseEntity.ok(pet);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/sleep")
    public ResponseEntity<?> sleep(@RequestHeader("Authorization") String token) {
        User user = getUserFromToken(token);
        try {
            Pet pet = petService.sleep(user);
            return ResponseEntity.ok(pet);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
