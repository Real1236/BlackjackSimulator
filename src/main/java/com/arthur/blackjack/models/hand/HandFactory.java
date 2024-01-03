package com.arthur.blackjack.models.hand;

public interface HandFactory {
    PlayerHand createPlayerHand();
    DealerHand createDealerHand();
}
