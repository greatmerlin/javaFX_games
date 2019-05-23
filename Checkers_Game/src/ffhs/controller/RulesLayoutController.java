package ffhs.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class RulesLayoutController {

    private Stage rulesStage;
    @FXML
    private TextArea rulesTextArea;

    @FXML
    private void close() {
        rulesStage.close();
    }

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

    public void setObjects(Stage stage) {
        this.rulesStage = stage;
    }
}
