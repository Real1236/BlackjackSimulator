package com.arthur.player;

import com.arthur.component.Card;
import com.arthur.component.Deck;
import com.arthur.component.Hand;

public class Dealer {
    private Hand hand;

    public Dealer() {
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

    public void play(Deck deck) {
        while (hand.getTotal() < 17) {
            dealCardToDealer(deck);
        }
    }

    public void clearHand() {
        hand = new Hand();
    }
}
