package ffhs.model;

import javafx.scene.Node;

/**
 * saves a players data
 * The player tokens belong here, the Color, the tokens and the names.
 */

public class Player {

    /**
     * all the tokens that belong to thee player
     */
    private Stone cStone[];

    /**
     * the Name of the Player
     */
    private String cName;

    /**
     * Color of the player's tokens
     */
    private Color cColor;

    /**
     * Basic Constructor
     */
    public Player() {}

    /**
     * Constructor places Color and Player names.
     *
     * @param c token's color.
     * @param name player's name.
     */
    public Player(Color c, String name) {
        cName = name;
        cColor = c;
    }

    /**
     * Constructor places Color and Names and generates the same tokens for both players.
     *
     * @param c Farbe der Steine.
     * @param name Name des Spielers.
     * @param size Größe des Spielfelds, um alle Steine zu generieren.
     */
    public Player(Color c, String name, int size) {
        this(c, name);
        createStones();
    }

    /**
     * initializes the players with Colors, Names and tokens
     *
     * @param name Player's name
     * @param c tokens Color.
     */
    private void init(String name, Color c) {
        cName = name;
        cColor = c;
        createStones();
    }

    /**
     * creates a playing field with white and black tokens.
     */
    private void createStones() {
        int size= 10;
        int x, y;
        if (cColor == Color.BLACK){
            x = 0;
            y = 0;
        }
        else {
            y = size - (size / 2 - 1);
            if (y % 2 != 0) {
                x = 1;
            }
            else {
                x = 0;
            }
        }
        int f = (int)((((double)size/4) - 0.5) * size);            //Anzahl Spielsteine pro Spieler abhängig von Feldgröße
        cStone = new Stone[f];
        for (int i = 0; i < f; i++){
            cStone[i] = new Stone(cColor, x, y, false);
            if (( x += 2) >= size){
                y += 1;
                if((y % 2) != 0){
                    x = 1;
                }
                else {
                    x = 0;
                }
            }
        }
    }

    /**
     * moves the token to another place in the playing field
     *
     * @param indexStone token's number
     * @param x x-coordinate token
     * @param y y-coordinate token
     */
    public void replaceStone (int indexStone, int x, int y){
        cStone[indexStone].setIndexX(x);
        cStone[indexStone].setIndexY(y);
    }

    public Stone[] getStones() {
        return cStone;
    }

    /**
     * check how many tokens are still active
     *
     * @return number of the active tokens (not hit)
     */
    public int getActiveStones() {
        int value = 0;
        for (Stone s : getStones()) {
            if (!s.isEliminated()) {
                value++;
            }
        }
        return value;
    }

    public String getName(){
        return cName;
    }

    public Color getColor() {
        return cColor;
    }

    public Color getEnemyColor(){
        if (cColor.equals(Color.BLACK)){
            return Color.WHITE;
        }
        return Color.BLACK;
    }

    /**
     * Searches the eligible token, for the clicked Node.
     *
     * @param n clicked Circle
     * @return token at that Node
     */
    public Stone getStoneOfClickedCircle(Node n) {
        for (Stone s : cStone) {
            if (s.getcCirc().equals(n)) {
                return s;
            }
        }
        return null;
    }

    /**
     *returns if the player has a token with th fitting coordinates.
     *
     * @param x x-Coordinate.
     * @param y y-Coordinate.
     * @return {@code true}, if the player has a token with these coordinates
     */
    public boolean hasStoneAt(int x, int y) {
        for (Stone s : cStone) {
            if (s.getIndexX() == x && s.getIndexY() == y && !s.isEliminated()) {
                return true;
            }
        }
        return false;
    }

    /**
     * returns the token with the fitting coordinates
     *
     * @param x x-Coordinate.
     * @param y y-Coordinate.
     * @return token with the fitting coordinates.
     * @see #hasStoneAt(int, int)
     */
    public Stone getStoneAt(int x, int y) {
        for (Stone s : cStone) {
            if (s.getIndexX() == x && s.getIndexY() == y && !s.isEliminated()) {
                return s;
            }
        }
        return null;
    }



}
