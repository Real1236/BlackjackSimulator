package com.arthur.blackjack.models.player;

import com.arthur.blackjack.models.hand.Hand;

public interface Dealer {
    Hand getHand();
    void setHand(Hand hand);
    void play();
}
