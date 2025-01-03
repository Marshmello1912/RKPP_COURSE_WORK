module com.example.tamagotchi {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;


    opens com.example.tamagotchi to javafx.fxml;
    exports com.example.tamagotchi;
}