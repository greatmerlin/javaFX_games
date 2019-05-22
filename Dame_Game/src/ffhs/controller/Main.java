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
 * */

public class Main extends Application {

    private Stage primaryStage;
    private Parent startLayout;
    private Parent gameLayout;
    private BorderPane menuLayout;
    private Stage rulesStage;
    private Stage aboutStage;

    public static PlayingField playingField;

    private GameLayoutController gamePaneController;
    private StartScreenController startPaneController;
    private MenuBarController menuPaneController;
    private PlayerController playerController;
    private Game game;

    private Image king;
    private Image logo;

    /**
     * Main Method starts the Application.
     * @param args Arguments with which the program starts.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * loads the main Stage and sets up the main window.
     * Initialize every game object
     * @param primaryStage the main Window
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        this.primaryStage = primaryStage;

        primaryStage.setTitle("Dame Game");
        primaryStage.getIcons().add(logo);
        primaryStage.setOnCloseRequest(event -> Platform.exit());

        loadRootMenuLayout();
        loadStartLayout();
        loadGameLayout();
        setStartLayout();
        loadRulesWindow();
        loadAboutWindow();

        playingField = new PlayingField();
        playerController = new PlayerController();
        game = new Game(this, gamePaneController, playerController);

//        primaryStage.setMaximized(true);  //TODO : decide if you want it full screen or not.
        primaryStage.setMinHeight(menuLayout.getPrefHeight());
        primaryStage.setMinWidth(menuLayout.getPrefWidth());

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
            menuLayout = loader.load();

            menuPaneController = loader.getController();
            menuPaneController.setObjects(this);

            primaryStage.setScene(new Scene(menuLayout));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * loads the start window
     * At the start window, a player can choose the versus AI or the PVP mode
     */
    private void loadStartLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("../view/startScreen.fxml"));
            startLayout = loader.load();

            startPaneController = loader.getController();
            startPaneController.setObjects(this);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * The start Scene will be placed at the "Center" ({@link BorderPane -> center}) of the Menu Scene.
     */
    private void setStartLayout() {
        menuLayout.setCenter(startLayout);
        this.primaryStage.setResizable(true);
        menuPaneController.disableBackMenuItem(true);
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
            loader.setLocation(Main.class.getResource("../view/RulesLayout.fxml"));
            Parent rulesScene = loader.load();

            rulesStage = new Stage(StageStyle.UTILITY);
            rulesStage.setTitle("Game Rules");
            rulesStage.setResizable(false);
            rulesStage.setScene(new Scene(rulesScene));
            rulesStage.sizeToScene();

            ((RulesLayoutController)loader.getController()).setObjects(rulesStage);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * open the Rules Window.
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
            loader.setLocation(Main.class.getResource("../view/AboutLayout.fxml"));
            Parent aboutPane = loader.load();

            aboutStage = new Stage(StageStyle.UTILITY);
            aboutStage.setAlwaysOnTop(true);
            aboutStage.setTitle("About Dame");
            aboutStage.setResizable(false);
            aboutStage.setScene(new Scene(aboutPane));
            aboutStage.sizeToScene();

            ((AboutLayoutController)loader.getController()).setObjects(aboutStage);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens the About Window
     * */
    public void showAboutWindow() {
        aboutStage.show();
    }

    /**
     * loads the Game Scene
     * In this Scene, the PlayingField will be shown
     */
    private void loadGameLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("../view/GameLayout.fxml"));
            gameLayout = loader.load();
            gamePaneController = loader.getController();
            gamePaneController.setObjects(this);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * set the game Layout.
     * the game layout will be at the ({@link BorderPane# center}) center.
     */
    private void setGameLayout() {
        menuLayout.setCenter(gameLayout);
        this.primaryStage.setResizable(false);
        menuPaneController.disableBackMenuItem(false);
    }

    /**
     * starts a Game
     * A new PLayingField will be generated and given to the Controller.
     * The Controller will give it "flesh and bones". The PlayerController will initialize and generate the 2 players.
     * The new players will be given again to the game controller to get their tokens.
     * @param ai returns if the game is single- (ai = true) or Multiplayer
     * @param name1 The Name from Player 1
     * @param name2 The Name from Player 2
     * @see GameLayoutController
     */
    public void startGame(boolean ai, String name1, String name2) {
        playingField.rebuild(startPaneController.getPlayingFieldSize());
        gamePaneController.buildPlayingField(startPaneController.getPlayingFieldSize(), (int)primaryStage.getHeight() - 200, playingField);
        playerController.init(ai, startPaneController.getPlayingFieldSize(), name1, name2);
        gamePaneController.createTokens(playerController.getPlayer1(), playerController.getPlayer2());
        setGameLayout();
    }

    /**
     * Shows a window when a player won.
     * The player will have a chance to start the game again,
     * to go to the main menu or to end the game.
     * @param name Name of the player that won the game.
     */
    public void winDialog(String name){
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.getDialogPane().getStylesheets().add(Main.class.getResource("../view/styles.css").toExternalForm());
        dialog.setHeaderText(name + " has won the Game! Now choose if you want to start the game again,\ngo back to the main Menu or close the game.");

        ButtonType restartButton = new ButtonType("restart");
        ButtonType menuButton = new ButtonType("mainMenu");
        ButtonType closeButton = new ButtonType("Close");

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
     * breaks the current game and returns to the main menu.
     */
    public void returnToStart() {
        setStartLayout();
        gamePaneController.clearField();
        game.reset();
    }

    /**
     * returns an Object from the Class PlayerControllers.
     * @return PlayerController
     */
    public PlayerController getPlayerController() {
        return playerController;
    }

    /**
     * return an Object from the Class Game.
     * @return Game
     */
    public Game getGame() {
        return game;
    }

    /**
     * returns the King Image.
     * @return Image King
     */
    public Image getKingImage() {
        return king;
    }

    /**
     * This will called from the start (loads all the needed Logos).
     */
    @Override
    public void init() {
        logo = new Image(Main.class.getClassLoader().getResourceAsStream("resources/queen.png"));
        king = new Image(Main.class.getClassLoader().getResourceAsStream("resources/king-logo.png"));
    }
}


