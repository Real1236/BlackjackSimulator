package com.arthur.blackjack.config;

import lombok.Getter;

@Getter
public enum Rule {
    NORMAL("rules/normal.properties"),
    FANDUEL_AMERICAN("rules/FanDuelAmerican.properties");

    private final String path;

    Rule(String path) {
        this.path = path;
    }

}