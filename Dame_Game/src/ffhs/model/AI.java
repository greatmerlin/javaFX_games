package ffhs.model;

import controller.Main;

import java.util.ArrayList;
import java.util.List;

/**
 * AI for the "Versus AI" Mode, inheritates von Player.
 */
public class AI extends Player {

    /**
     * human opponent.
     */
    private Player enemy;

    /**
     * Lists of all the posible moves, that can be done from the AI.
     */
    private List<Moves> allMoves;

    public AI(Color c, String name, int size, Player enemy) {
        super(c, name, size);   //Contructor call in Player
        this.enemy = enemy;
    }

    /**
     * Wird von der KI ein Zug erwartet, wird diese Methode aufgerufen, sie ermittelt einen möglichst langen Zug.
     *
     * @throws NoPossibleMoveException Die KI kann keinen Zug mehr machen.
     * @return Zugfolge
     */
    public Move getBestMove() throws NoPossibleMoveException{
        allMoves = new ArrayList<>();

        //Für alle noch nicht eliminierten Steine werden alle möglichen Züge ermittelt (KI ist eine rekursive Methode)
        for (Stone s : getStones()) {
            if (!s.isEliminated()) {
                ki(s, s.getIndexX(), s.getIndexY(), 0, true, new ArrayList<Field>(), new ArrayList<Field>());
            }
        }

        if(allMoves.size() == 0){
            throw new NoPossibleMoveException();
        }
        return Moves.getBestZug(allMoves);
    }

