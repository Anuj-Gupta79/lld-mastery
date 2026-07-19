package com.lld.problems.snakeandladder.code.manager;

import java.util.List;

import com.lld.problems.snakeandladder.code.player.Player;

public class TurnManager {
    private List<Player> players;
    private Player currentPlayer;

    public TurnManager(List<Player> players) {
        this.players = players;
    }

    public Player getNextPlayer() {
        int size = this.players.size();
        int index = (this.players.indexOf(this.currentPlayer) + 1) % size;
        this.currentPlayer = players.get(index);
        System.out.println(currentPlayer.getPlayerName() + "'s turn =>");
        return this.currentPlayer;
    }
}
