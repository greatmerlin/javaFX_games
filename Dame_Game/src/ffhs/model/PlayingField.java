package ffhs.model;

/**
 * speichert Daten für das ganze Spielfeld
 * Spielfeld hat eine Größe
 *
 * @author Mareike Giek
 */
public class PlayingField {
    private int FieldSize;
    private Field cField[];

    public PlayingField() {}

    public PlayingField(int size){
        FieldSize = size;
        createField();
    }

    /**
     * generiert ein Spielfeld, mit {@link #FieldSize} Feldern in einer Reihe.
     * Die einzelnen Felder bekommen abwechselnd die Farbe schwarz oder weiß zugeteilt.
     * Grafisch werden die Farben allerdings anders dargestellt, da man sonst die Steine nicht von den Feldern unterscheiden könnte.
     */
    private void createField(){
        cField = new Field[FieldSize*FieldSize];
        boolean black = false;
        for (int i = 0; i < FieldSize; i++){
            for (int j = 0; j < FieldSize; j++){
                Field temp;
                if (black) {
                    temp = new Field(Color.BLACK, i, j);
                }
                else{
                    temp = new Field(Color.WHITE, i, j);
                }
                cField[i * FieldSize + j] = temp;
                black = !black;
            }
        }
    }

    /**
     * generiert ein neues Spielfeld mit einer anderen Größe.
     *
     * @param fieldSize Größe des Spielfelds.
     */
    public void rebuild(int fieldSize) {
        this.FieldSize = fieldSize;
        createField();
    }

    public Field[] getcField(){
        return cField;
    }

    /**
     * gibt ein Feld mit bestimmten Koordinaten zurück.
     *
     * @param x x-Koordinate.
     * @param y y-Koordinate.
     * @return Feld mit den passenden Koordinaten.
     */
    public Field getField(int x, int y) {
        if (x * FieldSize + y >= 0 && x * FieldSize + y < FieldSize * FieldSize) {
            if (cField[x * FieldSize + y].getIndexX() == x && cField[x * FieldSize + y].getIndexY() == y) {
                return cField[x * FieldSize + y];
            }
            else {
                for (Field f : cField) {
                    if (f.getIndexX() == x && f.getIndexY() == y) {
                        return f;
                    }
                }
            }
        }
        return null;
    }

    public int getSize(){
        return FieldSize;
    }

    public boolean isPositionInsideField(int x, int y){
        return x >= 0 && x < FieldSize && y >= 0 && y < FieldSize;
    }
}
