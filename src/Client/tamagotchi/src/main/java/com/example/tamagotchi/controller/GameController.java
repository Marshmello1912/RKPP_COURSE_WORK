package com.example.tamagotchi.controller;

import com.example.tamagotchi.SceneManager;
import com.example.tamagotchi.service.TokenManager;
import com.example.tamagotchi.util.AlertUtil;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import com.example.tamagotchi.service.ApiClient;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameController {

    @FXML
    private Pane rootPane; // Main pane for the game window

    @FXML
    private Label petStatusLabel; // Label to display pet's status

    @FXML
    private ImageView petImage;
    Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(30), event -> updatePetStatus()));

    private ApiClient apiClient = new ApiClient(); // Service to interact with backend

    @FXML
    public void handleGame(){
        try{
            String token = TokenManager.loadToken();
            String[] status = apiClient.getPetStatus(token);
            petStatusLabel.setText(status[0]);
        } catch (Exception e){
            AlertUtil.showError("Ошибка: " + e.getMessage());
        }
    }

    @FXML
    public void initialize() {
        rootPane.setStyle("-fx-background-image: url('com/example/tamagotchi/images/choose_pet.jpg');"
                + "-fx-background-size: cover;"
                + "-fx-background-position: center;");
        updatePetStatus();
        startAutoUpdate();
    }

    @FXML
    private void handleFeedButtonAction() {
        try {
            String token = TokenManager.loadToken();
            apiClient.feedPet(token);
        } catch (Exception e) {
            if (e.getMessage().contains("400")) {
                AlertUtil.showError("Недостаточно энергии!");
            }
        }
        finally {
            updatePetStatus();
        }
    }
    @FXML
    private void handlePlayButtonAction() {
        // Logic to play with the pet
        try {
            String token = TokenManager.loadToken();
            apiClient.playWithPet(token);
        } catch (Exception e) {
            if (e.getMessage().contains("400")) {
                AlertUtil.showError("Недостаточно энергии!");
            }
        }
        finally {
            updatePetStatus();
        }
    }
    @FXML
    private void handleWalkButtonAction() {
        // Logic to play with the pet
        try {
            String token = TokenManager.loadToken();
            apiClient.walkWithPet(token);
        } catch (Exception e) {
            if (e.getMessage().contains("400")) {
                AlertUtil.showError("Недостаточно энергии!");
            }
        }
        finally {
            updatePetStatus();
        }
    }


    @FXML
    private void handleSleepButtonAction() {
        // Logic to play with the pet
        try {
            String token = TokenManager.loadToken();
            apiClient.sleepWithPet(token);
        } catch (Exception e) {
            if (e.getMessage().contains("400")) {
                AlertUtil.showError("Недостаточно энергии!");
            }
        }
        finally {
            updatePetStatus();
        }
    }

    private void updatePetStatus() {
        // Fetch the pet's status from the backend and update the label
        try {

            String token = TokenManager.loadToken();
            if (apiClient.hasPet(token)) {
                String[] status = apiClient.getPetStatus(token);
                petStatusLabel.setText(status[0]);
                petImage.setImage(new Image("./com/example/tamagotchi/images/" + status[1] + "_full.png"));

            } else {
                AlertUtil.showError("Ваш питомец помер :(");
                Stage stage = (Stage) rootPane.getScene().getWindow();
                SceneManager.getInstance().showChoosePetScreen(stage);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void startAutoUpdate() {
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
}
