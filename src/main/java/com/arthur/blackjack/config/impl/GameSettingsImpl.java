package com.arthur.blackjack.config.impl;

import org.springframework.stereotype.Component;

import com.arthur.blackjack.config.GameSettings;

@Component
public class GameSettingsImpl implements GameSettings {
    private int bankroll;
    private int betSize;
    private int maxRounds;

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

    @Override
    public void setBankroll(int bankroll) {
        this.bankroll = bankroll;
    }

    @Override
    public void setBetSize(int betSize) {
        this.betSize = betSize;
    }

    @Override
    public void setMaxRounds(int maxRounds) {
        this.maxRounds = maxRounds;
    }

}
