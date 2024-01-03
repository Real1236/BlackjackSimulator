package com.arthur.blackjack.models.hand.impl;

import java.util.ArrayList;
import com.arthur.blackjack.models.card.Card;
import com.arthur.blackjack.models.hand.PlayerHand;

public class PlayerHandImpl extends AbstractHand implements PlayerHand {

    private int bet;

    public PlayerHandImpl() {
        super(new ArrayList<Card>());
        this.bet = 0;
    }

    @Override
    public Integer getBet() {
        return bet;
    }

    @Override
    public void setBet(Integer bet) {
        this.bet = bet;
    }
    
}
