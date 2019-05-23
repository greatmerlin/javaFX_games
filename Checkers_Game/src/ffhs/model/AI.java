package ffhs.model;

import ffhs.controller.*;

import java.util.ArrayList;
import java.util.List;

public class AI extends Player {

    private Player opponent;
    private List<CrossedFields> allMoves;

    public AI(Color c, String name, int size, Player opponent) {
        super(c, name, size);
        this.opponent = opponent;
    }

    public Move getBestMove() throws NoPossibleMoveException{
        allMoves = new ArrayList<>();

        for (Token t : getTokens()) {
            if (!t.isEliminated()) {
                ai(t, t.getIndexX(), t.getIndexY(), 0, true, new ArrayList<Field>(), new ArrayList<Field>());
            }
        }
        if(allMoves.size() == 0){
            throw new NoPossibleMoveException();
        }
        return CrossedFields.getBestTurn(allMoves);
    }

    private int diagonalcheck(Token t, Direction d, int x, int y, boolean firstCall, List<Field> skipped, List<Field> entered) {

        if (d.equals(Direction.RIGHTDOWN)) {
            for(int i = 0; i < (t.isKing() ?  (ffhs.controller.Main.playingField.getSize()-2) : 1); i++) {
                if(ffhs.controller.Main.playingField.isPositionInsideField(x + i + 1, y - i - 1) && hasTokenAt(x + i + 1, y - i - 1) ||
                        opponent.hasTokenAt(x + i + 1, y - i - 1) && (opponent.hasTokenAt(x + i + 2, y - i - 2) || hasTokenAt(x + i + 2, y - i - 2))) {
                    return 0;
                }
                if (Main.playingField.isPositionInsideField(x + i + 2, y - i - 2) && Main.playingField.isPositionInsideField(x + i + 1, y - i - 1)
                        && !skipped.contains(Main.playingField.getField(x + i + 1, y - i - 1))
                        && opponent.hasTokenAt(x + i + 1, y - i - 1)) {
                    skipped.add(Main.playingField.getField(x + i + 1, y - i - 1));
                    entered.add(Main.playingField.getField(x + i + 2, y - i - 2));
                    return 2+i;
                }
            }
            if (firstCall && Main.playingField.isPositionInsideField(x + 1, y - 1) && !(opponent.hasTokenAt(x + 1, y - 1) || hasTokenAt(x + 1, y - 1))) {
                entered.add(Main.playingField.getField(x + 1, y - 1));
                return 1;
            }
        }
        else if (d.equals(Direction.LEFTDOWN)) {
            for(int i = 0; i < (t.isKing() ?  (Main.playingField.getSize()-2) : 1); i++) {
                if(Main.playingField.isPositionInsideField(x - i - 1, y - i - 1) && hasTokenAt(x - i - 1, y - i - 1) ||
                        opponent.hasTokenAt(x - i - 1, y - i - 1) && (opponent.hasTokenAt(x - i - 2, y - i - 2) || hasTokenAt(x - i - 2, y - i - 2))) {
                    return 0;
                }
                if (Main.playingField.isPositionInsideField(x - i - 2, y - i - 2) && Main.playingField.isPositionInsideField(x - i - 1, y - i - 1)
                        && !skipped.contains(Main.playingField.getField(x - i - 1, y - i - 1))
                        && opponent.hasTokenAt(x - i - 1, y - i - 1)) {
                    skipped.add(Main.playingField.getField(x - i - 1, y - i - 1));
                    entered.add(Main.playingField.getField(x - i - 2, y - i - 2));
                    return 2+i;
                }
            }
            if (firstCall && Main.playingField.isPositionInsideField(x - 1, y - 1) && !(opponent.hasTokenAt(x - 1, y - 1) || hasTokenAt(x - 1, y - 1))) {
                entered.add(Main.playingField.getField(x - 1, y - 1));
                return 1;
            }
        } else if (t.isKing() && d.equals(Direction.RIGHTUP)) {
            for(int i = 0; i < (t.isKing() ?  (Main.playingField.getSize()-2) : 1); i++) {
                if(Main.playingField.isPositionInsideField(x + i + 1, y + i + 1) && hasTokenAt(x + i + 1, y + i + 1) ||
                        opponent.hasTokenAt(x + i + 1, y + i + 1) && (opponent.hasTokenAt(x + i + 2, y + i + 2) || hasTokenAt(x + i + 2, y + i + 2))) {
                    return 0;
                }
                if (Main.playingField.isPositionInsideField(x + i + 2, y + i + 2) && Main.playingField.isPositionInsideField(x + i + 1, y + i + 1)
                        && !skipped.contains(Main.playingField.getField(x + i + 1, y + i + 1))
                        && opponent.hasTokenAt(x + i + 1, y + i + 1)) {
                    skipped.add(Main.playingField.getField(x + i + 1, y + i + 1));
                    entered.add(Main.playingField.getField(x + i + 2, y + i + 2));
                    return 2+i;
                }
            }
            if (firstCall && Main.playingField.isPositionInsideField(x + 1, y + 1) && !(opponent.hasTokenAt(x + 1, y + 1) || hasTokenAt(x + 1, y + 1))) {
                entered.add(Main.playingField.getField(x + 1, y + 1));
                return 1;
            }
        } else if (t.isKing() && d.equals(Direction.LEFTUP)) {
            for(int i = 0; i < (t.isKing() ?  (Main.playingField.getSize()-2) : 1); i++) {
                if(Main.playingField.isPositionInsideField(x - i - 1, y + i + 1) && hasTokenAt(x - i - 1, y + i + 1) ||
                        opponent.hasTokenAt(x - i - 1, y + i + 1) && (opponent.hasTokenAt(x - i - 2, y + i + 2) || hasTokenAt(x - i - 2, y + i + 2))) {
                    return 0;
                }
                if (Main.playingField.isPositionInsideField(x - i - 2, y + i + 2) && Main.playingField.isPositionInsideField(x - i - 1, y + i + 1)
                        && !skipped.contains(Main.playingField.getField(x - i - 1, y + i + 1))
                        && opponent.hasTokenAt(x - i - 1, y + i + 1)) {
                    skipped.add(Main.playingField.getField(x - i - 1, y + i + 1));
                    entered.add(Main.playingField.getField(x - i - 2, y + i + 2));
                    return 2+i;
                }
            }
            if (firstCall && Main.playingField.isPositionInsideField(x - 1, y + 1) && !(opponent.hasTokenAt(x - 1, y + 1) || hasTokenAt(x - 1, y + 1))) {
                entered.add(Main.playingField.getField(x - 1, y + 1));
                return 1;
            }
        }
        return 0;
    }

