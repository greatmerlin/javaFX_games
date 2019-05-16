package ffhs.model;

import java.util.List;
import java.util.Vector;

/**
 * speichert einen Zug von einem Stein ab. Darunter, mit welchen Stein gezogen wird, welche Felder besucht (angewählt)
 * werden und welche Felder übersprungen werden, auf denen ein gegnerischer Stein liegt.
 *
 * @author Mareike Giek
 */
public class Move {

    private Stone stone;
    private Vector<Field> enteredFields;
    private Vector<Field> skipedFields;
    private int index;
    private boolean outdated;

    /**
     * Basic Constructor.
     * initialisiert die Listen.
     */
    public Move() {
        enteredFields = new Vector<>();
        skipedFields = new Vector<>();
        outdated = true;
    }

    /**
     * initialisiert den Move gleich mit einem Stein.
     *
     * @param s ausgewählter Stein.
     */
    public Move(Stone s) {
        this();
        stone = s;
        index = 1;
        outdated = false;
    }

    /**
     * initialisiert den Move mit einem Stein.
     * Bisherige Daten werden dabei überschrieben.
     *
     * @param s ausgewählter Stein.
     */
    public void init(Stone s) {
        stone = s;
        enteredFields.clear();
        skipedFields.clear();
        index = 1;
        outdated = false;
    }

    /**
     * setzt den Flag {@link #outdated}.
     * Dieser Flag gibt an, dass der Move veraltet ist.
     * Dadurch kann ein Stein wieder abgewählt werden.
     *
     * @param outdated Flag veraltet
     * @see #isOutdated()
     */
    public void setOutdated(boolean outdated) {
        this.outdated = outdated;
    }

    /**
     * gibt den Flag {@link #outdated} zurück.
     *
     * @return Flag veraltet
     * @see #setOutdated(boolean)
     */
    public boolean isOutdated() {
        return outdated;
    }

    /**
     * gibt den zu dem Move gehörenden Stein.
     * mit diesem Stein wird der gesamte Move gezogen.
     *
     * @return ausgewählter Stein.
     */
    public Stone getStone() {
        return stone;
    }

    /**
     * liefert das erste Feld der besuchten Felder.
     * <br><b>WICHTIG:</b> Muss das Feld sein, auf dem der Stein zu Anfang lag.
     * Gibt {@code null} zurück, wenn die Liste leer ist.
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
     * Gibt das letzte besuchte Feld zurück.
     * Das ist das Feld, auf dem der Stein zuletzt liegt.
     *
     * @return End-Feld des Steins.
     */
    public Field getEndField() {
        if (enteredFields != null && !enteredFields.isEmpty()) {
            return enteredFields.get(enteredFields.size() - 1);
        }
        return null;
    }

    /**
     * Gibt das vorletzte Feld zurück.
     * Sobald ein besuchtes Feld zu der Liste hinzugefügt wird, wird mit Hilfe dieses Feldes der übersprungene Stein ermittelt.
     * Der übersprungene Stein muss zwischen dem LastField und dem EndField liegen.
     *
     * @return Vorletztes Feld.
     * @see controller.Game#selectField(Field)
     */
    public Field getLastField() {
        if (enteredFields != null && !enteredFields.isEmpty() && enteredFields.size() > 1) {
            return enteredFields.get(enteredFields.size() - 2);
        }
        return null;
    }

    /**
     * Gibt das Feld zu dem index zurück.
     * Zu diesem Feld wird der Stein grafisch bewegt. Sobald der Stein angekommen ist, wird der {@link #index} erhöht.
     *
     * @return Feld zu dem sich der Stein bewegen soll.
     * @see #nextField()
     */
    public Field getNextField() {
        return enteredFields.get(index);
    }

    /**
     * Gibt das Feld zurück, auf dem der Stein bisher grafisch lag. ({@link #index} - 1)
     *
     * @return Feld von dem der Stein kommt.
     */
    public Field getCurrentField() {
        return enteredFields.get(index - 1);
    }

