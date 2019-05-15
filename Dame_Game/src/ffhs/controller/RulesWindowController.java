package ffhs.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Control for the Rules Window
 *
 * */

public class RulesWindowController {

    /**
     * The Rules Window
     *
     * */
    private Stage rulesStage;

    /**
     * TextArea to display the rules
     *
     * */
    @FXML
    private TextArea rulesTextArea;

    /**
     * Method, to hand over Objects.
     *
     * @param rules the rules Window
     */
    public void setObjects(Stage rules) {
        this.rulesStage = rules;
    }

    /**
     * Get the rules from the rules.txt to the textArea.
     * It will be called during the init.
     */
    @FXML
    private void initialize() {

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    RulesWindowController.class
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
     * Closes the Rules Window.
     * Will be called when the "OK" button is pressed.
     */
    @FXML
    private void close() {

        rulesStage.close();
    }
}
