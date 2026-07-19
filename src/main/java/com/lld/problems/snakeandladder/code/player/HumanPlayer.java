package com.lld.problems.snakeandladder.code.player;

import java.util.Scanner;

public class HumanPlayer extends Player {
    private Scanner scanner;

    public HumanPlayer(String name) {
        super(name);
        this.scanner = new Scanner(System.in);
    }

    @Override
    public int rollDice() {
        System.out.println("Hit enter to roll your dice " + getPlayerName());
        scanner.nextLine();

        return getDice().roll();
    }

}
