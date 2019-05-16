package ffhs.controller;

import ffhs.model.*;
import ffhs.model.Player;

/**
 * Verwaltung von Spielerinformationen
 *
 * @author Alexander Hengsteler
 */
public class PlayerController {

    private Player player1;
    private Player player2;
    private boolean currentPlayer1;
    private boolean singlePlayerGame;

    public PlayerController() {}

    public PlayerController(boolean ki, int size, String name1, String name2) {
        init(ki, size, name1, name2);
    }

    /**
     * Initialisiert das Spiel mit den angegebenen Parametern
     * @param ki Gibt an, ob die KI benutzt wird (Singleplayer)
     * @param size Größe des Spielfeldes
     * @param name1 Name von Player1
     * @param name2 Name von Player2
     */
    public void init(boolean ki, int size, String name1, String name2) {
        singlePlayerGame = ki;
        player1 = new Player(Color.BLACK, name1.isEmpty() || name1.length() > 15 ? "Player 1" : name1, size);
        if (!ki) {
            player2 = new Player(Color.WHITE, name2.isEmpty() || name2.length() > 15 ? "Player 2" : name2, size);
        }
        else{
            player2 = new AI(Color.WHITE, name2.isEmpty() || name2.length() > 15 ? "KI" : name2, size, player1);
        }
        currentPlayer1 = true;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public Player getPlayerByColor(Color c) {
        if (player1.getColor() == c) {
            return player1;
        }
        else {
            return player2;
        }
    }

    /**
     * Gibt den aktiven Spieler zurück
     * @return Player aktiver Spieler
     */
    public Player getCurrentPlayer() {
        if (currentPlayer1) {
            return player1;
        }
        else {
            return player2;
        }
    }

    /**
     * Gibt den nicht aktiven Spieler zurück
     * @return Player nicht aktiver Spieler
     */
    public Player getOtherPlayer() {
        if (!currentPlayer1) {
            return player1;
        }
        else {
            return player2;
        }
    }

    /**
     *  Wechselt den aktiven Spieler
     */
    public void changePlayer() {
        currentPlayer1 = !currentPlayer1;
    }

    public boolean isSinglePlayerGame() {
        return singlePlayerGame;
    }

    public boolean isCurrentPlayer1() {
        return currentPlayer1;
    }

}
