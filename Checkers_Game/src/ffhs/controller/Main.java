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

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        this.primaryStage = primaryStage;

        primaryStage.setTitle("Checkers Game");
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

        primaryStage.setMinHeight(menuLayout.getPrefHeight());
        primaryStage.setMinWidth(menuLayout.getPrefWidth());

        primaryStage.show();
    }

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

    private void setStartLayout() {
        menuLayout.setCenter(startLayout);
        this.primaryStage.setResizable(true);
        menuPaneController.disableBackMenuItem(true);
    }

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

    public void showRulesWindow() {
        rulesStage.show();
    }

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

    public void showAboutWindow() {
        aboutStage.show();
    }

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

    private void setGameLayout() {
        menuLayout.setCenter(gameLayout);
        this.primaryStage.setResizable(false);
        menuPaneController.disableBackMenuItem(false);
    }

    public void startGame(boolean ai, String name1, String name2) {
        playingField.rebuild(startPaneController.getPlayingFieldSize());
        gamePaneController.buildPlayingField(startPaneController.getPlayingFieldSize(), (int)primaryStage.getHeight() - 200, playingField);
        playerController.init(ai, startPaneController.getPlayingFieldSize(), name1, name2);
        gamePaneController.createTokens(playerController.getPlayer1(), playerController.getPlayer2());
        setGameLayout();
    }

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

    public void returnToStart() {
        setStartLayout();
        gamePaneController.clearField();
        game.reset();
    }

    public PlayerController getPlayerController() {
        return playerController;
    }

    public Game getGame() {
        return game;
    }

    public Image getKingImage() {
        return king;
    }

    @Override
    public void init() {
        logo = new Image(Main.class.getClassLoader().getResourceAsStream("resources/king-logo.png"));
        king = new Image(Main.class.getClassLoader().getResourceAsStream("resources/king-logo.png"));
    }
}


