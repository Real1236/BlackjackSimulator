package com.arthur.blackjack.controller;

import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.arthur.blackjack.analytics.Analytics;
import com.arthur.blackjack.analytics.RoundResult;
import com.arthur.blackjack.config.GameRules;
import com.arthur.blackjack.config.GameSettings;
import com.arthur.blackjack.models.player.Dealer;
import com.arthur.blackjack.models.card.Deck;
import com.arthur.blackjack.models.hand.Hand;
import com.arthur.blackjack.models.hand.HandFactory;
import com.arthur.blackjack.models.hand.PlayerHand;
import com.arthur.blackjack.models.player.Player;
import com.arthur.blackjack.strategies.StrategyFactory;
import com.arthur.blackjack.utils.GameUtils;

import java.util.Stack;

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
    private GameRules rules;
    private StrategyFactory strategyFactory;
    private Analytics analytics;

    public Game(Player player,
            Dealer dealer,
            Deck deck,
            HandFactory handFactory,
            PlayerTurnManager playerTurnManager,
            GameSettings settings,
            GameRules rules,
            StrategyFactory strategyFactory,
            Analytics analytics) {
        this.roundNumber = 1;
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
        playerTurnManager.setStrategy(strategyFactory.getStrategy("basic"));
        analytics.createNewResultsSheet(1, settings.getBetSize()); // TODO - make game number dynamic

        deck.reshuffleDeck(); // Initialize Deck

        // Loop to play game
        while (GameUtils.playCondition(player.getBankroll(), roundNumber, settings.getMaxRounds())) {
            logger.info("Starting round {}.\n---------------------------------", roundNumber);
            logger.info("Player has ${} in their bankroll.", player.getBankroll());

            // Analytics
            analytics.writeResults(roundNumber, (int) player.getBankroll());

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
        // Player if at least one player hand didn't bust
        if (player.getHands().stream().anyMatch(hand -> hand.getHandValue() <= 21)) {
            dealer.play();
        }
    }

    private void payout() {
        Stack<PlayerHand> stack = new Stack<>();
        stack.addAll(player.getHands());
        int handNumber = 1;

        while (!stack.empty()) {
            PlayerHand hand = stack.pop();

            // Analytics
            RoundResult result = null;

            // Bust
            if (hand.getHandValue() > 21) {
                logger.info("Hand {} busted with a hand value of {}.", handNumber, hand.getHandValue());
                result = hand.getBet() == settings.getBetSize() ? RoundResult.BUST : RoundResult.DOUBLE_BUST;
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
                logger.info("Player won ${}.", hand.getBet() * 2);
                result = hand.getBet() == settings.getBetSize() ? RoundResult.WIN : RoundResult.DOUBLE_WIN;
            }

            // Compare hands
            else if (hand.getHandValue() > dealer.getHand().getHandValue()) {
                player.addToBankroll(hand.getBet() * 2);
                logger.info("Player won ${}.", hand.getBet() * 2);
                result = hand.getBet() == settings.getBetSize() ? RoundResult.WIN : RoundResult.DOUBLE_WIN;
            } else if (hand.getHandValue() == dealer.getHand().getHandValue()) {
                player.addToBankroll(hand.getBet());
                logger.info("Player pushed.");
                result = RoundResult.PUSH;
            } else {
                logger.info("Player lost ${}.", hand.getBet());
                result = hand.getBet() == settings.getBetSize() ? RoundResult.LOSE : RoundResult.DOUBLE_LOSE;
            }

            // Analytics
            analytics.recordRoundResult(roundNumber, result);
        }
    }
}
