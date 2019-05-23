package ffhs.controller;

import ffhs.model.*;
import ffhs.model.Player;

public class PlayerController {

    private Player player1;
    private Player player2;
    private boolean currentPlayer1;
    private boolean singlePlayerGame;

    public PlayerController() {}

    public PlayerController(boolean ai, int size, String name1, String name2) {
        init(ai, size, name1, name2);
    }

    public void init(boolean ai, int size, String name1, String name2) {
        singlePlayerGame = ai;
        player1 = new Player(Color.BLACK, name1.isEmpty() || name1.length() > 15 ? "Player 1" : name1, size);
        if (!ai) {
            player2 = new Player(Color.RED, name2.isEmpty() || name2.length() > 15 ? "Player 2" : name2, size);
        }
        else{
            player2 = new AI(Color.RED, name2.isEmpty() || name2.length() > 15 ? "Computer" : name2, size, player1);
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

    public Player getCurrentPlayer() {
        if (currentPlayer1) {
            return player1;
        }
        else {
            return player2;
        }
    }

    public Player getOtherPlayer() {
        if (!currentPlayer1) {
            return player1;
        }
        else {
            return player2;
        }
    }

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
