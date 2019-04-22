package sample;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Feld extends Rectangle {  // Feld = Tile (EN)

    private Stein stein;

    public boolean hasStein() {  // check if it's not null
        return stein != null;
    }

    public Stein getStein() {
        return stein;
    }

    public void setStein(Stein stein) {
        this.stein = stein;
    }

    public Feld(boolean light, int x, int y){  //ein Feld erzeugen

        setWidth(Dame_App.FELD_SIZE);
        setHeight(Dame_App.FELD_SIZE);

        relocate(x * Dame_App.FELD_SIZE, y * Dame_App.FELD_SIZE);

        setFill(light ? Color.valueOf("#feb") : Color.valueOf("#582"));

    }

}
