package ffhs.controller;

import ffhs.model.*;
import ffhs.model.Field;

import java.util.ArrayList;
import java.util.List;

/**
 * Control from the in-game Actions.
 */
public class Game {

    private Main control;
    private GameLayoutController gameLayoutController;
    private PlayerController playerController;

    private List<Field> possibleFields;
    private List<Field> visitedFields;
    private Move move;
    private Field currentField;

    public Game(Main control, GameLayoutController gameLayoutController, PlayerController playerController) {
        this.control = control;
        this.gameLayoutController = gameLayoutController;
        this.playerController = playerController;
        possibleFields = new ArrayList<>();
        visitedFields = new ArrayList<>();
        move = new Move();
    }

    /**
     * The Game will be restarted.
     */
    public void reset() {
        possibleFields.clear();
        move.setOutdated(true);
    }

    /**
     * determine if f is an empty field
     * @param f Field
     * @return is there in f a token
     */
    private boolean emptyField(Field f) {
        return !playerController.getCurrentPlayer().hasTokenAt(f.getIndexX(), f.getIndexY()) &&
                !playerController.getOtherPlayer().hasTokenAt(f.getIndexX(), f.getIndexY());
    }

    /**
     * determines the fields(x,y) that can be reached form this position.
     * @see #testFieldScope(Field, Color, boolean, boolean)
     * @param x actuell X-Coordinate
     * @param y actuell Y-Coordinate
     * @param indexX distance from X
     * @param indexY distance from Y
     * @param indexX2 x + indexX2 is goal-coordinate, if x + indexX is an opponent token.
     * @param indexY2 y + indexY2 ist goal-coordinate, if y + indexY is an opponent token.
     * @param further if this the  first turn or not. At the first turn you can do only one movement.
     * @return search further fields tot test
     */
    private boolean testField(int x, int y, int indexX, int indexY, int indexX2, int indexY2, boolean further) {
        Field field = Main.playingField.getField(x + indexX, y + indexY);
        Field field2;
        if (field != null) {
            if (emptyField(field) && !further) {
                possibleFields.add(field);
                return true;
            }
            else if (playerController.getOtherPlayer().hasTokenAt(field.getIndexX(), field.getIndexY())) {
                field2 = Main.playingField.getField(x + indexX2, y + indexY2);
                if(!visitedFields.contains(field)) {
                    if (field2 != null && emptyField(field2) || field2 != null && move.getFirstField() == field2) {
                        possibleFields.add(field2);
                        currentField = field2;
                        return false;
                    }
                }
            }
        }
        return false;
    }

    /**
     * determine the goal-fields from f.
     * @see #testField(int, int, int, int, int, int, boolean)
     * @param f Start Field
     * @param c Players color
     * @param further Was an opponent's token hit in this turn
     * @param king is the token a KING
     */
    private void testFieldScope(Field f, Color c, boolean further, boolean king) {
        if(king){
            for(int i = 1; i < Main.playingField.getSize(); i++) {
                if(!testField(f.getIndexX(), f.getIndexY(), i, i, i+1,i+1, further)){
                    break;
                }
            }
            for(int i = 1; i < Main.playingField.getSize(); i++) {
                if(!testField(f.getIndexX(), f.getIndexY(), i, -i,i+1,-i-1, further)){
                    break;
                }
            }
            for(int i = 1; i < Main.playingField.getSize(); i++) {
                if(!testField(f.getIndexX(), f.getIndexY(), -i, i,-i-1, i+1, further)){
                    break;
                }
            }
            for(int i = 1; i < Main.playingField.getSize(); i++) {
                if(!testField(f.getIndexX(), f.getIndexY(), -i, -i, -i-1, -i-1, further)){
                    break;
                }
            }
        }else {
            if (c == Color.BLACK) {
                testField(f.getIndexX(), f.getIndexY(), 1, 1,2,2, further);
                testField(f.getIndexX(), f.getIndexY(), -1, 1,-2,2, further);
            } else {
                testField(f.getIndexX(), f.getIndexY(), 1, -1,2,-2, further);
                testField(f.getIndexX(), f.getIndexY(), -1, -1,-2,-2, further);
            }
        }
    }

    /**
     * makes an options for the selected field.
     * If the token is the selected one, then the move will be done ({@link Move#setOutdated(boolean)}).
     * A new move with the selected token will be made.
     * @param t selected token
     */
    public void selectToken(Token t) {
        if (move != null && move.getToken() == t && !move.isOutdated() && Main.playingField.getField(t.getIndexX(), t.getIndexY()) != currentField) {
            gameLayoutController.colorField();
            move.setOutdated(true);
            visitedFields.clear();
            currentField = null;
            return;
        }
        if(currentField == null || Main.playingField.getField(t.getIndexX(), t.getIndexY()) != currentField) {
            move.init(t);
            Field f = Main.playingField.getField(move.getToken().getIndexX(), move.getToken().getIndexY());
            move.addEnterField(f);
            possibleFields.clear();
            testFieldScope(f, t.getColor(), false, t.isKing());
            gameLayoutController.highlightFields(possibleFields, move);
        }
        else  if (move != null && !move.isOutdated()) {
            Field f = Main.playingField.getField(t.getIndexX(), t.getIndexY());
            selectField(f);
        }
    }

