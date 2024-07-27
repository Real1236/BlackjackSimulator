package com.arthur.blackjack.models.hand.impl;

import java.util.ArrayList;
import com.arthur.blackjack.models.hand.PlayerHand;

public class PlayerHandImpl extends AbstractHand implements PlayerHand {

    private float bet;

    public PlayerHandImpl() {
        super(new ArrayList<>());
        this.bet = 0;
    }

    @Override
    public float getBet() {
        return bet;
    }

    @Override
    public void setBet(float bet) {
        this.bet = bet;
    }
    
}
