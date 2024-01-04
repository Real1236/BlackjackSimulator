package com.arthur.blackjack.controller;

import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.arthur.blackjack.config.GameSettings;
import com.arthur.blackjack.models.player.Dealer;
import com.arthur.blackjack.models.card.Deck;
import com.arthur.blackjack.models.hand.Hand;
import com.arthur.blackjack.models.hand.HandFactory;
import com.arthur.blackjack.models.hand.PlayerHand;
import com.arthur.blackjack.models.player.Player;
import com.arthur.blackjack.strategies.Strategy;
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

    private GameSettings settings;
    private Strategy playStrategy;

    public Game(Player player, Dealer dealer, Deck deck, HandFactory handFactory, GameSettings settings) {
        this.roundNumber = 1;
        this.player = player;
        this.dealer = dealer;
        this.deck = deck;
        this.handFactory = handFactory;
        this.settings = settings;
    }

    public void setStrategy(Strategy playStrategy) {
        this.playStrategy = playStrategy;
    }
    
    public void play() {
        logger.info("Starting a game of Blackjack!");
        deck.reshuffleDeck();

        // Loop to play game
        while (GameUtils.playCondition(player.getBankroll(), roundNumber, settings.getMaxRounds())) {
            logger.info("Starting round {}.\n---------------------------------", roundNumber++);
            logger.info("Player has ${} in their bankroll.", player.getBankroll());
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

    private void playerTurn() {
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

    private void dealerTurn() {
        // TODO: Implement dealerTurn
        return;
    }

    private void payout() {
        // TODO: Implement payout
        return;
    }
}
