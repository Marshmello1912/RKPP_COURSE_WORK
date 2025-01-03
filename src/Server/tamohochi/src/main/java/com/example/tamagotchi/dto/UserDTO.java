// src/main/java/com/example/tamagotchi/dto/UserDTO.java
package com.example.tamagotchi.dto;

import lombok.Data;

@Data
public class UserDTO {
    private String username;
    private String password;
    private String email;
}
