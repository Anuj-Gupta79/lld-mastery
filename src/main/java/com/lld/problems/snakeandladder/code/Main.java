package com.lld.problems.snakeandladder.code;

import java.util.List;

import com.lld.problems.snakeandladder.code.constants.PlayerIndicator;
import com.lld.problems.snakeandladder.code.models.PlayerInfo;

public class Main {
    public static void main(String[] args) {
        List<PlayerInfo> playerInfos = List.of(
                new PlayerInfo("Alex", PlayerIndicator.HUMAN),
                new PlayerInfo("Carry", PlayerIndicator.HUMAN),
                new PlayerInfo("BotOne", PlayerIndicator.BOT));

        SnakeAndLadderGame game = new SnakeAndLadderGame(playerInfos);

        game.start();
    }

}
