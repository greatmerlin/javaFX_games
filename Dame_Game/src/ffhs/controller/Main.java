package ffhs.controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author Baxevanos Theologos
 *
 * */

public class Main extends Application {

    public static void main(String[] args) {

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("../view/startGameMenu.fxml"));

        Scene mainMenuScene = new Scene(root, 800,600);

        primaryStage.setTitle("Dame Game");
        primaryStage.setScene(mainMenuScene);
        primaryStage.show();
    }
}
