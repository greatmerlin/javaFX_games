package ffhs.model;

import javafx.scene.Node;
import javafx.scene.shape.Circle;

/**
 * saves the data for a token
 * A token consists of: Color, Coordinates,
 * superDame (yes/no), Status (eliminated yes/no),
 * and Circle.
 */
public class Stone {

    private int indexX , indexY;
    private boolean eliminated;
    private Color cColor;
    private boolean superDame;
    private Node cCirc;

    public Stone(Color c, int x, int y, boolean superD){
        cColor = c;
        indexX = x;
        indexY = y;
        superDame = superD;
        eliminated = false;
        cCirc = new Circle();
    }

    public Node getcCirc(){
        return cCirc;
    }

    /**
     * Circle to Node
     * differentiate normal tokens from the "queen"(= superDame)
     *
     * @param n new graphical token
     */
    public void changeNode(Node n) {
        cCirc = n;
    }
    public Color getColor(){
        return cColor;
    }
    public boolean isSuperDame(){
        return superDame;
    }
    public void setSuperDame(){
        superDame = true;
    }
    public int getIndexX(){
        return indexX;
    }
    public int getIndexY(){
        return indexY;
    }
    public void setIndexX(int x){
        indexX = x;
    }
    public void setIndexY(int y){
        indexY = y;
    }

    public boolean isEliminated() {
        return eliminated;
    }

    public void setEliminated() {
        eliminated = true;
    }

}
