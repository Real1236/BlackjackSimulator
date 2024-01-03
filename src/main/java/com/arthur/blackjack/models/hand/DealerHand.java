package com.arthur.blackjack.models.hand;

import com.arthur.blackjack.models.card.Card;

public interface DealerHand extends Hand {
    Card getUpCard();
}
