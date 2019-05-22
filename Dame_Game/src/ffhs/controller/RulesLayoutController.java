package ffhs.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Controller for the Rules Window
 * */

public class RulesLayoutController {

    /**
     * The Rules Window (Stage)
     * */
    private Stage rulesStage;

    /**
     * TextArea to display the rules
     * */
    @FXML
    private TextArea rulesTextArea;

    /**
     * Closes the Rules Window.
     * Will be called when the "OK" button is pressed.
     */
    @FXML
    private void close() {
        rulesStage.close();
    }

    /**
     * Get the rules from the rules.txt to the textArea.
     * It will be auto called with the Stage's appearance.
     */
    @FXML
    private void initialize() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    RulesLayoutController.class
                            .getClassLoader()
                            .getResourceAsStream("resources/Rules.txt"), StandardCharsets.UTF_8));
            String line;
            while ((line = br.readLine()) != null) {
                rulesTextArea.appendText(line + "\n");
            }
            br.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method, to hand over Objects.
     * @param stage the rules Window (Stage)
     */
    public void setObjects(Stage stage) {
        this.rulesStage = stage;
    }
}
