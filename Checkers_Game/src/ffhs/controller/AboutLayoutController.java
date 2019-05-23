package ffhs.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class AboutLayoutController {

    private Stage aboutStage;

    @FXML
    private Label labelAbout;

    @FXML
    private void initialize() {
        labelAbout.setText("This software was developed as part of a Project for the Fernfachhochschule Schweiz.\n\n" +
                "developer:\n\n" +
                "Baxevanos Theologos");
    }

    @FXML
    private void close() {
        aboutStage.close();
    }

    public void setObjects(Stage stage) {
        aboutStage = stage;
    }
}
