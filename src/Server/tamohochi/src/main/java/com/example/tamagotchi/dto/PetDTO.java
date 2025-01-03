// src/main/java/com/example/tamagotchi/dto/PetDTO.java
package com.example.tamagotchi.dto;

import lombok.Data;

@Data
public class PetDTO {
    private String species; // "BEAR", "CAT", "DOG"
    private String name;
    private int hunger;
    private int happiness;
    private int energy;
    private int health;
}