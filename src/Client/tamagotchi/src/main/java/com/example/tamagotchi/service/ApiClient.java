package com.example.tamagotchi.service;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiClient {
    private final String baseUrl = "http://localhost:8080/api";

    private ObjectMapper mapper = new ObjectMapper();

    public void register(String username, String password, String email) throws Exception {
        UserDTO userDTO = new UserDTO(username, password, email);
        String response = post("/users/register", userDTO, null);
        // Ожидаем "Registered successfully"
    }

    public String login(String username, String password) throws Exception {
        UserDTO userDTO = new UserDTO(username, password, null);
        String token = post("/users/login", userDTO, null);
        return token;
    }

    public boolean hasPet(String token) throws Exception {
        // Попытка получить статус питомца, если ошибка - значит нет питомца
        try {
            String resp = get("/pet/status", token);
            // Если получили ��твет без ошибки - питомец есть
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void choosePet(String token, String species, String name) throws Exception {
        PetChoiceDTO choice = new PetChoiceDTO(species, name);
        post("/pet/choose", choice, token);
    }

    public void feedPet(String token) throws Exception {
        // Logic to feed the pet
        String resp = post("/pet/action/feed", null, token);
    }

    public void playWithPet(String token) throws Exception {
        // Logic to play with the pet
       post("/pet/action/play", null,  token);
    }

    public void walkWithPet(String token) throws Exception {
        // Logic to play with the pet
        post("/pet/action/walk", null, token);
    }

    public void sleepWithPet(String token) throws Exception {
        // Logic to play with the pet
        post("/pet/action/sleep", null, token);
    }



    public String[] getPetStatus(String token) throws Exception {
        // Logic to get the pet's status
        PetDTO pet = mapper.readValue(get("/pet/status", token), PetDTO.class);
        return new String[]{
            "Pet: " + pet.name + "\n" +
            "Hunger: " + pet.hunger+ "\n" +
            "Happiness: " + pet.happiness+ "\n" +
            "Energy: " + pet.energy+ "\n" +
            "Health: " + pet.health,
            pet.species
        };
    }

    private String post(String endpoint, Object body, String token) throws Exception {
        URL url = new URL(baseUrl + endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        if(token != null) conn.setRequestProperty("Authorization", token);
        conn.setDoOutput(true);
        if (body != null) {
            mapper.writeValue(conn.getOutputStream(), body);
        }
        return readResponse(conn);
    }

    private String get(String endpoint, String token) throws Exception {
        URL url = new URL(baseUrl + endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        if(token != null) conn.setRequestProperty("Authorization", token);
        return readResponse(conn);
    }

    private String readResponse(HttpURLConnection conn) throws Exception {
        int code = conn.getResponseCode();
        InputStream is = (code == 200) ? conn.getInputStream() : conn.getErrorStream();
        if(is == null) {
            throw new Exception("Empty response");
        }
        byte[] data = is.readAllBytes();
        String text = new String(data);
        if(code != 200) {
            throw new Exception("Error: " + code + " " + text);
        }
        return text;
    }

    static class UserDTO {
        public String username;
        public String password;
        public String email;

        public UserDTO() {}
        public UserDTO(String username, String password, String email) {
            this.username = username;
            this.password = password;
            this.email = email;
        }
    }

    static class PetChoiceDTO {
        public String species;
        public String name;

        public PetChoiceDTO() {}
        public PetChoiceDTO(String species, String name) {
            this.species = species;
            this.name = name;
        }
    }

    static class PetDTO {
        public int id;
        public String species;
        public String name;
        public int hunger;
        public int happiness;
        public int energy;
        public int health;
        @JsonIgnore
        public UserDTO owner;
        public PetDTO() {}
        public PetDTO(int id,String species, String name, int hunger, int happiness, int energy, int health, UserDTO owner) {
            this.id = id;
            this.species = species;
            this.name = name;
            this.hunger = hunger;
            this.happiness = happiness;
            this.energy = energy;
            this.health = health;
            this.owner = owner;
        }
    }
}
