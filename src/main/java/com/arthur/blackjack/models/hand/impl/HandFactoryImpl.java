package com.arthur.blackjack.models.hand.impl;

import org.springframework.stereotype.Component;

import com.arthur.blackjack.models.hand.DealerHand;
import com.arthur.blackjack.models.hand.HandFactory;
import com.arthur.blackjack.models.hand.PlayerHand;

@Component
public class HandFactoryImpl implements HandFactory {
    public PlayerHand createPlayerHand() {
        return new PlayerHandImpl();
    }

    public DealerHand createDealerHand() {
        return new DealerHandImpl();
    }
}
