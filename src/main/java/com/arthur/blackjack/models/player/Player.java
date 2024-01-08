package com.arthur.blackjack.models.player;

import com.arthur.blackjack.models.hand.PlayerHand;

import java.util.List;

public interface Player {
    double getBankroll();
    void subtractFromBankroll(double amount);
    void addToBankroll(double amount);
    List<PlayerHand> getHands();
    void addHand(PlayerHand hand);
    void clearHands();
}
