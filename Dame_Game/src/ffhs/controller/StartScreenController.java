package ffhs.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

/**
 * Control for the start window
 * */

public class StartScreenController {

    /**
     * String for a 8 * 8 Field
     */
    private final String eight = "8 * 8";

    /**
     * String for a 10 * 10 Field
     */
    private final String ten = "10 * 10";

    /**
     * Main Class Object
     */
    private Main mainClassObject;

    /**
     * ComboBox, to choose the size of the Field (8x8 or 10x10).
     */
    @FXML
    private ComboBox<String> comboBoxPlayingFieldSize;

    /**
     * TextField to choose the Name of Player 1.
     * Default is "Player 1".
     */
    @FXML
    private TextField textFieldPlayer1;

    /**
     * TextField to choose the Name of Player 2 .
     * Default is "Player 2".
     */
    @FXML
    private TextField textFieldPlayer2;

    /**
     * init the ComboBox.
     * auto called with the Scene appearance (initialize Method)
     */
    @FXML
    private void initialize() {
        comboBoxPlayingFieldSize.getItems().add(eight);
        comboBoxPlayingFieldSize.getItems().add(ten);
        comboBoxPlayingFieldSize.setValue(eight);
    }

    /**
     * starts a Single Player Game.
     * As a second player will be an AI opponent initialized.
     */
    @FXML
    private void handleSinglePlayer() {
        mainClassObject.startGame(true, textFieldPlayer1.getText(), textFieldPlayer2.getText());
    }

    /**
     * starts a Multi Player Game.
     * As second player, a human player will be initialized.
     */
    @FXML
    private void handleMultiplayer() {
        mainClassObject.startGame(false, textFieldPlayer1.getText(), textFieldPlayer2.getText());
    }

    /**
     * Method to hand over the objects.
     * @param mainClassObject Instance of the Main Class.
     */
    public void setObjects(Main mainClassObject) {
        this.mainClassObject = mainClassObject;
    }

    /**
     * get the size of the PlayingField.
     */
    public int getPlayingFieldSize() {
        return comboBoxPlayingFieldSize.getValue().equals(eight) ? 8 : 10;
    }
}
