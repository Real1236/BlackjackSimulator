package com.arthur.blackjack.controller.impl;

import java.util.Stack;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.arthur.blackjack.config.GameRules;
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
    private GameRules rules;

    public PlayerTurnManagerImpl(Player player, Dealer dealer, Deck deck, HandFactory handFactory, GameRules rules) {
        this.player = player;
        this.dealer = dealer;
        this.deck = deck;
        this.handFactory = handFactory;
        this.rules = rules;
    }

    public void setStrategy(Strategy strategy) {
        this.playStrategy = strategy;
    }

    public void playerTurn() {
        // Only play turn if dealer and player both don't have blackjack
        if (!GameUtils.isOpen10Blackjack(dealer.getHand()) && !GameUtils.isBlackjack(player.getHands().get(0))) {
            Stack<PlayerHand> stack = new Stack<>();
            stack.addAll(player.getHands());

            // Handle multiple hands from splitting
            while (!stack.empty()) {
                PlayerHand hand = stack.pop();
                GameUtils.displayHandsHiddenUpcard(dealer.getHand(), hand);

                if (split(hand, stack))
                    continue;
                if (doubleDown(hand))
                    continue;
                hitOrStand(hand);
            }
        }
    }

    private boolean split(PlayerHand hand, Stack<PlayerHand> stack) {
        int playerHandFirstCardValue = hand.getCards().get(0).getRank().getValue();
        int playerHandSecondCardValue = hand.getCards().get(1).getRank().getValue();
        int dealerUpcardValue = dealer.getHand().getCards().get(0).getRank().getValue();

        // Must have pair, only 2 cards, enough money, and not exceed resplit limit
        if (playerHandFirstCardValue == playerHandSecondCardValue
                && hand.getCards().size() == 2
                && player.getBankroll() >= hand.getBet()
                && player.getHands().size() < rules.getResplitLimit()
                && playStrategy.split(playerHandFirstCardValue, dealerUpcardValue)) {
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
        // Must have enough money, only 2 cards, and double after split or only 1 hand
        if (player.getBankroll() >= hand.getBet()
                && hand.getCards().size() == 2
                && (rules.isDoubleAfterSplit() || player.getHands().size() == 1)
                && playStrategy.doubleDown(hand, dealer.getHand().getUpCard().getRank().getValue())) {
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
        while (hand.getHandValue() < 21
                && playStrategy.hit(hand, dealer.getHand().getUpCard().getRank().getValue())) {
            hand.addCard(deck.dealCard());
            logger.info("Player hit.");
            GameUtils.displayHandsHiddenUpcard(dealer.getHand(), hand);
        }
    }

}
