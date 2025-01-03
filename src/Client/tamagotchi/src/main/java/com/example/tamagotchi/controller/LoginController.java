package com.example.tamagotchi.controller;

import com.example.tamagotchi.SceneManager;
import com.example.tamagotchi.service.ApiClient;
import com.example.tamagotchi.service.TokenManager;
import com.example.tamagotchi.util.AlertUtil;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private Pane rootPane;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private ApiClient apiClient = new ApiClient();

    @FXML
    public void initialize() {
        rootPane.setStyle("-fx-background-image: url('com/example/tamagotchi/images/auth_background.png');"
                + "-fx-background-size: cover;"
                + "-fx-background-position: center;");
    }

    @FXML
    public void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        try {
            String token = apiClient.login(username, password);
            TokenManager.saveToken(token);
            // Проверяем, есть ли у пользователя уже питомец
            boolean hasPet = apiClient.hasPet(token);
            Stage stage = (Stage) rootPane.getScene().getWindow();
            if(!hasPet) {
                SceneManager.getInstance().showChoosePetScreen(stage);
            } else {
                SceneManager.getInstance().showGameScreen(stage);
            }
        } catch (Exception e) {
            AlertUtil.showError("Ошибка: " + e.getMessage());
        }
    }

    @FXML
    public void handleRegister() {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        SceneManager.getInstance().showRegisterScreen(stage);
    }
}
