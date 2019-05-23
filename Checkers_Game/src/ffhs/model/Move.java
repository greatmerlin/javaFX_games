package ffhs.model;

import java.util.List;
import java.util.Vector;

public class Move {

    private Token token;
    private Vector<Field> enteredFields;
    private Vector<Field> skippedFields;
    private int index;
    private boolean outdated;

    public Move() {
        enteredFields = new Vector<>();
        skippedFields = new Vector<>();
        outdated = true;
    }

    public Move(Token t) {
        this();
        token = t;
        index = 1;
        outdated = false;
    }

    public void init(Token t) {
        token = t;
        enteredFields.clear();
        skippedFields.clear();
        index = 1;
        outdated = false;
    }

    public void setOutdated(boolean outdated) {
        this.outdated = outdated;
    }

    public boolean isOutdated() {
        return outdated;
    }

    public Token getToken() {
        return token;
    }

    public Field getFirstField() {
        if (enteredFields != null && !enteredFields.isEmpty()) {
            return enteredFields.get(0);
        }
        return null;
    }

    public Field getEndField() {
        if (enteredFields != null && !enteredFields.isEmpty()) {
            return enteredFields.get(enteredFields.size() - 1);
        }
        return null;
    }

    public Field getLastField() {
        if (enteredFields != null && !enteredFields.isEmpty() && enteredFields.size() > 1) {
            return enteredFields.get(enteredFields.size() - 2);
        }
        return null;
    }

    public Field getNextField() {
        return enteredFields.get(index);
    }

    public Field getCurrentField() {
        return enteredFields.get(index - 1);
    }

    public boolean nextField() {
        if (index < enteredFields.size() - 1) {
            index++;
            return true;
        }
        return false;
    }

    public void addEnterField(Field f) {
        enteredFields.add(f);
    }

    public void addSkipField(List<Field> f) {
        skippedFields.addAll(f);
    }

    public void addEnterField(List<Field> f) {
        enteredFields.addAll(f);
    }

    public void addSkipField(Field f) {
        skippedFields.add(f);
    }

    public List<Field> getEnteredFields() {
        return enteredFields;
    }

    public List<Field> getSkipedFields() {
        return skippedFields;
    }

    public Field getFirstSkipedField() {
        if (!skippedFields.isEmpty()) {
            return skippedFields.get(0);
        }
        return null;
    }

    public void nextSkipedField() {
        if (!skippedFields.isEmpty()) {
            skippedFields.remove(0);
        }
    }

    public void update() {
        token.setIndexX(getEndField().getIndexX());
        token.setIndexY(getEndField().getIndexY());
    }

}
