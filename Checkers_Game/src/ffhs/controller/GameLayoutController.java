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

public class GameLayoutController {

    private Main mainClassObject;
    private int size;
    private int amount;
    private int tokenRadius;
    private boolean graphicAction;
    private ArrayList<Rectangle> field;
    @FXML
    private Pane playingField;
    @FXML
    private Pane panePlayer1RemovedTokens;
    @FXML
    private Pane panePlayer2RemovedTokens;
    @FXML
    private Label labelPlayer1;
    @FXML
    private Label labelPlayer2;
    @FXML
    private Label labelAnnouncements;

    public void setObjects(Main mainClassObject) {
        this.mainClassObject = mainClassObject;
    }

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

    public void clearField() {
        panePlayer1RemovedTokens.getChildren().clear();
        panePlayer2RemovedTokens.getChildren().clear();
        playingField.getChildren().clear();
    }

    public void createTokens(Player... p) {
        for (Player player : p) {
            for (Token token : player.getTokens()) {
                initToken(token.getIndexX(), token.getIndexY(), token.getNodeCircle(), token.getColor() == ffhs.model.Color.BLACK ? Color.BLACK : Color.DARKRED);
            }
        }
        setNames(p[0].getName(), p[1].getName());
        updatePlayer();
    }

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

    private void setNames(String name1, String name2) {
        if (name1 != null && !name1.isEmpty()) {
            labelPlayer1.setText(name1);
        }
        if (name2 != null && !name2.isEmpty()) {
            labelPlayer2.setText(name2);
        }
    }

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

    private void setAnnouncement(String message) {
        labelAnnouncements.setText(message);
    }

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

    private void calculateTokenLocation(Node n, double value, Move move) {
        n.setLayoutX(n.getLayoutX() + (value / 12)
                * (move.getNextField().getIndexX() >= move.getCurrentField().getIndexX() ? 1 : -1));
        n.setLayoutY(n.getLayoutY() + (value / 12)
                * (move.getNextField().getIndexY() >= move.getCurrentField().getIndexY() ? -1 : 1));
    }

    private boolean isTokenNearField(Token t, Field f, double value) {
        double off = 0;
        if (t.getNodeCircle() instanceof StackPane) {
            off = tokenRadius;
        }
        return (f.getIndexX() + 0.48) * value <= t.getNodeCircle().getLayoutX() + off && (f.getIndexX() + 0.52) * value >= t.getNodeCircle().getLayoutX() + off &&
                size - (f.getIndexY() + 0.52) * value <= t.getNodeCircle().getLayoutY() + off && size - (f.getIndexY() + 0.48) * value >= t.getNodeCircle().getLayoutY() + off;
    }

    public void highlightFields(List<Field> fields, Move move) {
        colorField();
        for (Field f : move.getEnteredFields()) {
            f.getcRec().setFill(Color.BLUE);
        }
        for (Field f : fields) {
            f.getcRec().setFill(Color.DARKGREEN);
        }
    }

    private void placeToken(int x, int y, Node node) {
        double a = (double)size / amount;
        double off = 0;
        if (node instanceof StackPane) {
            off = tokenRadius;
        }
        node.setLayoutX((x + 0.5) * a - off);
        node.setLayoutY(size - (y + 0.5) * a - off);
    }

    private void updateToken(Node c) {
        playingField.getChildren().remove(c);
        playingField.getChildren().add(c);
    }

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



