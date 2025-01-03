package com.example.tamagotchi;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("loading.fxml"));
        Scene scene = new Scene(loader.load());
        
        // Подключаем CSS, если нужно
        scene.getStylesheets().add(getClass().getResource("css/style.css").toExternalForm());

        stage.setScene(scene);
        stage.setTitle("Tamagotchi Loading");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