    private void ai(Token t, int x, int y, int crossedFields, boolean firstCall, List<Field> skipped, List<Field> entered) {

        if (firstCall) {
            entered.add(Main.playingField.getField(x, y));
        }

        List<Field> skippedLeftDown = new ArrayList<>(skipped);
        List<Field> enteredLeftDown = new ArrayList<>(entered);

        List<Field> skippedRightDown = new ArrayList<>(skipped);
        List<Field> enteredRightDown = new ArrayList<>(entered);

        List<Field> skippedLeftUp = new ArrayList<>(skipped);
        List<Field> enteredLeftUp = new ArrayList<>(entered);

        List<Field> skippedRightUp = new ArrayList<>(skipped);
        List<Field> enteredRightUp = new ArrayList<>(entered);

        int a;
        if ((a = diagonalcheck(t, Direction.RIGHTDOWN, x, y, firstCall, skippedLeftDown, enteredLeftDown)) > 1) {
            ai(t, x + a, y - a, crossedFields + a, false, new ArrayList<Field>(skippedLeftDown), new ArrayList<Field>(enteredLeftDown));
        } else {
            if (crossedFields + a > 0) {
                CrossedFields z = new CrossedFields(crossedFields + a, t);
                z.addEnterField(enteredLeftDown);
                z.addSkipField(skippedLeftDown);
                allMoves.add(z);
            }
        }
        if ((a = diagonalcheck(t, Direction.LEFTDOWN, x, y, firstCall, skippedRightDown, enteredRightDown)) > 1) {
            ai(t, x - a, y - a, crossedFields + a, false, new ArrayList<Field>(skippedRightDown), new ArrayList<Field>(enteredRightDown));
        } else {
            if (crossedFields + a > 0) {
                CrossedFields z = new CrossedFields(crossedFields + a, t);
                z.addEnterField(enteredRightDown);
                z.addSkipField(skippedRightDown);
                allMoves.add(z);
            }
        }
        if ((a = diagonalcheck(t, Direction.RIGHTUP, x, y, firstCall, skippedLeftUp, enteredLeftUp)) > 1) {
            ai(t, x + a, y + a, crossedFields + a, false, new ArrayList<Field>(skippedLeftUp), new ArrayList<Field>(enteredLeftUp));
        } else {
            if (crossedFields + a > 0) {
                CrossedFields z = new CrossedFields(crossedFields + a, t);
                z.addEnterField(enteredLeftUp);
                z.addSkipField(skippedLeftUp);
                allMoves.add(z);
            }
        }
        if ((a = diagonalcheck(t, Direction.LEFTUP, x, y, firstCall, skippedRightUp, enteredRightUp)) > 1) {
            ai(t, x - a, y + a, crossedFields + a, false, new ArrayList<Field>(skippedRightUp), new ArrayList<Field>(enteredRightUp));
        } else {
            if (crossedFields + a > 0) {
                CrossedFields z = new CrossedFields(crossedFields + a, t);
                z.addEnterField(enteredRightUp);
                z.addSkipField(skippedRightUp);
                allMoves.add(z);
            }
        }
    }
}