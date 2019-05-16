package ffhs.controller;

import ffhs.model.*;
import ffhs.model.Field;

import java.util.ArrayList;
import java.util.List;

/**
 * Steuerung von Aktionen die während des Spielens vorkommen.
 *
 * @author Alexander Hengsteler
 * @author Joel Schmid
 */
public class Game {

    private Main control;
    private GameWindowController gamePaneController;
    private PlayerController playerController;

    private List<Field> possibleFields;
    private List<Field> visitedFields;
    private Move move;
    private Field currentField;

    public Game(Main control, GameWindowController gamePaneController, PlayerController playerController) {
        this.control = control;
        this.gamePaneController = gamePaneController;
        this.playerController = playerController;
        possibleFields = new ArrayList<>();
        visitedFields = new ArrayList<>();
        move = new Move();
    }

    /**
     * spiel wird zurückgesetzt.
     */
    public void reset() {
        possibleFields.clear();
        move.setOutdated(true);
    }

    /**
     * ermittelt, ob f ein leeres Feld ist.
     *
     * @param f Feld
     * @return ist auf f ein Stein
     */
    private boolean emptyField(Field f) {
        return !playerController.getCurrentPlayer().hasStoneAt(f.getIndexX(), f.getIndexY()) &&
                !playerController.getOtherPlayer().hasStoneAt(f.getIndexX(), f.getIndexY());
    }

    /**
     * ermittelt die Felder, die vom Feld(x,y) erreichbar sind.
     *
     * @author Joel Schmid
     * @see #testFieldScope(Field, Color, boolean, boolean)
     * @param x aktuelle X-Koordinate
     * @param y aktuelle Y-Koordinate
     * @param indexX wie weit von X entfernnt
     * @param indexY wie weit von Y entfernnt
     * @param indexX2 x + indexX2 ist Zielkoordinate, wenn x + indexX ein gegnerischer Stein ist
     * @param indexY2 y + indexY2 ist Zielkoordinate, wenn y + indexY ein gegnerischer Stein ist
     * @param further ist dies bereits ein weiterer zug, oder noch der erste. Beim ersten Zug wäre es möglich, nur ein Feld weiter zu gehen. Bei weiteren Zügen muss geschlagen werden.
     * @return suche nach weiteren Feldern in dieser Richtung abbrechen
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
     * Ausgehend von f werden die möglichen Zielfelder ermittelt.
     *
     * @author Joel Schmid
     * @see #testField(int, int, int, int, int, int, boolean)
     * @param f Startfeld
     * @param c Spielerfarbe
     * @param further Wurde bereits ein gegnerischer Stein in diesem Zug übersprungen (es kann dann nur weitergemacht werden, wenn ein weiterer Stein geschlagen werden kann)
     * @param superDame ist der Stein eine Superdame
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
     * Führt eine passende Option für den angeklickten Stein aus.
     * Wenn der Stein bereits der Stein zu dem aktuellen Move ist, wird der Stein wieder abgewählt, also der Move auf veraltet gesetzt ({@link Move#setOutdated(boolean)}).
     * <br>Ein neuer Move mit dem gewählten Stein wird aufgebaut.
     *
     * @param s angeklickter Stein
     */
    public void selectStone(Stone s) {
        if (move != null && move.getStone() == s && !move.isOutdated() && Main.playingField.getField(s.getIndexX(), s.getIndexY()) != currentField) {
            gamePaneController.colorField();
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
            gamePaneController.highlightFields(possibleFields, move);
        }
        else  if (move != null && !move.isOutdated()) {
            Field f = Main.playingField.getField(s.getIndexX(), s.getIndexY());
            selectField(f);
        }
    }

    /**
     * Führt eine passende Aktion für das angeklickte Feld aus.
     * Wenn durch das angeglickte Feld, ein anderes Feld übersprungen wird, wird ermittelt, auf welchen übersprungenen Feld
     * der gegnerische Stein liegt. Dieses Feld wird als skippedField zum Move hinzugefügt.
     * Sobald von dem angeglickten Feld kein weiterer Zug mehr möglich ist wird der aufgebaute {@link Move} ausgeführt. ({@link #makeMove(Move)})
     *
     * @param f angeklicktes Feld
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
                            gamePaneController.colorField();
                            possibleFields.clear();

                            testFieldScope(move.getEndField(), move.getStone().getColor(), true, move.getStone().isSuperDame());
                            if (!possibleFields.isEmpty()) {
                                gamePaneController.highlightFields(possibleFields, move);
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
     * Ein Spielzug wird ausgeführt.
     *
     * @param move Spielzug
     */
    private void makeMove(Move move) {
        gamePaneController.colorField();
        gamePaneController.moveToken(move);
        move.update();
        testForSuperDame(move.getStone());
        possibleFields.clear();
    }

    /**
     * Überprüft nach Beenden des Zuges, ob ein Spieler gewonnen hat und wechselt den aktuellen Spieler, wenn dies nicht der Fall ist.
     */
    public void finishedMove() {
        if (!testForWinner()) {
            move.setOutdated(true);
            playerController.changePlayer();
            gamePaneController.updatePlayer();
            playKI();
        }
    }

    /**
     * Ist der Stein s an der gegnerischen Grundlinie angekommen, wird er zur Superdame.
     *
     * @param s Stein der getestet wird.
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
     * hat ein Spieler bereits gewonnen
     *
     * @return {@code true} falls ein Spieler gewonnen hat. Dann kann das Spiel beendet werden.
     */
    private boolean testForWinner() {
        if (!isMovePossible(playerController.getOtherPlayer())) {
            control.winDialog(playerController.getCurrentPlayer().getName());
            return true;
        }
        return false;
    }

    /**
     * ist ein Spielzug des Spielers p möglich
     *
     * @param p Spieler
     * @return gibt es einen möglichen Spielzug
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
     * Im einzelspielermodus wird die KI ausgeführt, im Mehrspielermodus wird nichts gemacht
     */
    private void playKI() {
        if(playerController.isSinglePlayerGame() && !playerController.isCurrentPlayer1()) {
            try {
                Move m = ((KI) playerController.getPlayer2()).getBestMove();
                makeMove(m);
            } catch (NoPossibleMoveException e) {
                control.winDialog(playerController.getPlayer1().getName());
            }

        }
    }

}
