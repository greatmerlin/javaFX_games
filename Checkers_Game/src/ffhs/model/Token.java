package ffhs.model;

import javafx.scene.Node;
import javafx.scene.shape.Circle;

public class Token {

    private int indexX , indexY;
    private boolean eliminated;
    private Color circleColor;
    private boolean king;
    private Node nodeCircle;

    public Token(Color c, int x, int y, boolean king){
        circleColor = c;
        indexX = x;
        indexY = y;
        this.king = king;
        eliminated = false;
        nodeCircle = new Circle();
    }

    public Node getNodeCircle(){
        return nodeCircle;
    }

    public void changeNode(Node n) {
        nodeCircle = n;
    }

    public Color getColor(){
        return circleColor;
    }

    public boolean isKing(){
        return king;
    }

    public void setKing(){
        king = true;
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
