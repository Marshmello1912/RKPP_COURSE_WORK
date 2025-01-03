package com.example.tamagotchi.controller;

import com.example.tamagotchi.SceneManager;
import com.example.tamagotchi.service.ApiClient;
import com.example.tamagotchi.service.TokenManager;
import com.example.tamagotchi.util.AlertUtil;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.Glow;
import javafx.scene.effect.Lighting;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class ChoosePetController {
    @FXML
    private Pane rootPane;

    @FXML
    private ImageView catImage;
    @FXML
    private ImageView dogImage;
    @FXML
    private ImageView bearImage;

    @FXML
    private TextField petNameField;

    private String selectedSpecies = "CAT"; // по умолчанию

    private ApiClient apiClient = new ApiClient();

    @FXML
    public void initialize() {
        rootPane.setStyle("-fx-background-image: url('com/example/tamagotchi/images/choose_pet.jpg');"
                + "-fx-background-size: cover;"
                + "-fx-background-position: center;");
        highlightSelectedPet(catImage, dogImage, bearImage);
        // Округление карртинок
        Circle clipCat = new Circle(catImage.getFitWidth()/2, catImage.getFitHeight()/2, catImage.getFitWidth()/2);
        catImage.setClip(clipCat);

        Circle clipDog = new Circle(dogImage.getFitWidth()/2, dogImage.getFitHeight()/2, dogImage.getFitWidth()/2);
        dogImage.setClip(clipDog);

        Circle clipBear = new Circle(bearImage.getFitWidth()/2, bearImage.getFitHeight()/2, bearImage.getFitWidth()/2);
        bearImage.setClip(clipBear);

        catImage.setOnMouseClicked(e -> {
            selectedSpecies = "CAT";
            highlightSelectedPet(catImage, dogImage, bearImage);
        });
        dogImage.setOnMouseClicked(e -> {
            selectedSpecies = "DOG";
            highlightSelectedPet(dogImage, catImage, bearImage);
        });
        bearImage.setOnMouseClicked(e -> {
            selectedSpecies = "BEAR";
            highlightSelectedPet(bearImage, catImage, dogImage);
        });
    }

    @FXML
    public void handleChoosePet() {
        String petName = petNameField.getText();
        if(petName.isEmpty()) {
            AlertUtil.showError("Введите имя питомца!");
            return;
        }
        try {
            String token = TokenManager.loadToken();
            apiClient.choosePet(token, selectedSpecies, petName);
            Stage stage = (Stage) rootPane.getScene().getWindow();
            SceneManager.getInstance().showGameScreen(stage);
        } catch (Exception e) {
            AlertUtil.showError("Ошибка: " + e.getMessage());
        }
    }

    private void highlightSelectedPet(ImageView chosen, ImageView... others) {
        // Для выбранного ImageView устанавливаем стиль
        BoxBlur glow = new BoxBlur(10, 10, 2);
        chosen.setEffect(null);

        // Для остальных удаляем этот стиль
        for (ImageView other : others) {
            other.setEffect(glow);
        }
    }
}
