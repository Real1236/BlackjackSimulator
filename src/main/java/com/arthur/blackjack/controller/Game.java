package com.arthur.blackjack.controller;

import com.arthur.blackjack.analytics.Analytics;
import com.arthur.blackjack.analytics.RoundResult;
import com.arthur.blackjack.config.GameRules;
import com.arthur.blackjack.config.GameSettings;
import com.arthur.blackjack.models.card.Deck;
import com.arthur.blackjack.models.card.DeckFactory;
import com.arthur.blackjack.models.hand.Hand;
import com.arthur.blackjack.models.hand.HandFactory;
import com.arthur.blackjack.models.hand.PlayerHand;
import com.arthur.blackjack.models.player.Dealer;
import com.arthur.blackjack.models.player.Player;
import com.arthur.blackjack.models.player.PlayerFactory;
import com.arthur.blackjack.strategies.Strategy;
import com.arthur.blackjack.utils.GameUtils;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Game extends Thread {
    private static final Logger logger = LogManager.getLogger(Game.class);

    private final int gameNum;
    private int roundNumber;
    private int roundBetSize;

    private final Player player;
    private final Dealer dealer;
    private final Deck deck;

    private final HandFactory handFactory;

    private final PlayerTurnManager playerTurnManager;

    private final GameSettings settings;
    private final GameRules rules;

    @Setter
    private Analytics analytics;
    @Setter
    private Strategy strategy;

    public Game(int gameNum,
                PlayerFactory playerFactory,
                DeckFactory deckFactory,
                HandFactory handFactory,
                PlayerTurnManager playerTurnManager,
                GameSettings settings,
                GameRules rules) {
        this.gameNum = gameNum;
        this.roundNumber = 1;
        this.roundBetSize = 0;
        this.player = playerFactory.createPlayer();
        this.dealer = playerFactory.createDealer();
        this.deck = deckFactory.createDeck();
        this.playerTurnManager = playerTurnManager;
        this.handFactory = handFactory;
        this.rules = rules;
        this.settings = settings;
    }

    @Override
    public void run() {
        play();
    }

    public void play() {
        // Initialize deck and playerTurn strategy (not best practice but idk how to fix it)
        deck.setStrategy(strategy);
        playerTurnManager.setStrategy(strategy);

        logger.trace("Starting a game of Blackjack!");

        // Set analytics
        analytics.createNewResultsSheet(settings.getBetSize()); // TODO - make game number dynamic

        deck.reshuffleDeck(); // Initialize Deck

        // Loop to play game
        while (GameUtils.playCondition(player.getBankroll(), roundNumber, settings.getMaxRounds())) {
            logger.info("Thread " + gameNum + ": Starting round {}.\n---------------------------------", roundNumber);
            logger.trace("Player has ${} in their bankroll.", player.getBankroll());

            // Analytics
            analytics.recordNewRound(roundNumber, (int) player.getBankroll());

            // Round flow
            checkReshuffle();
            initializeHands();
            placeInitialBet();
            deal();
            playerTurnManager.playerTurn(dealer, player, deck);
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
            logger.trace("Reshuffling deck.");
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
        logger.trace("Player placed a bet of ${}.", this.roundBetSize);
        logger.trace("Player has ${} in their bankroll.", player.getBankroll());
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
            dealer.play(deck);
        }
    }

    private void payout() {
        int handNumber = 1;
        for (PlayerHand hand : player.getHands()) {
            logger.trace("Comparing player hand {} to dealer's hand.", handNumber);
            GameUtils.displayHands(dealer.getHand(), hand);

            // Analytics
            RoundResult result;

            // Bust
            if (hand.getHandValue() > 21) {
                logger.trace("Hand {} busted with a hand value of {}.", handNumber, hand.getHandValue());
                result = hand.getBet() == this.roundBetSize ? RoundResult.BUST : RoundResult.DOUBLE_BUST;
            }

            // Blackjack
            else if (GameUtils.isBlackjack(hand)) {
                logger.trace("Hand {} has a blackjack!", handNumber);
                if (GameUtils.isBlackjack(dealer.getHand())) {
                    logger.trace("Dealer also has a blackjack. Player pushed.");
                    player.addToBankroll(hand.getBet());
                    result = RoundResult.PUSH;
                } else {
                    player.addToBankroll(hand.getBet() * (rules.getBlackjackPayout() + 1));
                    logger.trace("Player won ${}.", hand.getBet() * (rules.getBlackjackPayout() + 1));
                    result = RoundResult.BLACKJACK;
                }
            }

            // Dealer bust
            else if (dealer.getHand().getHandValue() > 21) {
                logger.trace("Dealer busted with a hand value of {}.", dealer.getHand().getHandValue());
                player.addToBankroll(hand.getBet() * 2);
                logger.trace("Player hand {} won ${}.", handNumber, hand.getBet() * 2);
                result = hand.getBet() == this.roundBetSize ? RoundResult.WIN : RoundResult.DOUBLE_WIN;
            }

            // Compare hands
            else if (hand.getHandValue() > dealer.getHand().getHandValue()) {
                player.addToBankroll(hand.getBet() * 2);
                logger.trace("Player hand {} won ${}.", handNumber, hand.getBet() * 2);
                result = hand.getBet() == this.roundBetSize ? RoundResult.WIN : RoundResult.DOUBLE_WIN;
            } else if (hand.getHandValue() == dealer.getHand().getHandValue()) {
                player.addToBankroll(hand.getBet());
                logger.trace("Player hand {} pushed.", handNumber);
                result = RoundResult.PUSH;
            } else {
                logger.trace("Player hand {} lost ${}.", handNumber, hand.getBet());
                result = hand.getBet() == this.roundBetSize ? RoundResult.LOSE : RoundResult.DOUBLE_LOSE;
            }

            // Analytics
            analytics.recordRoundResult(roundNumber, result);
            handNumber++;
        }
    }
}
