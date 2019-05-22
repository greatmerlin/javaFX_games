package ffhs.controller;

import ffhs.model.Player;
import ffhs.model.Token;
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
 * This class is the Controller for the game Layout
 * It controls the user commands during a game.
 * */

public class GameLayoutController {

    /**
     * instance of the main Class
     */
    private Main mainClassObject;

    /**
     * The size of the board
     */
    private int size;

    /**
     * Amount of game pixels. How big is the game.
     */
    private int amount;

    /**
     * token Radius {@link javafx.scene.shape.Circle}
     */
    private int tokenRadius;

    /**
     * Flag(boolean): when a token is being moved, the users actions are locked
     */
    private boolean graphicAction;

    /**
     * List with all Field rectangles, for easier color change
     */
    private ArrayList<Rectangle> field;

    /**
     * Pane, in which the Playing field and the tokens will be generated
     */
    @FXML
    private Pane playingField;

    /**
     * Pane to put the removed tokens from player 1
     * @see # removeToken(stone)
     */
    @FXML
    private Pane panePlayer1RemovedTokens;

    /**
     * Pane to put the removed tokens from player 2
     * @see  # removeToken(stone)
     */
    @FXML
    private Pane panePlayer2RemovedTokens;

    /**
     * update Label for players1 name at his turn
     * @see #updatePlayer()
     */
    @FXML
    private Label labelPlayer1;

    /**
     /**
     * update Label for players2 name at his turn
     * @see #updatePlayer()
     */
    @FXML
    private Label labelPlayer2;

    /**
     * label for the announcements
     * @see  # setAnnouncement(String)
     */
    @FXML
    private Label labelAnnouncements;

    /**
     * Method that hands over objects
     * @param mainClassObject instance of Main Class
     */
    public void setObjects(Main mainClassObject) {
        this.mainClassObject = mainClassObject;
    }

    /**
     * creates the playing Field and generates all the {@link Rectangle}
     * @param amount the size of the playing Field ( 8 for 8x8 or 10 for 10x10)
     * @param size   the size of the playing Field in Pixel (primarystage.getHeight - 200) TODO : Better Formula for size
     * @param pf Object from {@link PlayingField} to read the Fields
     */
    public void buildPlayingField(int amount, int size, PlayingField pf) {
        this.amount = amount;
        this.size = size;
        this.tokenRadius = size / amount / 3;

        field = new ArrayList<>();
        clearField();
        playingField.setPrefSize(size, size);
        double a = (double)size / amount;
        for (int i = 0; i < amount; i++) {
            for (int j = 0; j < amount; j++) {
                Rectangle rectangle = new Rectangle();
                rectangle.setArcHeight(0);
                rectangle.setArcWidth(0);
                rectangle.setHeight(a);
                rectangle.setWidth(a);
                rectangle.setLayoutX(i * a);
                rectangle.setLayoutY(size - j * a - a);
                rectangle.setOnMouseClicked(this::onFieldClick);
                field.add(rectangle);
                playingField.getChildren().add(rectangle);
                pf.getField(i, j).setcRec(rectangle);
            }
        }
        colorField();
        setAnnouncement("");
    }

    /**
     * Colors the Fields, where the tokens are, at the standard color
     * */
    public void colorField() {
        int i = 0;
        for (Rectangle rectangle : field) {
            if (((i / amount) + (i % amount)) % 2 == 0) {
                rectangle.setFill(Color.SIENNA);
            } else {
                rectangle.setFill(Color.BLACK);
            }
            i++;
        }
    }

    /**
     * removes all the tokens (and the removed tokens) from the screen
     */
    public void clearField() {
        panePlayer1RemovedTokens.getChildren().clear();
        panePlayer2RemovedTokens.getChildren().clear();
        playingField.getChildren().clear();
    }

    /**
     * Token {@link Circle} generator.
     * @param p both players
     */
    public void createTokens(Player... p) {
        for (Player player : p) {
            for (Token token : player.getTokens()) {
                initToken(token.getIndexX(), token.getIndexY(), token.getNodeCircle(), token.getColor() == ffhs.model.Color.BLACK ? Color.BLACK : Color.DARKRED);
            }
        }
        setNames(p[0].getName(), p[1].getName());
        updatePlayer();
    }

    /**
     * generate a token {@link Circle} and set it to the right position on the field.
     * @param x X-Position
     * @param y Y-Position
     * @param c the circle
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
     * set the names of both players.
     * @param name1 Name of Player1
     * @param name2 Name of Player2
     */
    private void setNames(String name1, String name2) {
        if (name1 != null && !name1.isEmpty()) {
            labelPlayer1.setText(name1);
        }
        if (name2 != null && !name2.isEmpty()) {
            labelPlayer2.setText(name2);
        }
    }

    /**
     * colors the name of the place, whose turn is now
     */
    public void updatePlayer() {
        if (mainClassObject.getPlayerController().isCurrentPlayer1()) {
            labelPlayer1.setId("playerNameOnTurn");
            labelPlayer2.setId("playerNameNotOnTurn");
        }
        else {
            labelPlayer1.setId("playerNameNotOnTurn");
            labelPlayer2.setId("playerNameOnTurn");
        }
    }

    /**
     * Warning on top of the screen.
     * @param message announcement, which will be displayed.
     */
    private void setAnnouncement(String message) {
        labelAnnouncements.setText(message);
    }

