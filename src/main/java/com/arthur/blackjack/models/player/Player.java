package com.arthur.blackjack.models.player;

import com.arthur.blackjack.models.hand.PlayerHand;

import java.util.List;

public interface Player {
    int getBankroll();
    void subtractFromBankroll(int amount);
    void addToBankroll(int amount);
    List<PlayerHand> getHands();
    void addHand(PlayerHand hand);
    void clearHands();
}
