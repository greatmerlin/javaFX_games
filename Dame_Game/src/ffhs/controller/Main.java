package ffhs.controller;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.io.IOException;

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
     * @param primaryStage Oberflächen Fenster
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        this.primaryStage = primaryStage;

        primaryStage.setTitle("Dame Game");
        primaryStage.setOnCloseRequest(event -> Platform.exit());

        loadRootMenuLayout();
        loadStartLayout();
        setStartLayout();

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
}
