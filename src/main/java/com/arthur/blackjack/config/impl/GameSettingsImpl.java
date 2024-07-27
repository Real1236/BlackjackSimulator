package com.arthur.blackjack.config.impl;

import org.springframework.stereotype.Component;

import com.arthur.blackjack.config.GameSettings;

@Component
public class GameSettingsImpl implements GameSettings {
    private int bankroll;
    private int betSize;
    private int minChipSize;
    private int betSpread;
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
    public int getMinChipSize() {
        return this.minChipSize;
    }

    @Override
    public int getBetSpread() {
        return this.betSpread;
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
    public void setMinChipSize(int minChipSize) {
        this.minChipSize = minChipSize;
    }

    @Override
    public void setBetSpread(int betSpread) {
        this.betSpread = betSpread;
    }

    @Override
    public void setMaxRounds(int maxRounds) {
        this.maxRounds = maxRounds;
    }

}
