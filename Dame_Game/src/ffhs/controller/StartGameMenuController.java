package ffhs.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/**
 * Control for the start window
 *
 * */

public class StartGameMenuController {

    /**
     * Main Class Object
     */
    private Main control;

    /**
     * Method to hand over the objects.
     *
     * @param control Main Class object.
     */
    public void setObjects(Main control) {

        this.control = control;
    }

    /**
     * TextFeld to choose the Name of Player 1 .
     * Standard is "Player 1".
     */
    @FXML
    private TextField textField_Player1;

    /**
     * TextFeld to choose the Name of Player 2 .
     * Standard is "Player 2".
     */
    @FXML
    private TextField textField_Player2;

    /**
     * starts a Single Player Game.
     * As a second player will be an AI initialized.
     */
    @FXML
    private void handleVersusAI() {
        control.startGame(true, textField_Player1.getText(), textField_Player2.getText());
    }

    /**
     * starts a Multi Player Game.
     * As second player, a human player will be initialized.
     */
    @FXML
    private void handlePVP() {
        control.startGame(false, textField_Player1.getText(), textField_Player2.getText());
    }

    /**
     * PlayingField size = 10x10
     */
    public int getSize() {
        return 10;
    }
}
