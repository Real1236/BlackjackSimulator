package com.arthur.blackjack.models;

public interface Dealer {
    Hand getHand();
    void setHand(Hand hand);
    void play();
}
