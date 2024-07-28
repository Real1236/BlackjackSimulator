package com.arthur.blackjack.config;

public interface GameSettings {
    float getBankroll();
    int getBetSize();
    int getMinChipSize();
    int getBetSpread();
    int getMaxRounds();
    void setBankroll(float bankroll);
    void setBetSize(int betSize);
    void setMinChipSize(int minChipSize);
    void setBetSpread(int betSpread);
    void setMaxRounds(int maxRounds);
}
