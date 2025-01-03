// src/main/java/com/example/tamagotchi/model/Pet.java

package com.example.tamagotchi.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Entity
@Table(name="pets")
@Data
public class Pet {
    public enum PetSpecies {
        BEAR,
        CAT,
        DOG
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    private int hunger;
    private int happiness;
    private int energy;
    private int health;

    @Enumerated(EnumType.STRING)
    private PetSpecies species;

    @OneToOne
    @JoinColumn(name = "owner_id", unique = true)
    private User owner;
}