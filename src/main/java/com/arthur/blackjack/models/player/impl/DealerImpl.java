package com.arthur.blackjack.models.player.impl;

import org.springframework.stereotype.Component;

import com.arthur.blackjack.models.player.Dealer;
import com.arthur.blackjack.models.hand.DealerHand;

@Component
public class DealerImpl implements Dealer {

    private DealerHand hand;

    public DealerImpl() {
        this.hand = null;
    }

    @Override
    public DealerHand getHand() {
        return hand;
    }

    @Override
    public void setHand(DealerHand hand) {
        this.hand = hand;
    }

    @Override
    public void play() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'play'");
    }
    
}
