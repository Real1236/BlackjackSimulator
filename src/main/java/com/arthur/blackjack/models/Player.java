package com.arthur.blackjack.models;

import java.util.List;

public interface Player {
    void subtractFromBankroll();
    void addToBankroll();
    List<Hand> getHands();
    void addHand(Hand hand);
    void clearHands();
}
