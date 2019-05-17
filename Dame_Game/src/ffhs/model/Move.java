package ffhs.model;

import java.util.List;
import java.util.Vector;

/**
 * saves a turn from a token. Which fields will be used and which can be jumped, when an opponent ist there.
 */
public class Move {

    private Stone stone;
    private Vector<Field> enteredFields;
    private Vector<Field> skippedFields;
    private int index;
    private boolean outdated;

    /**
     * Basic Constructor.
     * initialize the Lists.
     */
    public Move() {
        enteredFields = new Vector<>();
        skippedFields = new Vector<>();
        outdated = true;
    }

    /**
     * initializes the Move same with a token.
     *
     * @param s chosen token.
     */
    public Move(Stone s) {
        this();
        stone = s;
        index = 1;
        outdated = false;
    }

    /**
     * initializes the Move with a token.
     * the data known until now will be ignored.
     *
     * @param s chosen token.
     */
    public void init(Stone s) {
        stone = s;
        enteredFields.clear();
        skippedFields.clear();
        index = 1;
        outdated = false;
    }

    /**
     * set the Flag {@link #outdated}.
     * This Flag means, that the move is OLD.
     * That way, the token can be chosen once again.
     *
     * @param outdated Flag is old
     * @see #isOutdated()
     */
    public void setOutdated(boolean outdated) {
        this.outdated = outdated;
    }

    /**
     * returns the Flag {@link #outdated}.
     *
     * @return Flag old
     * @see #setOutdated(boolean)
     */
    public boolean isOutdated() {
        return outdated;
    }

    /**
     * This token will be chosen for the move.
     *
     * @return chosen token
     */
    public Stone getStone() {
        return stone;
    }

    /**
     * Returns the first field from the visited fields.
     * It is the field that the token was at the start.
     * Returns {@code null} , if the list is empty.
     *
     * @return Startfeld, auf dem der Stein anfangs lag.
     */
    public Field getFirstField() {
        if (enteredFields != null && !enteredFields.isEmpty()) {
            return enteredFields.get(0);
        }
        return null;
    }

    /**
     * Returns the last visited field.
     *
     * @return End-Field from the token.
     */
    public Field getEndField() {
        if (enteredFields != null && !enteredFields.isEmpty()) {
            return enteredFields.get(enteredFields.size() - 1);
        }
        return null;
    }

    /**
     * Returns the pre-last field.
     * The token that got hit, shoudl be between the Last-Field and the End-field.
     *
     * @return pre-last Field.
     * @see ffhs.controller.Game #selectField(Field)
     */
    public Field getLastField() {
        if (enteredFields != null && !enteredFields.isEmpty() && enteredFields.size() > 1) {
            return enteredFields.get(enteredFields.size() - 2);
        }
        return null;
    }

    /**
     * returns the index of the Field.
     * At this field will be moved the token. When it is there, the  {@link #index} will be increased.
     *
     * @return the field, where the token should be moved to.
     * @see #nextField()
     */
    public Field getNextField() {
        return enteredFields.get(index);
    }

    /**
     * Returns the field, in which the token was until now ({@link #index} - 1)
     *
     * @return origin Field.
     */
    public Field getCurrentField() {
        return enteredFields.get(index - 1);
    }

    /**
     * increases the {@link #index}.
     * after the index increase, the  {@code true} will be returns. Otherwise {@code false}.
     *
     * @return Flag, if there is a next field.
     */
    public boolean nextField() {
        if (index < enteredFields.size() - 1) {
            index++;
            return true;
        }
        return false;
    }

    /**
     * adds a field to the visited (entered) fields. ({@link #enteredFields}).
     *
     * @param f Field, that the player chose and visited(entered).
     */
    public void addEnterField(Field f) {
        enteredFields.add(f);
    }

    /**
     * adds a field to the jumped(skipped) fields ({@link #skippedFields}).
     * In this field an opponent should be.
     * The opponent's token must also be removed.
     *
     * @param f skipped field with a token.
     */
    public void addSkipField(List<Field> f) {
        skippedFields.addAll(f);
    }

    /**
     * adds a list from the visited fields.
     *
     * @param f List with fields, which the token should visit.
     */
    public void addEnterField(List<Field> f) {
        enteredFields.addAll(f);
    }

    /**
     * adds the visited fields.
     * here should be an opponent.
     * the token from the opponent should be removed.
     *
     * @param f lsit with fields, that skipped an opponent(token) and are there.
     */
    public void addSkipField(Field f) {
        skippedFields.add(f);
    }

    /**
     * returns a list with all visited fields.
     *
     * @return list with fields that the tokens have visited.
     */
    public List<Field> getEnteredFields() {
        return enteredFields;
    }

    /**
     * returns a list with the skipped fields.
     *
     * @return returns a list with the skipped fields
     */
    public List<Field> getSkipedFields() {
        return skippedFields;
    }

    /**
     * returns the first skipped token.
     * The next field that will be skipped.
     *
     * @return the next field that will be skipped.
     * @see #nextSkipedField()
     */
    public Field getFirstSkipedField() {
        if (!skippedFields.isEmpty()) {
            return skippedFields.get(0);
        }
        return null;
    }

    /**
     * removes the first skipped field.
     * it will be activated when this field was skipped.
     * The next field will be named 'FirstSkippedField'
     *
     * @see #getFirstSkipedField()
     */
    public void nextSkipedField() {
        if (!skippedFields.isEmpty()) {
            skippedFields.remove(0);
        }
    }

    /**
     * updatet the right coordidates of the token.
     * This will be done after the end of the move.
     * Set the coordinates of the token to the end-Field coordinates.
     */
    public void update() {
        stone.setIndexX(getEndField().getIndexX());
        stone.setIndexY(getEndField().getIndexY());
    }

}
