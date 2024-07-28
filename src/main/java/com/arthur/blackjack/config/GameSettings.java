package com.arthur.blackjack.config;

public interface GameSettings {
    float getBankroll();
    int getBetSize();
    float getMinChipSize();
    int getBetSpread();
    int getMaxRounds();
    void setBankroll(float bankroll);
    void setBetSize(int betSize);
    void setMinChipSize(float minChipSize);
    void setBetSpread(int betSpread);
    void setMaxRounds(int maxRounds);
}
