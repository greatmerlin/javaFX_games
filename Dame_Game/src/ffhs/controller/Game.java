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
    private GameWindowController gameWindowController;
    private PlayerController playerController;

    private List<Field> possibleFields;
    private List<Field> visitedFields;
    private Move move;
    private Field currentField;

    public Game(Main control, GameWindowController gameWindowController, PlayerController playerController) {
        this.control = control;
        this.gameWindowController = gameWindowController;
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
     *
     * @param f Field
     * @return is there in f a token
     */
    private boolean emptyField(Field f) {
        return !playerController.getCurrentPlayer().hasStoneAt(f.getIndexX(), f.getIndexY()) &&
                !playerController.getOtherPlayer().hasStoneAt(f.getIndexX(), f.getIndexY());
    }

    /**
     * determines the fields(x,y) that can be reached form this position.
     *
     * @see #testFieldScope(Field, Color, boolean, boolean)
     * @param x actuell X-Coordinate
     * @param y actuell Y-Coordinate
     * @param indexX dustance from X
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
            else if (playerController.getOtherPlayer().hasStoneAt(field.getIndexX(), field.getIndexY())) {
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
     *
     * @see #testField(int, int, int, int, int, int, boolean)
     * @param f Startfeld
     * @param c Players color
     * @param further Was an opponent's token hit in this turn
     * @param superDame is the token a KING/Queen/SuperDame
     */
    private void testFieldScope(Field f, Color c, boolean further, boolean superDame) {
        if(superDame){
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
     *
     * @param s selected token
     */
    public void selectStone(Stone s) {
        if (move != null && move.getStone() == s && !move.isOutdated() && Main.playingField.getField(s.getIndexX(), s.getIndexY()) != currentField) {
            gameWindowController.colorField();
            move.setOutdated(true);
            visitedFields.clear();
            currentField = null;
            return;
        }
        if(currentField == null || Main.playingField.getField(s.getIndexX(), s.getIndexY()) != currentField) {
            move.init(s);
            Field f = Main.playingField.getField(move.getStone().getIndexX(), move.getStone().getIndexY());
            move.addEnterField(f);
            possibleFields.clear();
            testFieldScope(f, s.getColor(), false, s.isSuperDame());
            gameWindowController.highlightFields(possibleFields, move);
        }
        else  if (move != null && !move.isOutdated()) {
            Field f = Main.playingField.getField(s.getIndexX(), s.getIndexY());
            selectField(f);
        }
    }

    /**
     * makes an action for the selected field
     * If the token skips a filed, it determines the skipped field and where a token is.
     * if a move can be made then the {@link Move} will be activated. ({@link #makeMove(Move)})
     *
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
                        if (playerController.getOtherPlayer().hasStoneAt(x, y)) {
                            move.addSkipField(Main.playingField.getField(x, y));

                            visitedFields.add(Main.playingField.getField(x, y));
                            gameWindowController.colorField();
                            possibleFields.clear();

                            testFieldScope(move.getEndField(), move.getStone().getColor(), true, move.getStone().isSuperDame());
                            if (!possibleFields.isEmpty()) {
                                gameWindowController.highlightFields(possibleFields, move);
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
            else if (playerController.getCurrentPlayer().hasStoneAt(f.getIndexX(), f.getIndexY()) && currentField == null) {
                selectStone(playerController.getCurrentPlayer().getStoneAt(f.getIndexX(), f.getIndexY()));
            }
            else if(currentField != null){
                currentField = null;
                visitedFields.clear();
                possibleFields.clear();
                move.setOutdated(true);
                selectStone(playerController.getCurrentPlayer().getStoneAt(f.getIndexX(), f.getIndexY()));
            }
        }
        else if (playerController.getCurrentPlayer().hasStoneAt(f.getIndexX(), f.getIndexY())) {
            selectStone(playerController.getCurrentPlayer().getStoneAt(f.getIndexX(), f.getIndexY()));
        }
    }

    /**
     * a movement will be done
     *
     * @param move Player's move
     */
    private void makeMove(Move move) {
        gameWindowController.colorField();
        gameWindowController.moveToken(move);
        move.update();
        testForSuperDame(move.getStone());
        possibleFields.clear();
    }

    /**
     * Checks after the turn if the player won, else it is the other players turn to play
     */
    public void finishedMove() {
        if (!testForWinner()) {
            move.setOutdated(true);
            playerController.changePlayer();
            gameWindowController.updatePlayer();
            playAI();
        }
    }

    /**
     * If the token reaches the right point will be KING/Queen/SuperDame
     *
     * @param s token that will be tested
     */
    private void testForSuperDame(Stone s) {
        if (s.getColor() == Color.BLACK) {
            if (s.getIndexY() == Main.playingField.getSize() - 1) {
                s.setSuperDame();
            }
        }
        else if (s.getIndexY() == 0) {
            s.setSuperDame();
        }
    }

    /**
     * has the player already won
     *
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
     *
     * @param p Player
     * @return returns a possible movement
     */
    private boolean isMovePossible(Player p) {
        for (Stone s : p.getStones()) {
            if (!s.isEliminated()) {
                testFieldScope(Main.playingField.getField(s.getIndexX(), s.getIndexY()), s.getColor(), false, s.isSuperDame());
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
