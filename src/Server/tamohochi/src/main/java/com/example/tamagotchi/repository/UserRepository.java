// src/main/java/com/example/tamagotchi/repository/UserRepository.java
package com.example.tamagotchi.repository;

import com.example.tamagotchi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}