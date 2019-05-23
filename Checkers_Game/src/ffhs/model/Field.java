package ffhs.model;

import javafx.scene.shape.Rectangle;

public class Field {

    private Color cColor;
    private int indexX, indexY;
    private Rectangle cRec;

    public Field(Color c, int x, int y){
        cColor = c;
        indexX = x;
        indexY = y;
    }

    public Rectangle getcRec(){
        return cRec;
    }

    public void setcRec(Rectangle temp) {
        cRec = temp;
    }

    public int getIndexX(){
        return indexX;
    }

    public int getIndexY(){
        return indexY;
    }

    public Color getcColor(){
        return cColor;
    }
}