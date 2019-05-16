package ffhs.controller;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ffhs.model.*;

import java.io.IOException;
import java.util.Optional;

/**
 * @author Baxevanos Theologos
 *
 * */

public class Main extends Application {

    private BorderPane menuBarPane;
    private MenuBarController menuBarController;

    private Parent startLayout;
    private StartGameMenuController startGameMenuController;

    private Stage primaryStage;

    private Stage rulesStage;
    private Stage aboutStage;

    private PlayerController playerController;

    public static PlayingField playingField;


    /**
     * Main Method starts the Application.
     *
     * @param args Arguments with which the program starts.
     */
    public static void main(String[] args) {

        launch(args);
    }

    /**
     * loads the main Stage and sets up the main window.
     * Initialize every game object
     *
     * @param primaryStage the main Window
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        this.primaryStage = primaryStage;

        primaryStage.setTitle("Dame Game");
        primaryStage.setOnCloseRequest(event -> Platform.exit());

        loadRootMenuLayout();
        loadStartLayout();
        setStartLayout();
        loadRulesWindow();
        loadAboutWindow();

        primaryStage.show();

    }

    /**
     * loads the Menu Bar (the MenuBar.fxml)
     * The MenuBar is on a BorderPane. At the top part, there are 2 options, File and Help..
     * The middle part is empty, so that we can add more items later on, if we want to.
     * Menu will always remain visible.
     */
    private void loadRootMenuLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("../view/MenuBar.fxml"));
            menuBarPane = loader.load();

            menuBarController = loader.getController();
            menuBarController.setObjects(this);

            primaryStage.setScene(new Scene(menuBarPane));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * loads the start window
     * At the start window, a player can choose the versus AI or the PVP mode
     *
     */
    private void loadStartLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("../view/startGameMenu.fxml"));
            startLayout = loader.load();

            startGameMenuController = loader.getController();
            startGameMenuController.setObjects(this);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * The start Scene will be placed at the "Center" ({@link BorderPane -> center}) of the Menu Scene.
     */
    private void setStartLayout() {
        menuBarPane.setCenter(startLayout);
        this.primaryStage.setResizable(true);
        menuBarController.disableReturnItem(true);
    }

    /**
     * Loads the Rules Window
     * The rules will be in a separate window.
     * the game window will be visible.
     * The Rules WIndow is a Utility-Window ({@link StageStyle}).
     */
    private void loadRulesWindow() {

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("../view/RulesWindow.fxml"));
            Parent rulesScene = loader.load();

            rulesStage = new Stage(StageStyle.UTILITY);
            rulesStage.setTitle("Game Rules");
            rulesStage.setResizable(false);
            rulesStage.setScene(new Scene(rulesScene));
            rulesStage.sizeToScene();

            ((RulesWindowController)loader.getController()).setObjects(rulesStage);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * open the Rules Window.
     *
     */
    public void showRulesWindow() {
        rulesStage.show();
    }

    /**
     * Loads the About Window
     * The About Scene will be displayed as a separate Window
     * The separate Window is a Utility-Window ({@link StageStyle}), the size can not be changed and
     * it remains at the front stage.
     */
    private void loadAboutWindow() {

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("../view/AboutWindow.fxml"));
            Parent aboutPane = loader.load();


            aboutStage = new Stage(StageStyle.UTILITY);
            aboutStage.setAlwaysOnTop(true);
            aboutStage.setTitle("About Dame");
            aboutStage.setResizable(false);
            aboutStage.setScene(new Scene(aboutPane));
            aboutStage.sizeToScene();

            ((AboutWindowController)loader.getController()).setObjects(aboutStage);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens the About Window
     *
     * */
    public void showAboutWindow() {

        aboutStage.show();
    }

    /**
     * startet ein Spiel.
     * Dazu wird das Spielfeld neu generiert und an den GamePaneController übergeben, der
     * dieses Spielfeld grafisch darstellt. Der PlayerController wird neu initialisiert und generiert die neuen Spieler.
     * Die neuen Spieler werden wieder an den gamePaneController übergeben, sodasss die Steine der Spieler garfisch dargestellt
     * werden können.
     *
     * @param ki gibt an, ob es sich um ein Single- (ki = true) oder Multiplayer-Spiel handelt
     * @param name1 Der Name von Spieler 1
     * @param name2 Der Name von Spieler 2
     * @see GameWindowController
     */
    public void startGame(boolean ki, String name1, String name2) {
        playingField.rebuild(startPaneController.getSize());
        gamePaneController.buildPlayingField(startPaneController.getSize(), (int)primaryStage.getHeight() - 200, playingField);
        playerController.init(ki, startPaneController.getSize(), name1, name2);
        gamePaneController.createTokens(playerController.getPlayer1(), playerController.getPlayer2());
        setGameLayout();
    }

    /**
     * Zeigt ein Fenster an sobald ein Spieler gewonnen hat.
     * Der Spieler hat die Möglichkeit das Spiel neuzustarten,
     * ins Hauptmenü zu wechseln, oder das Programm zu beenden.
     *
     * @param name Name des Spielers, der gewonnen hat.
     */
    public void winDialog(String name){
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.getDialogPane().getStylesheets().add(Main.class.getResource("view/style.css").toExternalForm());
        dialog.setHeaderText(name + " hat das Spiel gewonnen! Wähle nun, ob du eine neue Runde spielen,\nins Hauptmenü zurückkehren oder das Spiel beenden möchtest.");

        ButtonType restartButton = new ButtonType("Neustart");
        ButtonType menuButton = new ButtonType("Hauptmenü");
        ButtonType closeButton = new ButtonType("Beenden");

        dialog.getButtonTypes().setAll(restartButton, menuButton, closeButton);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == restartButton){
            startGame(playerController.isSinglePlayerGame(), playerController.getPlayer1().getName(), playerController.getPlayer2().getName());
        }
        else if (result.isPresent() && result.get() == menuButton) {
            returnToStart();
        }
        else {
            Platform.exit();
        }

    }

    /**
     * returns an Object from Class PlayerControllers.
     *
     * @return PlayerController
     */
    public PlayerController getPlayerController() {
        return playerController;
    }

    /**
     * return an Object from Class Game.
     *
     * @return Game
     */
    public Game getGame() {
        return game;
    }

    /**
     * returns the SuperDame Image.
     *
     * @return Image SuperDame
     */
    public Image getSuperDameImage() {
        return superDame;
    }
}
