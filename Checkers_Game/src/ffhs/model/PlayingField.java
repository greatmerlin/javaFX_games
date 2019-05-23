package ffhs.model;

public class PlayingField {
    private int FieldSize;
    private Field cField[];

    public PlayingField() {}

    public PlayingField(int size){
        FieldSize = size;
        createField();
    }

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
                    temp = new Field(Color.RED, i, j);
                }
                cField[i * FieldSize + j] = temp;
                black = !black;
            }
        }
    }

    public void rebuild(int fieldSize) {
        this.FieldSize = fieldSize;
        createField();
    }

    public Field[] getcField(){
        return cField;
    }

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
