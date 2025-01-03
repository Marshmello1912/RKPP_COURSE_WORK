// src/main/java/com/example/tamagotchi/TamohochiApplication.java
package com.example.tamagotchi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TamohochiApplication {
    public static void main(String[] args) {
        SpringApplication.run(TamohochiApplication.class, args);
    }
}
