package com.example.tamagotchi.controller;

import com.example.tamagotchi.SceneManager;
import com.example.tamagotchi.service.ApiClient;
import com.example.tamagotchi.util.AlertUtil;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class RegisterController {
    @FXML
    private Pane rootPane;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;

    private ApiClient apiClient = new ApiClient();

    @FXML
    public void initialize() {
        rootPane.setStyle("-fx-background-image: url('com/example/tamagotchi/images/register_background.png');"
                + "-fx-background-size: cover;"
                + "-fx-background-position: center;");

    }

    @FXML
    public void handleRegister() {
        String username = usernameField.getText();
        String email = emailField.getText();
        String pass = passwordField.getText();
        String confirm = confirmPasswordField.getText();

        if(!pass.equals(confirm)) {
            AlertUtil.showError("Пароли не совпадают!");
            return;
        }

        try {
            apiClient.register(username, pass, email);
            AlertUtil.showInfo("Регистрация успешна! Теперь авторизуйтесь.");
            Stage stage = (Stage) rootPane.getScene().getWindow();
            SceneManager.getInstance().showLoginScreen(stage);
        } catch (Exception e) {
            AlertUtil.showError("Ошибка регистрации: " + e.getMessage());
        }
    }

    @FXML
    public void handleLogin() {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        SceneManager.getInstance().showLoginScreen(stage);
    }
}
