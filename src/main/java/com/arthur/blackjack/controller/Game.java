package com.arthur.blackjack.controller;

import com.arthur.blackjack.analytics.Analytics;
import com.arthur.blackjack.analytics.RoundResult;
import com.arthur.blackjack.config.GameRules;
import com.arthur.blackjack.config.GameSettings;
import com.arthur.blackjack.models.card.Deck;
import com.arthur.blackjack.models.hand.Hand;
import com.arthur.blackjack.models.hand.HandFactory;
import com.arthur.blackjack.models.hand.PlayerHand;
import com.arthur.blackjack.models.player.Dealer;
import com.arthur.blackjack.models.player.Player;
import com.arthur.blackjack.strategies.Strategy;
import com.arthur.blackjack.strategies.StrategyFactory;
import com.arthur.blackjack.utils.GameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class Game {
    private static final Logger logger = LogManager.getLogger(Game.class);

    private int roundNumber;
    private int roundBetSize;

    private final Player player;
    private final Dealer dealer;
    private final Deck deck;
    private final HandFactory handFactory;

    private final PlayerTurnManager playerTurnManager;

    private final GameSettings settings;
    private final GameRules rules;
    private final StrategyFactory strategyFactory;
    private final Analytics analytics;
    private Strategy strategy;

    public Game(Player player,
            Dealer dealer,
            Deck deck,
            HandFactory handFactory,
            PlayerTurnManager playerTurnManager,
            GameSettings settings,
            GameRules rules,
            StrategyFactory strategyFactory,
            @Qualifier("csvAnalyticsImpl") Analytics analytics) {
        this.roundNumber = 1;
        this.roundBetSize = 0;
        this.player = player;
        this.dealer = dealer;
        this.deck = deck;
        this.playerTurnManager = playerTurnManager;
        this.handFactory = handFactory;
        this.rules = rules;
        this.settings = settings;
        this.strategyFactory = strategyFactory;
        this.analytics = analytics;
    }

    public void play() {
        logger.info("Starting a game of Blackjack!");

        // Set strategy and analytics
        Strategy strategy = strategyFactory.getStrategy("hiLo"); // TODO - make strategy dynamic
        this.strategy = strategy;
        playerTurnManager.setStrategy(strategy);
        deck.setStrategy(strategy);
        analytics.createNewResultsSheet(1, settings.getBetSize()); // TODO - make game number dynamic

        deck.reshuffleDeck(); // Initialize Deck

        // Loop to play game
        while (GameUtils.playCondition(player.getBankroll(), roundNumber, settings.getMaxRounds())) {
            logger.info("Starting round {}.\n---------------------------------", roundNumber);
            logger.info("Player has ${} in their bankroll.", player.getBankroll());

            // Analytics
            analytics.recordNewRound(roundNumber, (int) player.getBankroll());

            // Round flow
            checkReshuffle();
            initializeHands();
            placeInitialBet();
            deal();
            playerTurnManager.playerTurn();
            dealerTurn();
            payout();
            player.clearHands();
            roundNumber++;
        }

        // Evaluate formulas and export results
        analytics.evaluateFormulas();
        analytics.saveExcel();
    }

    private void checkReshuffle() {
        if (deck.checkReshuffle()) {
            logger.info("Reshuffling deck.");
            deck.reshuffleDeck();
        }
    }

    private void initializeHands() {
        player.addHand(handFactory.createPlayerHand());
        dealer.setHand(handFactory.createDealerHand());
    }

    private void placeInitialBet() {
        this.roundBetSize = strategy.getBetSize();
        player.subtractFromBankroll(this.roundBetSize);
        player.getHands().getFirst().setBet(this.roundBetSize);
        analytics.recordInitialBet(roundNumber, this.roundBetSize);
        logger.info("Player placed a bet of ${}.", this.roundBetSize);
        logger.info("Player has ${} in their bankroll.", player.getBankroll());
    }

    private void deal() {
        Hand playerHand = player.getHands().getFirst();
        playerHand.addCard(deck.dealCard());
        playerHand.addCard(deck.dealCard());

        Hand dealerHand = dealer.getHand();
        dealerHand.addCard(deck.dealCard());
        dealerHand.addCard(deck.dealCard());
    }

    private void dealerTurn() {
        // Player if at least one player hand didn't bust
        if (player.getHands().stream().anyMatch(hand -> hand.getHandValue() <= 21)) {
            dealer.play();
        }
    }

    private void payout() {
        int handNumber = 1;
        for (PlayerHand hand : player.getHands()) {
            logger.info("Comparing player hand {} to dealer's hand.", handNumber);
            GameUtils.displayHands(dealer.getHand(), hand);

            // Analytics
            RoundResult result;

            // Bust
            if (hand.getHandValue() > 21) {
                logger.info("Hand {} busted with a hand value of {}.", handNumber, hand.getHandValue());
                result = hand.getBet() == this.roundBetSize ? RoundResult.BUST : RoundResult.DOUBLE_BUST;
            }

            // Blackjack
            else if (GameUtils.isBlackjack(hand)) {
                logger.info("Hand {} has a blackjack!", handNumber);
                if (GameUtils.isBlackjack(dealer.getHand())) {
                    logger.info("Dealer also has a blackjack. Player pushed.");
                    player.addToBankroll(hand.getBet());
                    result = RoundResult.PUSH;
                } else {
                    player.addToBankroll(hand.getBet() * (rules.getBlackjackPayout() + 1));
                    logger.info("Player won ${}.", hand.getBet() * (rules.getBlackjackPayout() + 1));
                    result = RoundResult.BLACKJACK;
                }
            }

            // Dealer bust
            else if (dealer.getHand().getHandValue() > 21) {
                logger.info("Dealer busted with a hand value of {}.", dealer.getHand().getHandValue());
                player.addToBankroll(hand.getBet() * 2);
                logger.info("Player hand {} won ${}.", handNumber, hand.getBet() * 2);
                result = hand.getBet() == this.roundBetSize ? RoundResult.WIN : RoundResult.DOUBLE_WIN;
            }

            // Compare hands
            else if (hand.getHandValue() > dealer.getHand().getHandValue()) {
                player.addToBankroll(hand.getBet() * 2);
                logger.info("Player hand {} won ${}.", handNumber, hand.getBet() * 2);
                result = hand.getBet() == this.roundBetSize ? RoundResult.WIN : RoundResult.DOUBLE_WIN;
            } else if (hand.getHandValue() == dealer.getHand().getHandValue()) {
                player.addToBankroll(hand.getBet());
                logger.info("Player hand {} pushed.", handNumber);
                result = RoundResult.PUSH;
            } else {
                logger.info("Player hand {} lost ${}.", handNumber, hand.getBet());
                result = hand.getBet() == this.roundBetSize ? RoundResult.LOSE : RoundResult.DOUBLE_LOSE;
            }

            // Analytics
            analytics.recordRoundResult(roundNumber, result);
            handNumber++;
        }
    }
}
