package ffhs.model;

import java.util.*;

/**
 * This is a class about the possible CrossedFields in the Field.
 */

public class CrossedFields extends Move{
    private int turnDuration;

    public CrossedFields(int turnDuration, Token s){
        super(s);
        this.turnDuration = turnDuration;
    }

    private int getCrossedFields(){return turnDuration;}

    /**
     * gives information for the Directions of a turn
     */
    public String toString(){
        return "Duration: " + turnDuration + "; fromPos: " + super.getFirstField().getIndexX() + ", " + super.getFirstField().getIndexY() +
                " toPos: " + super.getEndField().getIndexX() + ", " + super.getEndField().getIndexY();
    }

    /**
     * returns information from a turn
     */
    public void print(){
        System.out.println("Entered Fields:");
        for(Field f : getEnteredFields()){
            System.out.println(f.getIndexX() + ", " + f.getIndexY());
        }
        System.out.println("Skipped Fields:");
        for(Field f : getSkipedFields()){
            System.out.println(f.getIndexX() + ", " + f.getIndexY());
        }
        System.out.println(toString());
    }

    /**
     * From a possible moves list,  a random turn with maximum duration will be chosen.
     * @param turns list with possible turns.
     * @return random turn with maximum duration.
     */
    public static CrossedFields getBestTurn(List<CrossedFields> turns){
        if(turns != null) {
            if (turns.size() > 0) {
                //the highest duration turns will be searched and a random move will be chosen.
                int max = turns.get(0).getCrossedFields();
                List<CrossedFields> crossedFields = new ArrayList<>();

                for (CrossedFields z : turns) {
                    if (max < z.getCrossedFields()) {
                        max = z.getCrossedFields();
                    }
                }
                for (CrossedFields z : turns) {
                    if (max == z.getCrossedFields()) {
                        crossedFields.add(z);
                    }
                }
                return crossedFields.get(new Random().nextInt(crossedFields.size()));
            }
        }
        return null;
    }
}