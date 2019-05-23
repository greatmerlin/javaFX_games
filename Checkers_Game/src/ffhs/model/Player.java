package ffhs.model;

import javafx.scene.Node;

public class Player {

    private Token cToken[];
    private String cName;
    private Color cColor;

    public Player() {}

    public Player(Color c, String name) {
        cName = name;
        cColor = c;
    }

    public Player(Color c, String name, int size) {
        this(c, name);
        createTokens(size);
    }

    private void init(String name, int size, Color c) {
        cName = name;
        cColor = c;
        createTokens(size);
    }

    private void createTokens(int size) {
        int x, y;
        if (cColor == Color.BLACK) {
            x = 0;
            y = 0;
        } else {
            y = size - (size / 2 - 1);
            if (y % 2 != 0) {
                x = 1;
            } else {
                x = 0;
            }
        }
        int f = (int) ((((double) size / 4) - 0.5) * size);
        cToken = new Token[f];
        for (int i = 0; i < f; i++) {
            cToken[i] = new Token(cColor, x, y, false);
            if ((x += 2) >= size) {
                y += 1;
                if ((y % 2) != 0) {
                    x = 1;
                } else {
                    x = 0;
                }
            }
        }
    }

        public void replaceStone (int indexStone, int x, int y) {
            cToken[indexStone].setIndexX(x);
            cToken[indexStone].setIndexY(y);
        }

        public Token[] getTokens() {
            return cToken;
        }

    public int getActiveStones() {
        int value = 0;
        for (Token s : getTokens()) {
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
            return Color.RED;
        }
        return Color.BLACK;
    }

    public Token getStoneOfClickedCircle(Node n) {
        for (Token s : cToken) {
            if (s.getNodeCircle().equals(n)) {
                return s;
            }
        }
        return null;
    }

    public boolean hasTokenAt(int x, int y) {
        for (Token s : cToken) {
            if (s.getIndexX() == x && s.getIndexY() == y && !s.isEliminated()) {
                return true;
            }
        }
        return false;
    }

    public Token getTokenAt(int x, int y) {
        for (Token s : cToken) {
            if (s.getIndexX() == x && s.getIndexY() == y && !s.isEliminated()) {
                return s;
            }
        }
        return null;
    }
}
