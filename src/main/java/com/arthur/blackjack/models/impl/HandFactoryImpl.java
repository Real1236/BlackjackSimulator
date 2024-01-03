package com.arthur.blackjack.models.impl;

import org.springframework.stereotype.Component;

import com.arthur.blackjack.models.DealerHand;
import com.arthur.blackjack.models.HandFactory;
import com.arthur.blackjack.models.PlayerHand;

@Component
public class HandFactoryImpl implements HandFactory {
    public PlayerHand createPlayerHand() {
        return new PlayerHandImpl();
    }

    public DealerHand createDealerHand() {
        return new DealerHandImpl();
    }
}
