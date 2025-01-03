package com.example.tamagotchi.controller;

import com.example.tamagotchi.SceneManager;
import com.example.tamagotchi.service.ApiClient;
import com.example.tamagotchi.service.TokenManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LoadingController implements Initializable {
    @FXML
    private Pane rootPane;

    private ApiClient apiClient = new ApiClient();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Установим фоновое изображение для rootPane через CSS или в коде.
        // Например, в CSS для rootPane можно прописать:
        // -fx-background-image: url("/images/loading_background.png");
        //
        // Также, после короткой задержки можно переключиться на экран авторизации.

        // Пример: перейти на экран авторизации через 2 секунды
        rootPane.setStyle("-fx-background-image: url('com/example/tamagotchi/images/loading_background.jpg');"
                + "-fx-background-size: cover;"
                + "-fx-background-position: center;");

        // Используем таймер для перехода (или Platform.runLater с задержкой)
        new Thread(() -> {
            try {
                Thread.sleep(2000);
                String token = TokenManager.loadToken();
                if (apiClient.hasPet(token)) {
                    javafx.application.Platform.runLater(() -> {
                        SceneManager.getInstance().showGameScreen((Stage)rootPane.getScene().getWindow());
                    });
                } else {
                    javafx.application.Platform.runLater(() -> {
                        SceneManager.getInstance().showLoginScreen((Stage)rootPane.getScene().getWindow());
                    });
                }


            } catch (Exception e) { javafx.application.Platform.runLater(() -> {
                SceneManager.getInstance().showLoginScreen((Stage)rootPane.getScene().getWindow());
            });}
        }).start();
    }
}
