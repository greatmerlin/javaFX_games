package ffhs.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Control the About Window
 *
 * */

public class AboutWindowController {

    /**
     * The About Window
     * */
    private Stage aboutStage;

    /**
     * Label on the Scene
     * Information about the software
     *
     * */
    @FXML
    private Label label_info;

    /**
     * Method to hand over the objects
     * @param stage - the About Window
     *
     * */
    public void setObjects(Stage stage) {

        aboutStage = stage;
    }

    /**
     * set the label text
     * The developers/students and the institution
     * will be named
     * will be auto called by the init
     *
     * */
    @FXML
    private void initialize() {

        label_info.setText("This software was developed as part of a Project for the Fernfachhochschule Schweiz.\n\n" +
                "developer:\n\n" +
                "Baxevanos Theologos");
    }

    /**
     * closes the About Window
     *
     * */
    @FXML
    private void close() {

        aboutStage.close();
    }
}
