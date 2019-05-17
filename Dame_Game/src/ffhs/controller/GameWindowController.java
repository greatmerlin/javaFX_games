package ffhs.controller;

import ffhs.model.Player;
import ffhs.model.Stone;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import ffhs.model.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
     * @see # removeToken(stone)
     */
    @FXML
    private Pane pane_player1;

    /**
     * Pane to put the removed tokens from player 2
     *
     * @see  # removeToken(stone)
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
     * @see  # setstatus(String)
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
//     * @param amount the size of the playing Field (10x10)
//     * @param size   the size of the playing Field in Pixel
     * @param pf Object from {@link PlayingField} to read the Fields
     */

    public void buildPlayingField(int size, PlayingField pf) {
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

    /**
     * set the names of both players.
     *
     * @param name1 Name Player1
     * @param name2 Name Player2
     */
    private void setNames(String name1, String name2) {
        if (name1 != null && !name1.isEmpty()) {
            label_player1.setText(name1);
        }
        if (name2 != null && !name2.isEmpty()) {
            label_player2.setText(name2);
        }
    }

    /**
     * färbt die Spieler Namen richtig ein.
     * Der Spieler der am Zug ist, bekommt einen orangenen Namen.
     */
    public void updatePlayer() {
        if (control.getPlayerController().isCurrentPlayer1()) {
            label_player1.setId("nameOnTurn");
            label_player2.setId("name");
        }
        else {
            label_player1.setId("name");
            label_player2.setId("nameOnTurn");
        }
    }

    /**
     * Warning on top of the screen.
     * use {@code ""} to not show something.
     *
     * @param message message, which will be displayed.
     */
    private void setStatus(String message) {
        label_status.setText(message);
    }

    /**
     * removes a token from the field
     * The removed token will be broken into 2 pieces
     *
     * @param stone Stein that will be moved
     */
    private void removeToken(Stone stone) {
        playingField.getChildren().remove(stone.getcCirc());
        if (stone.getColor() == ffhs.model.Color.BLACK) {
            pane_player1.getChildren().add(stone.getcCirc());
            double off = 0;
            if (stone.getcCirc() instanceof StackPane) {
                off = tokenRadius;
            }
            stone.getcCirc().setLayoutX(pane_player1.getWidth() / 2 - off);
            stone.getcCirc().setLayoutY(pane_player1.getChildren().size() * tokenRadius - off);
        }
        else {
            pane_player2.getChildren().add(stone.getcCirc());
            double off = 0;
            if (stone.getcCirc() instanceof StackPane) {
                off = tokenRadius;
            }
            stone.getcCirc().setLayoutX(pane_player2.getWidth() / 2 - off);
            stone.getcCirc().setLayoutY(pane_player2.getChildren().size() * tokenRadius - off);
        }
    }

    /**
     * moves a token across the field.
     * skip-fields will be removed
     * during the token movements, the players are locked {@link #graphicAction}
     *
     * @param move Move, that the token does
     */
    public void moveToken(Move move) {
        graphicAction = true;
        double value = (double)size / amount;
        updateToken(move.getStone().getcCirc());
        Stone s = move.getStone();
        Timer t = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    try {
                        if (!isStoneNearField(s, move.getNextField(), value)) {
                            calculateTokenLocation(s.getcCirc(), value, move);
                            if (move.getFirstSkipedField() != null && isStoneNearField(s, move.getFirstSkipedField(), value)) {
                                Stone stone = control.getPlayerController().getOtherPlayer().getStoneAt(move.getFirstSkipedField().getIndexX(), move.getFirstSkipedField().getIndexY());
                                if (stone != null) {
                                    stone.setEliminated();
                                    removeToken(stone);
                                    move.nextSkipedField();
                                }
                            }
                        }
                        else {
                            if (!move.nextField()) {
                                placeToken(move.getEndField().getIndexX(), move.getEndField().getIndexY(), s.getcCirc());
                                t.cancel();
                                t.purge();
                                if (s.isSuperDame()) {
                                    visualizeSuperDame(s);
                                }
                                control.getGame().finishedMove();
                                graphicAction = false;
                            }
                        }
                    }
                    catch (Exception e) {
                        System.err.println("Something went wrong: " + e.getClass().getName());
                        e.printStackTrace();
                    }
                });
            }
        };
        t.schedule(task, 0, 40);
    }

    /**
     * set the Node to a closer value near the direction.
     * The node will will be every time (1/12) from a  a Field replaced.
     *
     * @param n Objekt, which should be moved
     * @param value size from a field
     * @param move Der actuall move to lock the destination.
     */
    private void calculateTokenLocation(Node n, double value, Move move) {
        n.setLayoutX(n.getLayoutX() + (value / 12)
                * (move.getNextField().getIndexX() >= move.getCurrentField().getIndexX() ? 1 : -1));
        n.setLayoutY(n.getLayoutY() + (value / 12)
                * (move.getNextField().getIndexY() >= move.getCurrentField().getIndexY() ? -1 : 1));
    }

    /**
     * test if a token is there graphical.
     * An offset of +/- 2% will be taken into consideration.
     *
     * @param s token will be tested
     * @param f Field that will be tested if the token is on it.
     * @param value distance that the token moved
     * @return true, if the token is on the field.
     */
    private boolean isStoneNearField(Stone s, Field f, double value) {
        double off = 0;
        if (s.getcCirc() instanceof StackPane) {
//            off = ((StackPane) s.getcCirc()).getWidth() / 2;
            off = tokenRadius;
        }
        return (f.getIndexX() + 0.48) * value <= s.getcCirc().getLayoutX() + off && (f.getIndexX() + 0.52) * value >= s.getcCirc().getLayoutX() + off &&
                size - (f.getIndexY() + 0.52) * value <= s.getcCirc().getLayoutY() + off && size - (f.getIndexY() + 0.48) * value >= s.getcCirc().getLayoutY() + off;
    }

    /**
     * has all visited fields and the possible fields to be moved into.
     *
     * @param fields possible fields.
     * @param move Move (visited fields).
     */
    public void highlightFields(List<Field> fields, Move move) {
        colorField();

        for (Field f : move.getEnteredFields()) {
            f.getcRec().setFill(Color.BLUE);
        }
        for (Field f : fields) {
            f.getcRec().setFill(Color.DARKGREEN);
        }
    }

    /**
     * initialize the {@link Circle} from a token and set it to the tight position in the field.
     *
     * @param x X-Position
     * @param y Y-Position
     * @param c init circle
     * @param color color of the token
     */
    private void initToken(int x, int y, Node c, Color color) {
        if (c instanceof Circle) {
            ((Circle)c).setRadius(tokenRadius);
            ((Circle)c).setFill(color);
            ((Circle)c).setStroke(Color.GRAY);
            ((Circle)c).setStrokeWidth(1);
            placeToken(x, y, c);
            c.setOnMouseClicked(this::onFieldClick);
            playingField.getChildren().add(c);
        }
        else {
            System.err.println("Something went wrong");
        }
    }

    /**
     * set the x- and the y-coordinates from a Node, and check the king/queen(superDame).
     * Normal tokens are round and have a  ZeroSpot in der Middle ({@link Circle}).
     * A King has a crown. Has a zeroPoint up and left.
     *
     * @param x Index x from the field that the token should be
     * @param y Index y from the field that the token should be
     * @param node Node that will be placed.
     */
    private void placeToken(int x, int y, Node node) {
        double a = (double)size / amount;
        double off = 0;
        if (node instanceof StackPane) {
            off = tokenRadius;
        }
        node.setLayoutX((x + 0.5) * a - off);
        node.setLayoutY(size - (y + 0.5) * a - off);
    }

    /**
     * updatet a token, so that it will be all over up.
     * the node will be for a short time removed from the field and then added again (movement animation).
     * <b>WICHTIG:</b> für {@link #moveToken(Move)}
     *
     * @param c circle
     */
    private void updateToken(Node c) {
        playingField.getChildren().remove(c);
        playingField.getChildren().add(c);
    }

    /**
     * controls a normal token in a king-mode
     * a stackPane will be made, which will help us with {@link Circle} and {@link javafx.scene.image.Image}
     * The Image has 75% from the Circles size.
     *
     * @param s SuerDame(King) token.
     */
    private void visualizeSuperDame(Stone s) {
        if (s.isSuperDame() && s.getcCirc() instanceof Circle) {
            Circle c = (Circle)s.getcCirc();
            c.setOnMouseClicked(null);
            StackPane sp = new StackPane();
            sp.setPrefSize(Control.USE_COMPUTED_SIZE, Control.USE_COMPUTED_SIZE);
            sp.getChildren().add(c);

            ImageView iw = new ImageView(control.getSuperDameImage());
            iw.setFitHeight(c.getRadius() * 1.5);
            iw.setFitWidth(c.getRadius() * 1.5);

            sp.getChildren().add(iw);
            sp.setLayoutX(c.getLayoutX() - c.getRadius());
            sp.setLayoutY(c.getLayoutY() - c.getRadius());
            sp.setOnMouseClicked(this::onFieldClick);
            playingField.getChildren().remove(c);
            playingField.getChildren().add(sp);
            s.changeNode(sp);
        }
    }

    /**
     * processes with mouse clicks on the spielfield.
     * The things that can be clicked at a{@link Rectangle} from a {@link Field},
     * a {@link Circle} from a {@link Stone} or a {@link StackPane} from a King.
     *
     * @param e MouseEvent
     * @see Game#selectStone(Stone)
     * @see Game#selectField(Field)
     */
    @FXML
    private void onFieldClick(MouseEvent e) {
        if (!graphicAction && (!control.getPlayerController().isSinglePlayerGame() || control.getPlayerController().isCurrentPlayer1())) {
            if (e.getSource() instanceof Rectangle || e.getSource() instanceof Circle || e.getSource() instanceof StackPane) {
                setStatus("");
                if (e.getSource() instanceof Rectangle) {
                    Rectangle temp = (Rectangle) e.getSource();
                    int index = field.indexOf(temp);
                    int x = (index / amount);
                    int y = index % amount;
                    Field pressedField = Main.playingField.getField(x, y);
                    control.getGame().selectField(pressedField);
                }
                else {
                    Stone s = control.getPlayerController().getCurrentPlayer().getStoneOfClickedCircle((Node)e.getSource());
                    if (s == null) {
                        setStatus("This is NOT your token");
                        return;
                    }
                    else if (s.isEliminated()) {
                        setStatus("This token was already eliminated");
                        return;
                    }
                    control.getGame().selectStone(s);
                }
            }
        }
    }

}


