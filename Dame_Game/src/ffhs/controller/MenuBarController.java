package ffhs.controller;

import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;

/**
 * Controll for the Menu Bar
 *
 * */

public class MenuBarController {


    /**
     * Main Class Object
     * */
    private Main control;

    @FXML
    MenuItem menuItem_back;

    /**
     * Method to hand over objects.
     *
     * @param control Main Class Object.
     */
    public void setObjects(Main control) {
        this.control = control;
    }

    /**
     * Set the {@link MenuItem} "Back" as enabled/disabled.
     * At the start Scene the {@link MenuItem} should be disabled.
     *
     * @param disable Flag, if the {@link MenuItem} should be disabled or not.
     */
    public void disableReturnItem(boolean disable) {

        menuItem_back.setDisable(disable);
    }

    /**
     * handle the click to "Rules" {@link MenuItem} .
     */
    @FXML
    private void handleRules() {
        control.showRulesWindow();
    }
}
