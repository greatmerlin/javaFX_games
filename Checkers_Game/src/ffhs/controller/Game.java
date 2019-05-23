package ffhs.controller;

import ffhs.model.*;
import ffhs.model.Field;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private Main mainClassObject;
    private GameLayoutController gameLayoutController;
    private PlayerController playerController;
    private List<Field> possibleFields;
    private List<Field> visitedFields;
    private Move move;
    private Field currentField;

    public Game(Main mainClassObject, GameLayoutController gameLayoutController, PlayerController playerController) {
        this.mainClassObject = mainClassObject;
        this.gameLayoutController = gameLayoutController;
        this.playerController = playerController;
        possibleFields = new ArrayList<>();
        visitedFields = new ArrayList<>();
        move = new Move();
    }

    public void reset() {
        possibleFields.clear();
        move.setOutdated(true);
    }

    private boolean emptyField(Field f) {
        return !playerController.getCurrentPlayer().hasTokenAt(f.getIndexX(), f.getIndexY()) &&
                !playerController.getOtherPlayer().hasTokenAt(f.getIndexX(), f.getIndexY());
    }

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

    private void makeMove(Move move) {
        gameLayoutController.colorField();
        gameLayoutController.moveToken(move);
        move.update();
        testForKing(move.getToken());
        possibleFields.clear();
    }

    public void finishedMove() {
        if (!testForWinner()) {
            move.setOutdated(true);
            playerController.changePlayer();
            gameLayoutController.updatePlayer();
            playAI();
        }
    }

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

    private boolean testForWinner() {
        if (!isMovePossible(playerController.getOtherPlayer())) {
            mainClassObject.winDialog(playerController.getCurrentPlayer().getName());
            return true;
        }
        return false;
    }

    private boolean isMovePossible(Player p) {
        for (Token t : p.getTokens()) {
            if (!t.isEliminated()) {
                testFieldScope(Main.playingField.getField(t.getIndexX(), t.getIndexY()), t.getColor(), false, t.isKing());
                if (!possibleFields.isEmpty()) {
                    possibleFields.clear();
                    return true;
                }
            }
        }
        return false;
    }

    private void playAI() {
        if(playerController.isSinglePlayerGame() && !playerController.isCurrentPlayer1()) {
            try {
                Move m = ((AI) playerController.getPlayer2()).getBestMove();
                makeMove(m);
            } catch (NoPossibleMoveException e) {
                mainClassObject.winDialog(playerController.getPlayer1().getName());
            }
        }
    }
}
