package com.arthur.blackjack.models.player;

import com.arthur.blackjack.models.card.Deck;
import com.arthur.blackjack.models.hand.DealerHand;

public interface Dealer {
    DealerHand getHand();
    void setHand(DealerHand hand);
    void play(Deck deck);
}
