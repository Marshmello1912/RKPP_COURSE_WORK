// src/main/java/com/example/tamagotchi/repository/PetRepository.java

package com.example.tamagotchi.repository;

import com.example.tamagotchi.model.Pet;
import com.example.tamagotchi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PetRepository extends JpaRepository<Pet, Long> {
    Optional<Pet> findByOwner(User owner);
}
