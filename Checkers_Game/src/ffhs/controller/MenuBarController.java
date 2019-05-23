package ffhs.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;

public class MenuBarController {

    private Main mainClassObject;
    @FXML
    private MenuItem menuItemBack;

    @FXML
    private void handleRules() {
        mainClassObject.showRulesWindow();
    }
    @FXML
    private void handleAbout() {
        mainClassObject.showAboutWindow();
    }
    @FXML
    private void handleExit(ActionEvent event) {
        Platform.exit();
    }

    public void setObjects(Main mainClassObject) {
        this.mainClassObject = mainClassObject;
    }

    public void disableBackMenuItem(boolean disable) {
        menuItemBack.setDisable(disable);
    }

    public void handleBack(){
        mainClassObject.returnToStart();
    }
}
