package com.arthur.blackjack.models.player;

import com.arthur.blackjack.models.hand.Hand;

import java.util.List;

public interface Player {
    int getBankroll();
    void subtractFromBankroll(int amount);
    void addToBankroll(int amount);
    List<Hand> getHands();
    void addHand(Hand hand);
    void clearHands();
}
