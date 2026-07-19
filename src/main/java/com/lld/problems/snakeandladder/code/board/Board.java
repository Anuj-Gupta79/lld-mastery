package com.lld.problems.snakeandladder.code.board;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.lld.problems.snakeandladder.code.models.Jump;
import com.lld.problems.snakeandladder.code.models.Pawn;
import com.lld.problems.snakeandladder.code.player.Player;

public class Board {
    private List<Player> players;
    private List<Jump> jumps;

    public Board() {
        this.players = new ArrayList<>();
        this.jumps = generateJumps();
    }

    public int getFinalPosition(int currentPosition, int steps) {
        int finalPosition = currentPosition + steps;
        if (finalPosition > 100) {
            return currentPosition;
        }

        for (Jump jump : this.jumps) {
            if (jump.getStart() == finalPosition) {
                finalPosition = jump.getEnd();
                break;
            }
        }
        return finalPosition;
    }

    public void registerPlayer(Player player) {
        if (this.players.contains(player)) {
            return;
        }

        this.players.add(player);
    }

    public void notifyPlayers(Player currPlayer) {
        for (Player player : this.players) {
            System.out.println(player.getPlayerName() + " has been informed that " + currPlayer.getPlayerName()
                    + "'s pawn moved to position " + currPlayer.getPawn().getCurrentPosition());
        }
    }

    public boolean checkWin(Pawn pawn) {
        return pawn.getCurrentPosition() == 100;
    }

    public List<Player> getPlayers() {
        return this.players;
    }

    public List<Jump> getJumps() {
        return this.jumps;
    }

    private List<Jump> generateJumps() {
        List<Jump> jumps = new ArrayList<>();
        Set<Integer> cellSet = new HashSet<>();
        Random random = new Random();
        for (int ind = 0; ind < 10; ind++) {
            int start = 2 + random.nextInt(97);
            int end = 2 + random.nextInt(97);

            if (start != end && !cellSet.contains(start) && !cellSet.contains(end)) {
                Jump jump = new Jump(start, end);
                jumps.add(jump);
                cellSet.add(start);
                cellSet.add(end);
            }

        }
        return jumps;
    }
}
