package ffhs.model;

import ffhs.controller.*;

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
     * Lists of all the possible moves, that can be done from the AI.
     */
    private List<CrossedFields> allMoves;

    public AI(Color c, String name, int size, Player enemy) {
        super(c, name, size);   //Contructor call in Player
        this.enemy = enemy;
    }

    /**
     * If a turn from the AI will is being waited, this Method will be called and returns a possible move.
     *
     * @throws NoPossibleMoveException The AI can't do any moves.
     * @return Zugfolge
     */
    public Move getBestMove() throws NoPossibleMoveException{
        allMoves = new ArrayList<>();

        //A possible move for every not eliminated token (AI is a recursive Method)
        for (Stone s : getStones()) {
            if (!s.isEliminated()) {
                ki(s, s.getIndexX(), s.getIndexY(), 0, true, new ArrayList<Field>(), new ArrayList<Field>());
            }
        }

        if(allMoves.size() == 0){
            throw new NoPossibleMoveException();
        }
        return CrossedFields.getBestZug(allMoves);
    }

    /**
     * From the  X and Y coordinates will be investigated which are the possible moves to be made.
     * normal tokens can move: (RIGHTDOWN, LEFTDOWN), The KING/Queen can go in every direction.
     * (RIGHTDOWN, LEFTDOWN, RIGHTUP, LEFTUP) and more than one field at once.
     *
     * @see #ki(Stone, int, int, int, boolean, List, List)
     * @param s token for this turn
     * @param d direction, in which there is danger
     * @param x actuell X-coordinate
     * @param y actuell Y-coordinate
     * @param firstCall is the first call(recursive not called yet)
     * @param skipped has all the opponent-Fields, that were eliminated in this round
     * @param entered has all the Fields, in which the token enters.
     * @return the number of cross fields of X and Y
     */
    private int diagonalcheck(Stone s, Direction d, int x, int y, boolean firstCall, List<Field> skipped, List<Field> entered) {

        // possible turn right down
        if (d.equals(Direction.RIGHTDOWN)) {
            /*  If s is a KING/Queen, the for-loop will be used many times to find a token which is far from the current field,
                otherwise check where a nearby opponent is */
            for(int i = 0; i < (s.isSuperDame() ?  (ffhs.controller.Main.playingField.getSize()-2) : 1); i++) {

                // The moment an opponent is on the way it stops or when an opponent is behind this field.
                if(ffhs.controller.Main.playingField.isPositionInsideField(x + i + 1, y - i - 1) && hasStoneAt(x + i + 1, y - i - 1) ||
                        enemy.hasStoneAt(x + i + 1, y - i - 1) && (enemy.hasStoneAt(x + i + 2, y - i - 2) || hasStoneAt(x + i + 2, y - i - 2))) {
                    return 0;
                }

                /*  the next 2 diagonal fields, if the skipped field wasn't visited the opponent should be empty*/
                if (Main.playingField.isPositionInsideField(x + i + 2, y - i - 2) && Main.playingField.isPositionInsideField(x + i + 1, y - i - 1)
                        && !skipped.contains(Main.playingField.getField(x + i + 1, y - i - 1))
                        && enemy.hasStoneAt(x + i + 1, y - i - 1)) {
                    //skipped token
                    skipped.add(Main.playingField.getField(x + i + 1, y - i - 1));
                    //see again if there are other possible moves to be done
                    entered.add(Main.playingField.getField(x + i + 2, y - i - 2));
                    //save these scores to be used later.
                    return 2+i;
                }

            }
            /*  if the next diagonal field is empty, it can be used */
            if (firstCall && Main.playingField.isPositionInsideField(x + 1, y - 1) && !(enemy.hasStoneAt(x + 1, y - 1) || hasStoneAt(x + 1, y - 1))) {
                entered.add(Main.playingField.getField(x + 1, y - 1));
                return 1;
            }
        }
        /*  the decision for the move*/
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
            if (firstCall && Main.playingField.isPositionInsideField(x - 1, y - 1) && !(enemy.hasStoneAt(x - 1, y - 1) || hasStoneAt(x - 1, y - 1))) {
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
            if (firstCall && Main.playingField.isPositionInsideField(x + 1, y + 1) && !(enemy.hasStoneAt(x + 1, y + 1) || hasStoneAt(x + 1, y + 1))) {
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
            if (firstCall && Main.playingField.isPositionInsideField(x - 1, y + 1) && !(enemy.hasStoneAt(x - 1, y + 1) || hasStoneAt(x - 1, y + 1))) {
                entered.add(Main.playingField.getField(x - 1, y + 1));
                return 1;
            }
        }
        return 0;
    }

    /**
     * The AI Methods determine {@link #diagonalcheck(Stone, Direction, int, int, boolean, List, List)}, if an opponent's token can be hit(skipped) and calls
     * itself in the new coordinates(X,Y) again recursive. Then again from the new position diagonal.
     *
     * @see #diagonalcheck(Stone, Direction, int, int, boolean, List, List)
     * @param s actuell token
     * @param x actuell X-coordinate
     * @param y actuell Y-coordinate
     * @param crossedFields crossedFields until now
     * @param firstCall the first call (not recursive)
     * @param skipped the skipped fields of this turn
     * @param entered the entered fields
     */
    private void ki(Stone s, int x, int y, int crossedFields, boolean firstCall, List<Field> skipped, List<Field> entered) {

        // if it is the first path(entry) the Startfeld will be added
        if (firstCall) {
            entered.add(Main.playingField.getField(x, y));
        }

        // skipped und entered Lists were copied for all the directions.
        List<Field> skippedLeftDown = new ArrayList<>(skipped);
        List<Field> enteredLeftDown = new ArrayList<>(entered);

        List<Field> skippedRightDown = new ArrayList<>(skipped);
        List<Field> enteredRightDown = new ArrayList<>(entered);

        List<Field> skippedLeftUp = new ArrayList<>(skipped);
        List<Field> enteredLeftUp = new ArrayList<>(entered);

        List<Field> skippedRightUp = new ArrayList<>(skipped);
        List<Field> enteredRightUp = new ArrayList<>(entered);

        int a;
        if ((a = diagonalcheck(s, Direction.RIGHTDOWN, x, y, firstCall, skippedLeftDown, enteredLeftDown)) > 1) {
            /*  recursive AI call, if an opponent is skipped (hit) to see if there is another one to continue doing so */
            ki(s, x + a, y - a, crossedFields + a, false, new ArrayList<Field>(skippedLeftDown), new ArrayList<Field>(enteredLeftDown));
        } else {
            if (crossedFields + a > 0) {
                CrossedFields z = new CrossedFields(crossedFields + a, s);
                z.addEnterField(enteredLeftDown);
                z.addSkipField(skippedLeftDown);
                allMoves.add(z);
            }
        }

        if ((a = diagonalcheck(s, Direction.LEFTDOWN, x, y, firstCall, skippedRightDown, enteredRightDown)) > 1) {
            ki(s, x - a, y - a, crossedFields + a, false, new ArrayList<Field>(skippedRightDown), new ArrayList<Field>(enteredRightDown));
        } else {
            if (crossedFields + a > 0) {
                CrossedFields z = new CrossedFields(crossedFields + a, s);
                z.addEnterField(enteredRightDown);
                z.addSkipField(skippedRightDown);
                allMoves.add(z);
            }
        }

        if ((a = diagonalcheck(s, Direction.RIGHTUP, x, y, firstCall, skippedLeftUp, enteredLeftUp)) > 1) {
            ki(s, x + a, y + a, crossedFields + a, false, new ArrayList<Field>(skippedLeftUp), new ArrayList<Field>(enteredLeftUp));
        } else {
            if (crossedFields + a > 0) {
                CrossedFields z = new CrossedFields(crossedFields + a, s);
                z.addEnterField(enteredLeftUp);
                z.addSkipField(skippedLeftUp);
                allMoves.add(z);
            }
        }

        if ((a = diagonalcheck(s, Direction.LEFTUP, x, y, firstCall, skippedRightUp, enteredRightUp)) > 1) {
            ki(s, x - a, y + a, crossedFields + a, false, new ArrayList<Field>(skippedRightUp), new ArrayList<Field>(enteredRightUp));
        } else {
            if (crossedFields + a > 0) {
                CrossedFields z = new CrossedFields(crossedFields + a, s);
                z.addEnterField(enteredRightUp);
                z.addSkipField(skippedRightUp);
                allMoves.add(z);
            }
        }
    }
}