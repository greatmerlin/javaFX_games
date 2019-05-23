package ffhs.model;

import java.util.*;

public class CrossedFields extends Move{
    private int turnDuration;

    public CrossedFields(int turnDuration, Token s){
        super(s);
        this.turnDuration = turnDuration;
    }

    private int getCrossedFields(){return turnDuration;}

    public String toString(){
        return "Duration: " + turnDuration + "; fromPos: " + super.getFirstField().getIndexX() + ", " + super.getFirstField().getIndexY() +
                " toPos: " + super.getEndField().getIndexX() + ", " + super.getEndField().getIndexY();
    }

    public static CrossedFields getBestTurn(List<CrossedFields> turns){
        if(turns != null) {
            if (turns.size() > 0) {
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