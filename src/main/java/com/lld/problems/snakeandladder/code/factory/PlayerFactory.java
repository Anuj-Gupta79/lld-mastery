package com.lld.problems.snakeandladder.code.factory;

import com.lld.problems.snakeandladder.code.constants.PlayerIndicator;
import com.lld.problems.snakeandladder.code.player.BotPlayer;
import com.lld.problems.snakeandladder.code.player.HumanPlayer;
import com.lld.problems.snakeandladder.code.player.Player;

public class PlayerFactory {
    public Player createPlayer(PlayerIndicator indicator, String playerName) {
        switch (indicator) {
            case HUMAN:
                return new HumanPlayer(playerName);
            case BOT:
                return new BotPlayer(playerName);
            default:
                throw new IllegalArgumentException("Invalid Player Indicator");
        }
    }
}
