package com.arthur.blackjack.config.impl;

import org.springframework.stereotype.Component;

import com.arthur.blackjack.config.GameSettings;

@Component
public class GameSettingsImpl implements GameSettings {
    private final int bankroll;
    private final int betSize;
    private final int maxRounds;

    public GameSettingsImpl() {
        this.betSize = 1;
        this.bankroll = 1000 * betSize; // 1000 betting units = 1% risk of ruin
        this.maxRounds = (int) Math.pow(10, 3);
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
