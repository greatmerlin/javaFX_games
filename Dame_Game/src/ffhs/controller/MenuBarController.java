package ffhs.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;

/**
 * Controller for the Menu Bar
 * */

public class MenuBarController {

    /**
     * Main Class Object
     * */
    private Main mainClassObject;

    @FXML
    private MenuItem menuItemBack;

    /**
     * handle the click to "Rules" {@link MenuItem}.
     */
    @FXML
    private void handleRules() {
        mainClassObject.showRulesWindow();
    }

    /**
     * handle the click to "About" {@link MenuItem}.
     * */
    @FXML
    private void handleAbout() {
        mainClassObject.showAboutWindow();
    }

    /**
     * handle pressing the "exit". Safe exit from the Game.
     * */
    @FXML
    private void handleExit(ActionEvent event) {
        Platform.exit();
    }

    /**
     * Method to hand over objects.
     *
     * @param mainClassObject Instance from the Class Main
     */
    public void setObjects(Main mainClassObject) {
        this.mainClassObject = mainClassObject;
    }

    /**
     * Set the {@link MenuItem} "Back" as enabled/disabled.
     * At the start Scene the {@link MenuItem} should be disabled.
     * @param disable Flag, if the {@link MenuItem} should be disabled or not.
     */
    public void disableBackMenuItem(boolean disable) {
        menuItemBack.setDisable(disable);
    }

    /**
     * By pressing the {@link MenuItem} "Return", returns the user to the main menu.
     * */
    public void handleBack(){
        mainClassObject.returnToStart();
    }


}
