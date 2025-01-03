package com.example.tamagotchi;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {
    private static SceneManager instance;

    private SceneManager() {
    }

    public static SceneManager getInstance() {
        if (instance == null) {
            instance = new SceneManager();
        }
        return instance;
    }

    public void showLoginScreen(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(getClass().getResource("css/style.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Tamagotchi Login");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showRegisterScreen(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("register.fxml"));
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(getClass().getResource("css/style.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Tamagotchi Registration");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showChoosePetScreen(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("choose_pet.fxml"));
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(getClass().getResource("css/style.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Choose your Pet");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showGameScreen(Stage stage) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("main-game.fxml"));
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(getClass().getResource("css/style.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Tamagotchi Game");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
