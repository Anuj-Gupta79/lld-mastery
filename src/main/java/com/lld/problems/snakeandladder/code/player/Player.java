package com.lld.problems.snakeandladder.code.player;

import com.lld.problems.snakeandladder.code.board.Board;
import com.lld.problems.snakeandladder.code.models.Dice;
import com.lld.problems.snakeandladder.code.models.Pawn;

public abstract class Player {
    private String playerName;
    private Dice dice;
    private Pawn pawn;

    public Player(String name) {
        this.playerName = name;
        this.pawn = new Pawn();
        this.dice = new Dice();
    }

    public final void chance(Board board) {
        int steps = rollDice();
        int currPosition = this.pawn.getCurrentPosition();
        int finalPosition = board.getFinalPosition(currPosition, steps);
        moveToCell(finalPosition);
    }

    abstract int rollDice();

    protected void moveToCell(int position) {
        this.pawn.updatePosition(position);
    }

    protected Dice getDice() {
        return this.dice;
    }

    public Pawn getPawn() {
        return this.pawn;
    }

    public String getPlayerName() {
        return this.playerName;
    }

}