    /**
     * makes an action for the selected field
     * If the token skips a filed, it determines the skipped field and where a token is.
     * if a move can be made then the {@link Move} will be activated. ({@link #makeMove(Move)})
     * @param f selected Field
     */
    public void selectField(Field f) {
        if (move != null && !move.isOutdated()) {
            if (!possibleFields.isEmpty() && possibleFields.contains(f)) {
                move.addEnterField(f);
                if (Math.abs(move.getLastField().getIndexX() - f.getIndexX()) >= 2) {
                    int x,y;
                    for (int i = 1; i < Math.abs(move.getLastField().getIndexX() - f.getIndexX()); i++) {
                        if (f.getIndexX() > move.getLastField().getIndexX()) {
                            x = move.getLastField().getIndexX() + i;
                        }
                        else {
                            x = move.getLastField().getIndexX() - i;
                        }
                        if (f.getIndexY() > move.getLastField().getIndexY()) {
                            y = move.getLastField().getIndexY() + i;
                        }
                        else {
                            y = move.getLastField().getIndexY() - i;
                        }
                        if (playerController.getOtherPlayer().hasTokenAt(x, y)) {
                            move.addSkipField(Main.playingField.getField(x, y));

                            visitedFields.add(Main.playingField.getField(x, y));
                            gameLayoutController.colorField();
                            possibleFields.clear();

                            testFieldScope(move.getEndField(), move.getToken().getColor(), true, move.getToken().isKing());
                            if (!possibleFields.isEmpty()) {
                                gameLayoutController.highlightFields(possibleFields, move);
                                return;
                            }
                        }
                    }
                }
                currentField = null;
                visitedFields.clear();
                makeMove(move);
            }
            else if (move.getEndField().equals(f) && move.getEndField() != move.getFirstField()) {
                makeMove(move);
            }
            else if (playerController.getCurrentPlayer().hasTokenAt(f.getIndexX(), f.getIndexY()) && currentField == null) {
                selectToken(playerController.getCurrentPlayer().getTokenAt(f.getIndexX(), f.getIndexY()));
            }
            else if(currentField != null){
                currentField = null;
                visitedFields.clear();
                possibleFields.clear();
                move.setOutdated(true);
                selectToken(playerController.getCurrentPlayer().getTokenAt(f.getIndexX(), f.getIndexY()));
            }
        }
        else if (playerController.getCurrentPlayer().hasTokenAt(f.getIndexX(), f.getIndexY())) {
            selectToken(playerController.getCurrentPlayer().getTokenAt(f.getIndexX(), f.getIndexY()));
        }
    }

    /**
     * a movement will be done
     * @param move Player's move
     */
    private void makeMove(Move move) {
        gameLayoutController.colorField();
        gameLayoutController.moveToken(move);
        move.update();
        testForKing(move.getToken());
        possibleFields.clear();
    }

    /**
     * Checks after the turn if the player won, else it is the other players turn to play
     */
    public void finishedMove() {
        if (!testForWinner()) {
            move.setOutdated(true);
            playerController.changePlayer();
            gameLayoutController.updatePlayer();
            playAI();
        }
    }

    /**
     * If the token reaches the right point will be KING
     * @param t token that will be tested
     */
    private void testForKing(Token t) {
        if (t.getColor() == Color.BLACK) {
            if (t.getIndexY() == Main.playingField.getSize() - 1) {
                t.setKing();
            }
        }
        else if (t.getIndexY() == 0) {
            t.setKing();
        }
    }

    /**
     * has the player already won.
     * @return {@code true} if a player has won, can the game end
     */
    private boolean testForWinner() {
        if (!isMovePossible(playerController.getOtherPlayer())) {
            control.winDialog(playerController.getCurrentPlayer().getName());
            return true;
        }
        return false;
    }

    /**
     * is the Movement from a player possible
     * @param p Player
     * @return returns a possible movement
     */
    private boolean isMovePossible(Player p) {
        for (Token s : p.getTokens()) {
            if (!s.isEliminated()) {
                testFieldScope(Main.playingField.getField(s.getIndexX(), s.getIndexY()), s.getColor(), false, s.isKing());
                if (!possibleFields.isEmpty()) {
                    possibleFields.clear();
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * In SinglePlayer Mode an AI will be added , in Multiplayer nothing will be done.
     */
    private void playAI() {
        if(playerController.isSinglePlayerGame() && !playerController.isCurrentPlayer1()) {
            try {
                Move m = ((AI) playerController.getPlayer2()).getBestMove();
                makeMove(m);
            } catch (NoPossibleMoveException e) {
                control.winDialog(playerController.getPlayer1().getName());
            }
        }
    }
}
