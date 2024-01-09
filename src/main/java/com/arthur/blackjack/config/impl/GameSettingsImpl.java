package com.arthur.blackjack.config.impl;

import org.springframework.stereotype.Component;

import com.arthur.blackjack.config.GameSettings;

@Component
public class GameSettingsImpl implements GameSettings {
    private final int bankroll;
    private final int betSize;
    private final int maxRounds;

    public GameSettingsImpl() {
        this.bankroll = 1000;
        this.betSize = 10;
        this.maxRounds = (int) Math.pow(10, 6);
    }

    @Override
    public int getBankroll() {
        return this.bankroll;
    }

    @Override
    public int getBetSize() {
        return this.betSize;
    }

    @Override
    public int getMaxRounds() {
        return this.maxRounds;
    }

}