    /**
     * removes a token from the field
     * The removed token will be broken into 2 pieces
     * @param token token that will be moved
     */
    private void removeToken(Token token) {
        playingField.getChildren().remove(token.getNodeCircle());
        if (token.getColor() == ffhs.model.Color.BLACK) {
            panePlayer1RemovedTokens.getChildren().add(token.getNodeCircle());
            double off = 0;
            if (token.getNodeCircle() instanceof StackPane) {
                off = tokenRadius;
            }
            token.getNodeCircle().setLayoutX(panePlayer1RemovedTokens.getWidth() / 2 - off);
            token.getNodeCircle().setLayoutY(panePlayer1RemovedTokens.getChildren().size() * tokenRadius - off);
        }
        else {
            panePlayer2RemovedTokens.getChildren().add(token.getNodeCircle());
            double off = 0;
            if (token.getNodeCircle() instanceof StackPane) {
                off = tokenRadius;
            }
            token.getNodeCircle().setLayoutX(panePlayer2RemovedTokens.getWidth() / 2 - off);
            token.getNodeCircle().setLayoutY(panePlayer2RemovedTokens.getChildren().size() * tokenRadius - off);
        }
    }

    /**
     * moves a token across the field.
     * skip-fields will be removed
     * during the token movements, the players are locked {@link #graphicAction}
     * @param move Move, that the token does
     */
    public void moveToken(Move move) {
        graphicAction = true;
        double value = (double)size / amount;
        updateToken(move.getToken().getNodeCircle());
        Token token = move.getToken();
        Timer t = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    try {
                        if (!isTokenNearField(token, move.getNextField(), value)) {
                            calculateTokenLocation(token.getNodeCircle(), value, move);
                            if (move.getFirstSkipedField() != null && isTokenNearField(token, move.getFirstSkipedField(), value)) {
                                Token token = mainClassObject.getPlayerController().getOtherPlayer()
                                        .getTokenAt(move.getFirstSkipedField().getIndexX(), move.getFirstSkipedField().getIndexY());
                                if (token != null) {
                                    token.setEliminated();
                                    removeToken(token);
                                    move.nextSkipedField();
                                }
                            }
                        }
                        else {
                            if (!move.nextField()) {
                                placeToken(move.getEndField().getIndexX(), move.getEndField().getIndexY(), token.getNodeCircle());
                                t.cancel();
                                t.purge();
                                if (token.isKing()) {
                                    visualizedKing(token);
                                }
                                mainClassObject.getGame().finishedMove();
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
     * @param t token will be tested
     * @param f Field that will be tested if the token is on it.
     * @param value distance that the token moved
     * @return true, if the token is on the field.
     */
    private boolean isTokenNearField(Token t, Field f, double value) {
        double off = 0;
        if (t.getNodeCircle() instanceof StackPane) {
            off = tokenRadius;
        }
        return (f.getIndexX() + 0.48) * value <= t.getNodeCircle().getLayoutX() + off && (f.getIndexX() + 0.52) * value >= t.getNodeCircle().getLayoutX() + off &&
                size - (f.getIndexY() + 0.52) * value <= t.getNodeCircle().getLayoutY() + off && size - (f.getIndexY() + 0.48) * value >= t.getNodeCircle().getLayoutY() + off;
    }

    /**
     * has all visited fields and the possible fields to be moved into.
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
     * set the x- and the y-coordinates from a Node, and check the king.
     * Normal tokens are round and have a  ZeroSpot in der Middle ({@link Circle}).
     * A King has a crown. Has a zeroPoint up and left.
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
     * update a token, so that it will be all over up.
     * the node will be for a short time removed from the field and then added again (movement animation).
     * check {@link #moveToken(Move)}
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
     * @param t the King token.
     */
    private void visualizedKing(Token t) {
        if (t.isKing() && t.getNodeCircle() instanceof Circle) {
            Circle c = (Circle)t.getNodeCircle();
            c.setOnMouseClicked(null);
            StackPane sp = new StackPane();
            sp.setPrefSize(Control.USE_COMPUTED_SIZE, Control.USE_COMPUTED_SIZE);
            sp.getChildren().add(c);

            ImageView iw = new ImageView(mainClassObject.getKingImage());
            iw.setFitHeight(c.getRadius() * 1.5);
            iw.setFitWidth(c.getRadius() * 1.5);

            sp.getChildren().add(iw);
            sp.setLayoutX(c.getLayoutX() - c.getRadius());
            sp.setLayoutY(c.getLayoutY() - c.getRadius());
            sp.setOnMouseClicked(this::onFieldClick);
            playingField.getChildren().remove(c);
            playingField.getChildren().add(sp);
            t.changeNode(sp);
        }
    }

    /**
     * processes with mouse clicks on the game Field.
     * The things that can be clicked at a{@link Rectangle} from a {@link Field},
     * a {@link Circle} from a {@link Token} or a {@link StackPane} from a King.
     * @param e MouseEvent
     * @see Game#selectToken(Token)
     * @see Game#selectField(Field)
     */
    private void onFieldClick(MouseEvent e) {
        if (!graphicAction && (!mainClassObject.getPlayerController().isSinglePlayerGame() || mainClassObject.getPlayerController().isCurrentPlayer1())) {
            if (e.getSource() instanceof Rectangle || e.getSource() instanceof Circle || e.getSource() instanceof StackPane) {
                setAnnouncement("");
                if (e.getSource() instanceof Rectangle) {
                    Rectangle temp = (Rectangle) e.getSource();
                    int index = field.indexOf(temp);
                    int x = (index / amount);
                    int y = index % amount;
                    Field pressedField = Main.playingField.getField(x, y);
                    mainClassObject.getGame().selectField(pressedField);
                }
                else {
                    Token t = mainClassObject.getPlayerController().getCurrentPlayer().getStoneOfClickedCircle((Node)e.getSource());
                    if (t == null) {
                        setAnnouncement("This is NOT your token");
                        return;
                    }
                    else if (t.isEliminated()) {
                        setAnnouncement("This token was already eliminated");
                        return;
                    }
                    mainClassObject.getGame().selectToken(t);
                }
            }
        }
    }
}



