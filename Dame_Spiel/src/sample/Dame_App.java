package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Dame_App extends Application {

    public static final int FELD_SIZE = 100;
    public static final int WIDTH = 10;
    public static final int HEIGHT = 10;

    //create our board
    private Feld[][] board = new Feld[WIDTH][HEIGHT];

    //groups are drawn on top of the Felder (Tiles)
    private Group feldGroup = new Group();
    private Group steinGroup = new Group();

    private Parent createContent(){

        Pane root = new Pane();
        root.setPrefSize(WIDTH * FELD_SIZE, HEIGHT * FELD_SIZE);
        root.getChildren().addAll(feldGroup,steinGroup);

        // create our Felder

        for(int y = 0; y < HEIGHT; y++){
            for (int x = 0; x < WIDTH; x++){
                Feld feld = new Feld((x + y) % 2 == 0, x, y); // pattern for the light(white) colored pieces (think of the board)
                board[x][y] = feld;

                feldGroup.getChildren().add(feld);

                //populate the Stein -> we want the dark Tiles(Felder)

                Stein stein = null;
                if(y <= 2 && (x + y) % 2 != 0) {
                    stein = makeStein(SteinType.RED, x, y); // the top
                }
                if(y >= 5 && (x + y) % 2 != 0) {
                    stein = makeStein(SteinType.WHITE, x, y); // the top
                }

                if(stein != null) {
                    feld.setStein(stein);
                    steinGroup.getChildren().add(stein);
                }
            }
        }

        return root;
    }

    private MoveResult tryMove(Stein stein, int newX, int newY){

        if(board[newX][newY].hasStein() || (newX + newY) % 2 == 0){  // we can't move on white Tiles because we start on black and we only move n black.

            return new MoveResult(MoveType.NONE);
        }
        //we want to know the difference between Tiles when we move. Because we can't move freely
        int x0 = toBoard(stein.getOldX());
        int y0 = toBoard(stein.getOldY());

        if(Math.abs(newX - x0) == 1 && newY - y0 == stein.getType().moveDir){
            return new MoveResult(MoveType.NORMAL); // this is where we can move normally
        } else if(Math.abs(newX - x0) == 2 && newY - y0 == stein.getType().moveDir * 2){   // 2, because it is 2 steps

            //also check if between is an enemy
            int x1 = x0 + (newX - x0) / 2;
            int y1 = y0 + (newY - y0) / 2;

            if(board[x1][y1].hasStein() && board[x1][y1].getStein().getType() != stein.getType()){

                return new MoveResult(MoveType.KILL, board[x1][y1].getStein());
            }
        }
        //after we have done all of that, we can't move
        return new MoveResult(MoveType.NONE);

    }

    // a Method that converts pixel coordinates to board coordinates

    private int toBoard(double pixel){

        return ((int)(pixel + FELD_SIZE / 2) / FELD_SIZE);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{

/*      Parent root = FXMLLoader.load(getClass().getResource("Brett.fxml"));

        primaryStage.setTitle("Dame_Spiel");
        primaryStage.setScene(new Scene(root, 1000, 1000));
        primaryStage.show();*/

        Scene scene = new Scene(createContent());

        primaryStage.setTitle("Dame_Spiel");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    // after we create the piece (Stein), we need to populate it

    private Stein makeStein(SteinType type, int x, int y){

        Stein stein = new Stein(type, x, y);

        stein.setOnMouseReleased(e -> {

            int newX = toBoard(stein.getLayoutX());   // because when we move it, it changes Layouts
            int newY = toBoard(stein.getLayoutY());

            MoveResult result = tryMove(stein, newX, newY);

            int x0 = toBoard(stein.getOldX());  // to clear the old Tile
            int y0 = toBoard(stein.getOldY());

            switch (result.getType()){
                case NONE:
                    stein.abortMove();
                case NORMAL:
                    stein.move(newX, newY);
                    board[x0][y0].setStein(null);
                    board[newX][newY].setStein(stein);
                    break;
                case KILL:
                    stein.move(newX, newY);
                    board[x0][y0].setStein(null);
                    board[newX][newY].setStein(stein);
                    //we also kill the other piece

                    Stein otherStein = result.getStein();
                    board[toBoard(otherStein.getOldX())][toBoard(otherStein.getOldY())].setStein(null); // we now destroy it from the screen aswell
                    steinGroup.getChildren().remove(otherStein);
                    break;
            }
        });

        return stein;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
