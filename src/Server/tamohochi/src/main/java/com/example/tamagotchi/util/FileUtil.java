// src/main/java/com/example/tamagotchi/util/FileUtil.java
package com.example.tamagotchi.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileUtil {
    private static final String FILE_PATH = "lastUpdated.txt";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public static LocalDateTime readLastUpdated() {
        try {
            String content = new String(Files.readAllBytes(Paths.get(FILE_PATH)));
            if (content.isEmpty()) {
                return null; // File is empty
            }
            return LocalDateTime.parse(content, FORMATTER);
        } catch (IOException e) {
            return null; // File not found or other IO error
        }
    }

    public static void writeLastUpdated(LocalDateTime lastUpdated) {
        try {
            Files.write(Paths.get(FILE_PATH), lastUpdated.format(FORMATTER).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
