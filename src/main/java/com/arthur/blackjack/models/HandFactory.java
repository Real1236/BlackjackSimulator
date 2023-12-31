package com.arthur.blackjack.models;

public interface HandFactory {
    PlayerHand createPlayerHand();
    DealerHand createDealerHand();
}
