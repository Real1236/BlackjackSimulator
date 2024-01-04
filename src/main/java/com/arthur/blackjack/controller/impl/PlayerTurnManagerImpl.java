package com.arthur.blackjack.controller.impl;

import java.util.Stack;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.arthur.blackjack.controller.PlayerTurnManager;
import com.arthur.blackjack.models.card.Deck;
import com.arthur.blackjack.models.hand.HandFactory;
import com.arthur.blackjack.models.hand.PlayerHand;
import com.arthur.blackjack.models.player.Dealer;
import com.arthur.blackjack.models.player.Player;
import com.arthur.blackjack.strategies.Strategy;
import com.arthur.blackjack.utils.GameUtils;

@Component
public class PlayerTurnManagerImpl implements PlayerTurnManager {
    private static final Logger logger = LogManager.getLogger(PlayerTurnManagerImpl.class);

    private Player player;
    private Dealer dealer;
    private Deck deck;
    private HandFactory handFactory;

    private Strategy playStrategy;

    public PlayerTurnManagerImpl(Player player, Dealer dealer, Deck deck, HandFactory handFactory) {
        this.player = player;
        this.dealer = dealer;
        this.deck = deck;
        this.handFactory = handFactory;
    }

    public void setStrategy(Strategy strategy) {
        this.playStrategy = strategy;
    }

    public void playerTurn() {
        Stack<PlayerHand> stack = new Stack<>();

        // Handle multiple hands from splitting
        while (!stack.empty()) {
            PlayerHand hand = stack.pop();
            GameUtils.displayHandsHiddenUpcard(dealer.getHand(), hand);
            
            if (split(hand, stack)) continue;
            if (doubleDown(hand)) continue;
            hitOrStand(hand);
        }

        GameUtils.displayHandsHiddenUpcard(dealer.getHand(), player.getHands().get(0));
    }

    private boolean split(PlayerHand hand, Stack<PlayerHand> stack) {
        // If the player has a pair and enough money to split, there's an option to split
        if (hand.getCards().get(0).getRank().getValue() == hand.getCards().get(1).getRank().getValue()
                && player.getBankroll() >= hand.getBet()
                && playStrategy.split()) {
            PlayerHand newHand = handFactory.createPlayerHand();
            newHand.addCard(hand.getCards().remove(1));
            newHand.setBet(hand.getBet());
            player.subtractFromBankroll(newHand.getBet());
            hand.addCard(deck.dealCard());
            newHand.addCard(deck.dealCard());
            logger.info("Player split and placed a bet of ${}.", newHand.getBet());
            logger.info("Player has ${} in their bankroll.", player.getBankroll());

            stack.push(newHand);
            stack.push(hand);
            return true;
        }
        return false;
    }

    private boolean doubleDown(PlayerHand hand) {
        // If the player has enough money to double down, there's an option to double down
        if (player.getBankroll() >= hand.getBet()
                && playStrategy.doubleDown()) {
            hand.addCard(deck.dealCard());
            hand.setBet(hand.getBet() * 2);
            player.subtractFromBankroll(hand.getBet());
            logger.info("Player doubled down and placed a bet of ${}.", hand.getBet());
            GameUtils.displayHandsHiddenUpcard(dealer.getHand(), hand);
            return true;
        }
        return false;
    }

    private void hitOrStand(PlayerHand hand) {
        while (hand.getHandValue() < 21 && playStrategy.hit()) {
            hand.addCard(deck.dealCard());
            logger.info("Player hit.");
            GameUtils.displayHandsHiddenUpcard(dealer.getHand(), hand);
        }
    }
    
}