    /**
     * Hier wird ausgehend von X und Y Koordinaten geschaut, welcher Zug möglich ist.
     * normale Steine können nur nach unten schlagen/ziehen (RIGHTDOWN, LEFTDOWN), die Superdame kann hingegen in alle vier diagonalen Richtungen
     * (RIGHTDOWN, LEFTDOWN, RIGHTUP, LEFTUP) und auch mehrere Felder auf einmal.
     *
     * @see #ki(Stone, int, int, int, boolean, List, List)
     * @param s Stein, für den der Zug ermittelt wird
     * @param d Richtung, in die gefahren werden soll
     * @param x aktuelle X-Koordinate
     * @param y aktuelle Y-Koordinate
     * @param ersterDurchgang ist dies der erste Aufruf (noch nicht rekursiv aufgerufen)
     * @param skipped enthält alle gegnerischen Felder, die in diesem Zug bereits rausgeworfen wurden
     * @param entered enthält alle Felder, auf denen der eigene Stein kurz aufkommt
     * @return Zuglänge ausgehend von X und Y (Anzahl überquerte Felder)
     */
    private int diagonalcheck(Stone s, Direction d, int x, int y, boolean ersterDurchgang, List<Field> skipped, List<Field> entered) {

        // ermittelt möglichen Spielzug nach rechts unten
        if (d.equals(Direction.RIGHTDOWN)) {
            /*  Wenn s eine superdame ist, wird die for-Schleife mehrmals durchlaufen um auch einen weiter entfernten Stein zu finden,
                ansonsten nur einmal, um zu schauen, ob beim nächsten Feld bereits ein gegnerischer Stein ist. */
            for(int i = 0; i < (s.isSuperDame() ?  (Main.playingField.getSize()-2) : 1); i++) {

                // Sobald ein eigener Stein im Weg ist wird abgebrochen oder sobald ein gegnerische Stein, hinter dem das Feld nicht frei ist.
                if(Main.playingField.isPositionInsideField(x + i + 1, y - i - 1) && hasStoneAt(x + i + 1, y - i - 1) ||
                        enemy.hasStoneAt(x + i + 1, y - i - 1) && (enemy.hasStoneAt(x + i + 2, y - i - 2) || hasStoneAt(x + i + 2, y - i - 2))) {
                    return 0;
                }

                /*  sind die nächsten zwei diagonalen Felder innerhalb des Spielfeldes,
                    ist der zu überspringende gegnerische Stein nicht bereits im gleichen zug schonmal besucht(Zyklus)
                    Gegnerischer Stein auf nächstem Feld, übernächstes muss aufgrund der Abbruchbedingung von oben leer sein*/
                if (Main.playingField.isPositionInsideField(x + i + 2, y - i - 2) && Main.playingField.isPositionInsideField(x + i + 1, y - i - 1)
                        && !skipped.contains(Main.playingField.getField(x + i + 1, y - i - 1))
                        && enemy.hasStoneAt(x + i + 1, y - i - 1)) {
                    //Übersprungener Stein
                    skipped.add(Main.playingField.getField(x + i + 1, y - i - 1));
                    //landepunkt, von dem ausgehend dann erneut geschaut wird, ob es weitere Zugmöglichkeiten gibt
                    entered.add(Main.playingField.getField(x + i + 2, y - i - 2));
                    //diese Punkte werden gespeichert, um später die Bewegungsanimation zu ermöglichen
                    return 2+i;
                }

            }
            /*  ist nächstes diagonales Feld innerhalb des Spielfeldes, und nicht von einem Spielstein besetzt, einfacher Zug, ist deshalb nur bei erstem
                Aufruf möglich, nicht im rekursionsfall */
            if (ersterDurchgang && Main.playingField.isPositionInsideField(x + 1, y - 1) && !(enemy.hasStoneAt(x + 1, y - 1) || hasStoneAt(x + 1, y - 1))) {
                entered.add(Main.playingField.getField(x + 1, y - 1));
                return 1;
            }
        }
        /*  In den folgenden else if- Fällen ist alles gleich wie im ersten Fall, es unterscheidet sich nur in der Richtung,
            die Überprüft wird. */
        else if (d.equals(Direction.LEFTDOWN)) {
            for(int i = 0; i < (s.isSuperDame() ?  (Main.playingField.getSize()-2) : 1); i++) {
                if(Main.playingField.isPositionInsideField(x - i - 1, y - i - 1) && hasStoneAt(x - i - 1, y - i - 1) ||
                        enemy.hasStoneAt(x - i - 1, y - i - 1) && (enemy.hasStoneAt(x - i - 2, y - i - 2) || hasStoneAt(x - i - 2, y - i - 2))) {
                    return 0;
                }
                if (Main.playingField.isPositionInsideField(x - i - 2, y - i - 2) && Main.playingField.isPositionInsideField(x - i - 1, y - i - 1)
                        && !skipped.contains(Main.playingField.getField(x - i - 1, y - i - 1))
                        && enemy.hasStoneAt(x - i - 1, y - i - 1)) {
                    skipped.add(Main.playingField.getField(x - i - 1, y - i - 1));
                    entered.add(Main.playingField.getField(x - i - 2, y - i - 2));
                    return 2+i;
                }

            }
            if (ersterDurchgang && Main.playingField.isPositionInsideField(x - 1, y - 1) && !(enemy.hasStoneAt(x - 1, y - 1) || hasStoneAt(x - 1, y - 1))) {
                entered.add(Main.playingField.getField(x - 1, y - 1));
                return 1;
            }
        } else if (s.isSuperDame() && d.equals(Direction.RIGHTUP)) {
            for(int i = 0; i < (s.isSuperDame() ?  (Main.playingField.getSize()-2) : 1); i++) {
                if(Main.playingField.isPositionInsideField(x + i + 1, y + i + 1) && hasStoneAt(x + i + 1, y + i + 1) ||
                        enemy.hasStoneAt(x + i + 1, y + i + 1) && (enemy.hasStoneAt(x + i + 2, y + i + 2) || hasStoneAt(x + i + 2, y + i + 2))) {
                    return 0;
                }
                if (Main.playingField.isPositionInsideField(x + i + 2, y + i + 2) && Main.playingField.isPositionInsideField(x + i + 1, y + i + 1)
                        && !skipped.contains(Main.playingField.getField(x + i + 1, y + i + 1))
                        && enemy.hasStoneAt(x + i + 1, y + i + 1)) {
                    skipped.add(Main.playingField.getField(x + i + 1, y + i + 1));
                    entered.add(Main.playingField.getField(x + i + 2, y + i + 2));
                    return 2+i;
                }

            }
            if (ersterDurchgang && Main.playingField.isPositionInsideField(x + 1, y + 1) && !(enemy.hasStoneAt(x + 1, y + 1) || hasStoneAt(x + 1, y + 1))) {
                entered.add(Main.playingField.getField(x + 1, y + 1));
                return 1;
            }
        } else if (s.isSuperDame() && d.equals(Direction.LEFTUP)) {
            for(int i = 0; i < (s.isSuperDame() ?  (Main.playingField.getSize()-2) : 1); i++) {
                if(Main.playingField.isPositionInsideField(x - i - 1, y + i + 1) && hasStoneAt(x - i - 1, y + i + 1) ||
                        enemy.hasStoneAt(x - i - 1, y + i + 1) && (enemy.hasStoneAt(x - i - 2, y + i + 2) || hasStoneAt(x - i - 2, y + i + 2))) {
                    return 0;
                }
                if (Main.playingField.isPositionInsideField(x - i - 2, y + i + 2) && Main.playingField.isPositionInsideField(x - i - 1, y + i + 1)
                        && !skipped.contains(Main.playingField.getField(x - i - 1, y + i + 1))
                        && enemy.hasStoneAt(x - i - 1, y + i + 1)) {
                    skipped.add(Main.playingField.getField(x - i - 1, y + i + 1));
                    entered.add(Main.playingField.getField(x - i - 2, y + i + 2));
                    return 2+i;
                }

            }
            if (ersterDurchgang && Main.playingField.isPositionInsideField(x - 1, y + 1) && !(enemy.hasStoneAt(x - 1, y + 1) || hasStoneAt(x - 1, y + 1))) {
                entered.add(Main.playingField.getField(x - 1, y + 1));
                return 1;
            }
        }
        return 0;
    }