    /**
     * erhöht den {@link #index}, an dem der Move schrittweise durchgegangen wird.
     * Wenn es ein nächstes Feld gibt, wird der index erhöht und {@code true} zurückgegeben, ansonsten {@code false}.
     *
     * @return Flag, ob es ein nächstes Feld gibt.
     */
    public boolean nextField() {
        if (index < enteredFields.size() - 1) {
            index++;
            return true;
        }
        return false;
    }

    /**
     * Fügt ein Feld zu den besuchten Felder ({@link #enteredFields}) hinzu.
     *
     * @param f Feld, das der Spieler ausgewählt hat und das der Stein besucht.
     */
    public void addEnterField(Field f) {
        enteredFields.add(f);
    }

    /**
     * Fügt ein Feld zu den übersprungenen Feldern ({@link #skipedFields}) hinzu.
     * <br><b>WICHTIG:</b> Auf diesem Feld muss ein gegnerischer Stein liegen.
     * Wird dazu genutzt, um den gegnerischen Stein dann zu entfernen, wenn der ausgewählte Stein über diesem Feld liegt.
     *
     * @param f übersprungenes Feld mit einem gegnerischen Stein
     */
    public void addSkipField(List<Field> f) {
        skipedFields.addAll(f);
    }

    /**
     * Fügt eine ganze Liste von besuchten Feldern hinzu.
     *
     * @param f Liste mit Feldern, die der Stein der Reihe nach besuchen soll.
     */
    public void addEnterField(List<Field> f) {
        enteredFields.addAll(f);
    }

    /**
     * Fügt eine Reihe von übersprungenen Felder hinzu.
     * <br><b>WICHTIG:</b> Auf diesem Feld muss ein gegnerischer Stein liegen.
     * Wird dazu genutzt, um gegnerischen Steine dann zu entfernen, wenn der ausgewählte Stein über dem Feld liegt.
     *
     * @param f Liste mit Feldern, die der Stein überspringt und auf denen gegnerische Steine liegen.
     */
    public void addSkipField(Field f) {
        skipedFields.add(f);
    }

    /**
     * Gibt die Liste mit allen besuchten Feldern zurück.
     *
     * @return Liste mit Feldern, die der Stein besucht
     */
    public List<Field> getEnteredFields() {
        return enteredFields;
    }

    /**
     * Gibt eine Liste mit allen übersprungenen Feldern zurück.
     *
     * @return Liste mit Feldern, die der Stein überspringt.
     */
    public List<Field> getSkipedFields() {
        return skipedFields;
    }

    /**
     * Gibt das erste übersprungene Feld zurück.
     * Auf diesem Feld liegt der nächste gegnerische Stein, der übersprungen wird.
     *
     * @return nächstes Feld, welches übersprungen wird
     * @see #nextSkipedField()
     */
    public Field getFirstSkipedField() {
        if (!skipedFields.isEmpty()) {
            return skipedFields.get(0);
        }
        return null;
    }

    /**
     * entfernt das erste übersprungene Feld.
     * Wird ausgeführt, sobald dieses Feld tatsächlich übersprungen wurde.
     * Dadurch wird das nächste Feld zu dem 'FirstSkippedField'
     *
     * @see #getFirstSkipedField()
     */
    public void nextSkipedField() {
        if (!skipedFields.isEmpty()) {
            skipedFields.remove(0);
        }
    }

    /**
     * updatet den Stein auf die richtigen Koordinaten.
     * Sollte ganz am Schluss, sobald der Move abgeschlossen ist, ausgeführt werden.
     * Setzt die Koordinaten des Steins auf die, des Endfeldes.
     */
    public void update() {
        stone.setIndexX(getEndField().getIndexX());
        stone.setIndexY(getEndField().getIndexY());
    }

}
