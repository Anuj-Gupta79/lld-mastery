package com.lld.problems.snakeandladder.code.models;

import com.lld.problems.snakeandladder.code.constants.PlayerIndicator;

public class PlayerInfo {
    private String name;
    private PlayerIndicator indicator;

    public PlayerInfo(String name, PlayerIndicator indicator) {
        this.name = name;
        this.indicator = indicator;
    }

    public String getPlayerName() {
        return this.name;
    }

    public PlayerIndicator getPlayerIndicator() {
        return this.indicator;
    }
}