    /**
     * Die KI Methode ermittelt mit Hilfe von {@link #diagonalcheck(Stone, Direction, int, int, boolean, List, List)}, ob ein gegnerischer Stein geschlagen werden kann und ruft sich dann mit der
     * neuen Position (X,Y) wieder rekursiv auf. Dann wird ausgehen von der neuen Position wieder Diagonalchecks durchgeführt
     *
     * @see #diagonalcheck(Stone, Direction, int, int, boolean, List, List)
     * @param s aktueller Stein
     * @param x aktuelle X-Koordinate
     * @param y aktuelle Y-Koordinate
     * @param zuglaenge bisherige Zuglänge des Zuges
     * @param ersterDurchgang ist dies der erste Aufruf (nicht Rekursionsfall)
     * @param skipped enthält alle gegnerischen Felder, die in diesem Zug bereits rausgeworfen wurden
     * @param entered enthält alle Felder, auf denen der eigene Stein kurz aufkommt
     */
    private void ki(Stone s, int x, int y, int zuglaenge, boolean ersterDurchgang, List<Field> skipped, List<Field> entered) {

        // ist dies der erste Durchgang, wird das Startfeld hinzugefügt
        if (ersterDurchgang) {
            entered.add(Main.playingField.getField(x, y));
        }

        // skipped und entered Listen werden für die jeweiligen Richtungen kopiert
        List<Field> skippedLeftDown = new ArrayList<>(skipped);
        List<Field> enteredLeftDown = new ArrayList<>(entered);

        List<Field> skippedRightDown = new ArrayList<>(skipped);
        List<Field> enteredRightDown = new ArrayList<>(entered);

        List<Field> skippedLeftUp = new ArrayList<>(skipped);
        List<Field> enteredLeftUp = new ArrayList<>(entered);

        List<Field> skippedRightUp = new ArrayList<>(skipped);
        List<Field> enteredRightUp = new ArrayList<>(entered);

        int a;
        if ((a = diagonalcheck(s, Direction.RIGHTDOWN, x, y, ersterDurchgang, skippedLeftDown, enteredLeftDown)) > 1) {
            /*  KI wird rekursiv aufgerufen, wenn ein gegnerischer Stein übersprungen werden kann (Rückgabewert von diagonalcheck > 1), um zu schauen ob ein weiterer
                gegnerischer Stein von der neuen Position aus erreichbar ist */
            ki(s, x + a, y - a, zuglaenge + a, false, new ArrayList<Field>(skippedLeftDown), new ArrayList<Field>(enteredLeftDown));
        } else {
            if (zuglaenge + a > 0) {
                Moves z = new Moves(zuglaenge + a, s);
                z.addEnterField(enteredLeftDown);
                z.addSkipField(skippedLeftDown);
                allMoves.add(z);
            }
        }

        if ((a = diagonalcheck(s, Direction.LEFTDOWN, x, y, ersterDurchgang, skippedRightDown, enteredRightDown)) > 1) {
            ki(s, x - a, y - a, zuglaenge + a, false, new ArrayList<Field>(skippedRightDown), new ArrayList<Field>(enteredRightDown));
        } else {
            if (zuglaenge + a > 0) {
                Moves z = new Moves(zuglaenge + a, s);
                z.addEnterField(enteredRightDown);
                z.addSkipField(skippedRightDown);
                allMoves.add(z);
            }
        }

        if ((a = diagonalcheck(s, Direction.RIGHTUP, x, y, ersterDurchgang, skippedLeftUp, enteredLeftUp)) > 1) {
            ki(s, x + a, y + a, zuglaenge + a, false, new ArrayList<Field>(skippedLeftUp), new ArrayList<Field>(enteredLeftUp));
        } else {
            if (zuglaenge + a > 0) {
                Moves z = new Moves(zuglaenge + a, s);
                z.addEnterField(enteredLeftUp);
                z.addSkipField(skippedLeftUp);
                allMoves.add(z);
            }
        }

        if ((a = diagonalcheck(s, Direction.LEFTUP, x, y, ersterDurchgang, skippedRightUp, enteredRightUp)) > 1) {
            ki(s, x - a, y + a, zuglaenge + a, false, new ArrayList<Field>(skippedRightUp), new ArrayList<Field>(enteredRightUp));
        } else {
            if (zuglaenge + a > 0) {
                Moves z = new Moves(zuglaenge + a, s);
                z.addEnterField(enteredRightUp);
                z.addSkipField(skippedRightUp);
                allMoves.add(z);
            }
        }
    }
}