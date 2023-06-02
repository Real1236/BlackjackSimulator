package com.arthur.blackjack.player;

import com.arthur.blackjack.component.Card;
import com.arthur.blackjack.component.Deck;
import com.arthur.blackjack.component.Hand;
import com.arthur.blackjack.core.GameRules;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Dealer {
    private Hand hand;
    GameRules gameRules;

    @Autowired
    public Dealer(GameRules gameRules) {
        this.gameRules = gameRules;
        hand = new Hand();
    }

    public void dealCard(Player player, Deck deck) {
        Card card = deck.dealCard();
        player.addCard(card);
    }

    public void dealCard(Player player, int hand, Deck deck) {
        Card card = deck.dealCard();
        player.addCard(card, hand);
    }

    public void dealCardToDealer(Deck deck) {
        Card card = deck.dealCard();
        hand.addCard(card);
    }

    public Hand getHand() {
        return hand;
    }

    public Card getUpcard() {
        return hand.getCards().get(0);
    }

    public void play(Deck deck) {
        if (gameRules.isStandsOnSoft17()) {
            while (hand.getTotal() < 17)
                dealCardToDealer(deck);
        } else {
            int handTotal = hand.getTotal();
            while (handTotal < 17 || (!hand.isHard() && handTotal == 17)) {
                dealCardToDealer(deck);
                handTotal = hand.getTotal();
            }
        }
    }

    public void clearHand() {
        hand = new Hand();
    }
}
