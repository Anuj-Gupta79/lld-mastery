package com.lld.problems.snakeandladder.code;

import java.util.ArrayList;
import java.util.List;

import com.lld.problems.snakeandladder.code.board.Board;
import com.lld.problems.snakeandladder.code.factory.PlayerFactory;
import com.lld.problems.snakeandladder.code.manager.TurnManager;
import com.lld.problems.snakeandladder.code.models.PlayerInfo;
import com.lld.problems.snakeandladder.code.player.Player;

public class SnakeAndLadderGame {
    private List<Player> players;
    private PlayerFactory factory;
    private TurnManager manager;
    private Board board;

    public SnakeAndLadderGame(List<PlayerInfo> playerInfos) {
        this.players = new ArrayList<>();
        this.factory = new PlayerFactory();
        this.board = new Board();

        for (PlayerInfo info : playerInfos) {
            Player currentPlayer = factory.createPlayer(info.getPlayerIndicator(), info.getPlayerName());
            this.players.add(currentPlayer);
            this.board.registerPlayer(currentPlayer);
        }

        this.manager = new TurnManager(players);
    }

    public void start() {
        while (true) {
            Player currentPlayer = manager.getNextPlayer();
            currentPlayer.chance(board);
            boolean isWin = board.checkWin(currentPlayer.getPawn());
            board.notifyPlayers(currentPlayer);

            if (isWin) {
                end(currentPlayer);
                break;
            }
        }
    }

    public void end(Player currentPlayer) {
        System.out.println("Congratulations " + currentPlayer.getPlayerName() + " has been won the game!");
    }
}
