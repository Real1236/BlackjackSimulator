package com.arthur.blackjack.config;

public interface GameSettings {
    int getBankroll();
    int getBetSize();
    int getMaxRounds();
    void setBankroll(int bankroll);
    void setBetSize(int betSize);
    void setMaxRounds(int maxRounds);
}
