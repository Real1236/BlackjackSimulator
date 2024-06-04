package com.arthur.blackjack.models.hand.impl;

import java.util.ArrayList;

import com.arthur.blackjack.models.card.Card;
import com.arthur.blackjack.models.hand.DealerHand;

public class DealerHandImpl extends AbstractHand implements DealerHand {

    public DealerHandImpl() {
        super(new ArrayList<>());
    }

    @Override
    public Card getUpCard() {
        return cards.getFirst();
    }
    
}
