package sample;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

import static sample.Dame_App.FELD_SIZE;

public class Stein extends StackPane {

    private SteinType type;

    private double mouseX,mouseY;
    private double oldX, oldY; // so that we can do the move

    public SteinType getType() {
        return type;
    }

    public double getOldX() {
        return oldX;
    }

    public double getOldY() {
        return oldY;
    }

    public Stein(SteinType type, int x, int y){  // we take the type and the position

            this.type = type;

            //relocate the whole stackPane

        move(x,y);

            //create our Ellipsis (Circle-shaped piece)

        Ellipse bg = new Ellipse(FELD_SIZE * 0.3125, FELD_SIZE * 0.26);   // bg = background, also import the FELD_SIZE from Dame_Spiel (radius taken from wiki)
        bg.setFill(Color.BLACK);

        bg.setStroke(Color.BLACK);
        bg.setStrokeWidth(FELD_SIZE * 0.03);

        //we need to be centered ( * 2 for the Diameter) or it's only the radius X. The whole devide by 2 because we want to move only the half of what we get
        bg.setTranslateX((FELD_SIZE - (FELD_SIZE * 0.3125 * 2))/2);
        bg.setTranslateY((FELD_SIZE - (FELD_SIZE * 0.26 * 2))/2 + FELD_SIZE * 0.07); // same for y but with the y radius | * 0.07 for a slight diff with the black background

        // to make a shade behind the Piece and make it like an illusion
        Ellipse ellipse = new Ellipse(FELD_SIZE * 0.3125, FELD_SIZE * 0.26);   // bg = background, also import the FELD_SIZE from Dame_Spiel (radius taken from wiki)
        ellipse.setFill(type == SteinType.RED ? Color.valueOf("#c40003") : Color.valueOf("#fff9f4"));

        ellipse.setStroke(Color.BLACK);
        ellipse.setStrokeWidth(FELD_SIZE * 0.03);

        //we need to be centered ( * 2 for the Diameter) or it's only the radius X. The whole devide by 2 because we want to move only the half of what we get
        ellipse.setTranslateX((FELD_SIZE - (FELD_SIZE * 0.3125 * 2))/2);
        ellipse.setTranslateY((FELD_SIZE - (FELD_SIZE * 0.26 * 2))/2); // same for y but with the y radius

        getChildren().addAll(bg, ellipse); // background first and then ellipse

        //add the event

        setOnMousePressed(e -> {

            mouseX = e.getSceneX();   // how we remember the movement
            mouseY = e.getScreenY();

        });

        setOnMouseDragged(e -> {

            relocate(e.getSceneX() - mouseX + oldX, e.getSceneY() - mouseY + oldY); // now we will be able to release it
        });
        }

        public void move(int x,int y){

            oldX = x * FELD_SIZE;
            oldY = y * FELD_SIZE;
            relocate(oldX,oldY);
        }

        public void abortMove(){    // a move to go back to the old position

            relocate(oldX, oldY);
        }
}
