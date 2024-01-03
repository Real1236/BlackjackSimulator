package com.arthur.blackjack.controller;

import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.arthur.blackjack.config.GameSettings;
import com.arthur.blackjack.models.player.Dealer;
import com.arthur.blackjack.models.card.Deck;
import com.arthur.blackjack.models.hand.Hand;
import com.arthur.blackjack.models.hand.HandFactory;
import com.arthur.blackjack.models.player.Player;
import com.arthur.blackjack.utils.GameUtils;

import org.apache.logging.log4j.LogManager;

@Component
public class Game {
    private static final Logger logger = LogManager.getLogger(Game.class);

    private int roundNumber;

    private Player player;
    private Dealer dealer;
    private Deck deck;
    private HandFactory handFactory;

    private GameSettings settings;

    public Game(Player player, Dealer dealer, Deck deck, HandFactory handFactory, GameSettings settings) {
        this.roundNumber = 1;
        this.player = player;
        this.dealer = dealer;
        this.deck = deck;
        this.handFactory = handFactory;
        this.settings = settings;
    }
    
    public void play() {
        logger.info("Starting a game of Blackjack!");

        // Loop to play game
        while (GameUtils.playCondition(player.getBankroll(), roundNumber, settings.getMaxRounds())) {
            logger.info("Starting round {}.\n---------------------------------", roundNumber++);
            initializeHands();
            placeInitialBet();
            deal();
            if (!GameUtils.isOpen10Blackjack(dealer.getHand()) && !GameUtils.isBlackjack(player.getHands().get(0))) {
                playerTurn();
            }
            dealerTurn();
            payout();
        }
    }

    // private boolean playCondition() {
    //     return player.getBankroll() > 0 && roundNumber <= settings.getMaxRounds();
    // }

    private void initializeHands() {
        player.addHand(handFactory.createPlayerHand());
        dealer.setHand(handFactory.createDealerHand());
    }

    private void placeInitialBet() {
        logger.info("Player has ${} in their bankroll.", player.getBankroll());
        int betSize = settings.getBetSize();
        player.subtractFromBankroll(betSize);
        logger.info("Player placed a bet of ${}.", betSize);
    }

    private void deal() {
        Hand playerHand = player.getHands().get(0);
        playerHand.addCard(deck.dealCard());
        playerHand.addCard(deck.dealCard());

        Hand dealerHand = dealer.getHand();
        dealerHand.addCard(deck.dealCard());
        dealerHand.addCard(deck.dealCard());
    }

    private void playerTurn() {
        // TODO: Implement playerTurn
        return;
    }

    private void dealerTurn() {
        // TODO: Implement dealerTurn
        return;
    }

    private void payout() {
        // TODO: Implement payout
        return;
    }
}
