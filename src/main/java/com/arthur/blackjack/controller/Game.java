package com.arthur.blackjack.controller;

import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.arthur.blackjack.config.GameSettings;
import com.arthur.blackjack.models.player.Dealer;
import com.arthur.blackjack.models.card.Deck;
import com.arthur.blackjack.models.hand.Hand;
import com.arthur.blackjack.models.hand.HandFactory;
import com.arthur.blackjack.models.player.Player;
import com.arthur.blackjack.strategies.StrategyFactory;
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

    private PlayerTurnManager playerTurnManager;

    private GameSettings settings;
    private StrategyFactory strategyFactory;

    public Game(Player player, Dealer dealer, Deck deck, HandFactory handFactory, PlayerTurnManager playerTurnManager, GameSettings settings, StrategyFactory strategyFactory) {
        this.roundNumber = 1;
        this.player = player;
        this.dealer = dealer;
        this.deck = deck;
        this.playerTurnManager = playerTurnManager;
        this.handFactory = handFactory;
        this.settings = settings;
        this.strategyFactory = strategyFactory;
    }
    
    public void play() {
        logger.info("Starting a game of Blackjack!");

        deck.reshuffleDeck();   // Initialize Deck
        playerTurnManager.setStrategy(strategyFactory.getStrategy("basic"));    // Set strategy

        // Loop to play game
        while (GameUtils.playCondition(player.getBankroll(), roundNumber, settings.getMaxRounds())) {
            logger.info("Starting round {}.\n---------------------------------", roundNumber++);
            logger.info("Player has ${} in their bankroll.", player.getBankroll());
            initializeHands();
            placeInitialBet();
            deal();
            playerTurnManager.playerTurn();
            dealerTurn();
            payout();
        }
    }

    private void initializeHands() {
        player.addHand(handFactory.createPlayerHand());
        dealer.setHand(handFactory.createDealerHand());
    }

    private void placeInitialBet() {
        int betSize = settings.getBetSize();
        player.subtractFromBankroll(betSize);
        player.getHands().get(0).setBet(betSize);
        logger.info("Player placed a bet of ${}.", betSize);
        logger.info("Player has ${} in their bankroll.", player.getBankroll());
    }

    private void deal() {
        Hand playerHand = player.getHands().get(0);
        playerHand.addCard(deck.dealCard());
        playerHand.addCard(deck.dealCard());

        Hand dealerHand = dealer.getHand();
        dealerHand.addCard(deck.dealCard());
        dealerHand.addCard(deck.dealCard());
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
