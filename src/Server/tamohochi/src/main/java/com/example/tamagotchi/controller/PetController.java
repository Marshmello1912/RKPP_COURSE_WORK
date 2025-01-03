// src/main/java/com/example/tamagotchi/controller/PetController.java
package com.example.tamagotchi.controller;

import com.example.tamagotchi.dto.PetDTO;
import com.example.tamagotchi.model.Pet;
import com.example.tamagotchi.model.User;
import com.example.tamagotchi.service.PetService;
import com.example.tamagotchi.service.UserService;
import com.example.tamagotchi.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pet")
public class PetController {

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


    @PostMapping("/choose")
    public ResponseEntity<?> choosePet(@RequestHeader("Authorization") String token,
                                       @RequestBody PetDTO petChoiceDTO) {
        User user = getUserFromToken(token);
        Pet.PetSpecies species;
        try {
            species = Pet.PetSpecies.valueOf(petChoiceDTO.getSpecies().toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid species");
        }

        try {
            Pet pet = petService.choosePet(user, species, petChoiceDTO.getName());
            return ResponseEntity.ok("Pet chosen successfully");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }


    @GetMapping("/status")
    public ResponseEntity<?> getPetStatus(@RequestHeader("Authorization") String token) {
        User user = getUserFromToken(token);
        Pet pet = petService.getPet(user);
        return ResponseEntity.ok(pet);
    }
}