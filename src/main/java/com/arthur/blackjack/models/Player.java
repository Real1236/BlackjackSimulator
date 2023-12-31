package com.arthur.blackjack.models;

import java.util.List;

public interface Player {
    int getBankroll();
    void subtractFromBankroll(int amount);
    void addToBankroll(int amount);
    List<Hand> getHands();
    void addHand(Hand hand);
    void clearHands();
}
