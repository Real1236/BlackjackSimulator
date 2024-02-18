package com.arthur.blackjack.models.player.impl;

import com.arthur.blackjack.models.player.Dealer;
import com.arthur.blackjack.utils.GameUtils;
import com.arthur.blackjack.config.GameRules;
import com.arthur.blackjack.models.card.Deck;
import com.arthur.blackjack.models.hand.DealerHand;

public class DealerImpl implements Dealer {

    private DealerHand hand;

    private final GameRules gameRules;

    public DealerImpl(GameRules gameRules) {
        this.hand = null;
        this.gameRules = gameRules;
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
    public void play(Deck deck) {
        if (gameRules.isStandsOnSoft17()) {
            while (hand.getHandValue() < 17)
                hand.addCard(deck.dealCard());
        } else {
            int handTotal = hand.getHandValue();
            while (handTotal < 17 || (!GameUtils.isHard(hand) && handTotal == 17)) {
                hand.addCard(deck.dealCard());
                handTotal = hand.getHandValue();
            }
        }
    }
    
}
