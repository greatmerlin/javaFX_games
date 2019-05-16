package ffhs.controller;

import ffhs.model.Player;
import ffhs.model.Stone;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

/**
 * This class is the playing Field
 * It controls the user commands during a game.
 *
 * */

public class GameWindowController {

    /**
     * Main class object.
     */
    private Main control;

    /**
     * The size of the board
     */
    private int size = 10;

    /**
     * Amount of game pixels. How big is the game.
     */
    private int amount;

    /**
     * Checkers Radius {@link javafx.scene.shape.Circle}
     */
    private int tokenRadius;

    /**
     * Flag to lock users input
     * when a token is being moved, the users input are locked
     */
    private boolean graphicAction;

    /**
     * Pane, in which the Playing field and the tokens will be generated
     */
    @FXML
    private Pane playingField;

    /**
     * Pane to put the removed tokens from player 1
     *
     * @see #removeToken(stone)
     */
    @FXML
    private Pane pane_player1;

    /**
     * Pane to put the removed tokens from player 2
     *
     * @see #removeToken(stone)
     */
    @FXML
    private Pane pane_player2;

    /**
     * Label for players1 name
     * it will be aupdated at his turn
     *
     * @see #updatePlayer()
     */
    @FXML
    private Label label_player1;

    /**
     * Label for players2 name
     * it will be aupdated at his turn
     *
     * @see #updatePlayer()
     */
    @FXML
    private Label label_player2;

    /**
     * label for the announcements
     *
     * @see setstatus(String)
     */
    @FXML
    private Label label_status;

    /**
     * List with all Field rectangles, for easier color change
     */
    private ArrayList<Rectangle> field;

    /**
     * Method that hands over objects
     *
     * @param control Main Class Object/instance
     */
    public void setObjects(Main control) {
        this.control = control;
    }

    /**
     * creates the playing Field.
     * inits all the {@link Rectangle}
     *
     * @param amount the size of the playing Field (10x10)
     * @param size   the size of the playing Field in Pixel
     * @param pf     Object from {@link PlayingField} to read the Fields
     */

    public void buildPlayingField() {
        int amount = 10;
        this.size = size;
        this.tokenRadius = size / amount / 3;

        field = new ArrayList<>();
        clearField();
        playingField.setPrefSize(size, size);

        double a = (double) size / amount;

        for (int i = 0; i < amount; i++) {
            for (int j = 0; j < amount; j++) {
                Rectangle temp = new Rectangle();
                temp.setArcHeight(0);
                temp.setArcWidth(0);
                temp.setHeight(a);
                temp.setWidth(a);
                temp.setLayoutX(i * a);
                temp.setLayoutY(size - j * a - a);
                temp.setOnMouseClicked(this::onFieldClick);
                field.add(temp);
                playingField.getChildren().add(temp);
                pf.getField(i, j).setcRec(temp);
            }
        }
        colorField();
        setStatus("");
    }

    /**
     * Colors the playing Field at the standard color
     * */
    public void colorField() {
        int i = 0;
        for (Rectangle rec : field) {
            if (((i / amount) + (i % amount)) % 2 == 0) {
                rec.setFill(Color.BROWN);
            } else {
                rec.setFill(Color.WHEAT);
            }
            i++;
        }
    }

    /**
     * removes all the graphical objects from the screen
     */
    public void clearField() {
        pane_player1.getChildren().clear();
        pane_player2.getChildren().clear();
        playingField.getChildren().clear();
    }

    /**
     * places the checkers (tokens) from the player in the Field
     * All the {@link Circle} tokens will be initialized.
     *
     * @param p all players
     */
    public void createTokens(Player... p) {
        for (Player player : p) {
            for (Stone s : player.getStones()) {
                initToken(s.getIndexX(), s.getIndexY(), s.getcCirc(), s.getColor() == ffhs.model.Color.BLACK ? Color.BLACK : Color.WHITE);
            }
        }
        setNames(p[0].getName(), p[1].getName());
        updatePlayer();
    }

}


