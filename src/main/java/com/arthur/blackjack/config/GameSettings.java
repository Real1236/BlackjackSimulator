package com.arthur.blackjack.config;

public interface GameSettings {
    int getBankroll();
    int getBetSize();
    int getMinChipSize();
    int getBetSpread();
    int getMaxRounds();
    void setBankroll(int bankroll);
    void setBetSize(int betSize);
    void setMinChipSize(int minChipSize);
    void setBetSpread(int betSpread);
    void setMaxRounds(int maxRounds);
}
