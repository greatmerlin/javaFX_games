package ffhs.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class StartScreenController {

    private final String eight = "8 * 8";
    private final String ten = "10 * 10";
    private Main mainClassObject;
    @FXML
    private ComboBox<String> comboBoxPlayingFieldSize;
    @FXML
    private TextField textFieldPlayer1;
    @FXML
    private TextField textFieldPlayer2;

    @FXML
    private void initialize() {
        comboBoxPlayingFieldSize.getItems().add(eight);
        comboBoxPlayingFieldSize.getItems().add(ten);
        comboBoxPlayingFieldSize.setValue(eight);
    }

    @FXML
    private void handleSinglePlayer() {
        mainClassObject.startGame(true, textFieldPlayer1.getText(), textFieldPlayer2.getText());
    }

    @FXML
    private void handleMultiplayer() {
        mainClassObject.startGame(false, textFieldPlayer1.getText(), textFieldPlayer2.getText());
    }

    public void setObjects(Main mainClassObject) {
        this.mainClassObject = mainClassObject;
    }

    public int getPlayingFieldSize() {
        return comboBoxPlayingFieldSize.getValue().equals(eight) ? 8 : 10;
    }
}
