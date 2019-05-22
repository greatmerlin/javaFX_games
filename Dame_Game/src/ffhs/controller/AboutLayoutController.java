package ffhs.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Class: Control the About fxml
 * */

public class AboutLayoutController {

    /**
     * The About Window
     * */
    private Stage aboutStage;

    /**
     * Label on the Scene: Information about the software
     * */
    @FXML
    private Label labelAbout;

    /**
     * set the label text. The developers/students and the institution will be placed (auto called by init)
     * */
    @FXML
    private void initialize() {
        labelAbout.setText("This software was developed as part of a Project for the Fernfachhochschule Schweiz.\n\n" +
                "developer:\n\n" +
                "Baxevanos Theologos");
    }

    /**
     * closes the About Window
     * */
    @FXML
    private void close() {
        aboutStage.close();
    }

    /**
     * Method to hand over the objects
     * @param stage - the About Window(Stage)
     * */
    public void setObjects(Stage stage) {
        aboutStage = stage;
    }
}
