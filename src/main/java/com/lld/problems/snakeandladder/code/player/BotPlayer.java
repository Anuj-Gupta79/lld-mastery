package com.lld.problems.snakeandladder.code.player;

public class BotPlayer extends Player {

    public BotPlayer(String name) {
        super(name);
    }

    @Override
    public int rollDice() {
        return getDice().roll();
    }

}
