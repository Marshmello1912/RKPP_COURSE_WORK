package com.example.tamagotchi.service;

import java.nio.file.Files;
import java.nio.file.Path;

public class TokenManager {

    private static final Path TOKEN_FILE = Path.of(System.getProperty("user.home"), ".tamagotchi_token");

    public static void saveToken(String token) throws Exception {
        Files.writeString(TOKEN_FILE, token);
    }

    public static String loadToken() throws Exception {
        if(!Files.exists(TOKEN_FILE)) {
            throw new Exception("No token stored");
        }
        return Files.readString(TOKEN_FILE).trim();
    }
}
