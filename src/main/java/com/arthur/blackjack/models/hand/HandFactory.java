package com.arthur.blackjack.models.hand;

import com.arthur.blackjack.models.hand.DealerHand;
import com.arthur.blackjack.models.hand.PlayerHand;

public interface HandFactory {
    PlayerHand createPlayerHand();
    DealerHand createDealerHand();
}
