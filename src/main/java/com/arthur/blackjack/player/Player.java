package com.arthur.blackjack.player;

import com.arthur.blackjack.core.Game;
import com.arthur.blackjack.component.Card;
import com.arthur.blackjack.component.Deck;
import com.arthur.blackjack.component.Hand;
import com.arthur.blackjack.core.GameRules;
import com.arthur.blackjack.core.GameSettings;
import com.arthur.blackjack.simulation.Action;
import com.arthur.blackjack.simulation.RoundResult;
import com.arthur.blackjack.simulation.StrategyTableReader;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;

@Data
@Component
@Scope("prototype")
public class Player {
    private int id;
    private final List<Hand> hands;
    private int money;

    private final GameSettings gameSettings;
    private final GameRules gameRules;
    private final StrategyTableReader strategyTableReader;

    @Autowired
    public Player(GameSettings gameSettings, GameRules gameRules, StrategyTableReader strategyTableReader) {
        this.gameSettings = gameSettings;
        this.gameRules = gameRules;
        this.strategyTableReader = strategyTableReader;
        hands = new ArrayList<>();
    }

    public void placeBet(int hand) {
        if (getHand(hand).getBet() <= money) {
            money -= getHand(hand).getBet();
            Game.totalBet += getHand(hand).getBet();
        } else {
            System.out.println("Error: not enough money to place bet.");
        }
    }

    public void winBet(int hand) {
        money += 2 * getHand(hand).getBet();
    }

    public void winBlackjack(int hand) {
        money += 2.5 * getHand(hand).getBet();
    }

    public void pushBet(int hand) {
        money += getHand(hand).getBet();
    }

    public void addCard(Card card) {
        getHand().addCard(card);
    }

    public void addCard(Card card, int hand) {
        getHand(hand).addCard(card);
    }

    public void addHand() {
        hands.add(new Hand());
    }

    public void addHand(int bet) {
        hands.add(new Hand(bet));
    }

    public void addHand(Hand hand) {
        hands.add(hand);
    }

    public Hand getHand() {
        return hands.get(hands.size() - 1);
    }

    public Hand getHand(int hand) {
        return hands.get(hand);
    }

    public int getNumOfHands() {
        return hands.size();
    }

    public void clearHand(int index) {
        hands.remove(index);
    }

    public boolean canSplit(int handIndex) {
        Hand hand = getHand(handIndex);
        List<Card> cards = hand.getCards();
        return hands.size() < gameRules.getResplitLimit()
                && cards.size() == 2
                && cards.get(0).getRank() == cards.get(1).getRank()
                && hand.getBet() <= money;
    }

    public boolean canDouble(int handIndex) {
        boolean rules = true;
        if (hands.size() > 1)
            rules = gameRules.isDoubleAfterSplit();

        Hand hand = getHand(handIndex);
        List<Card> cards = hand.getCards();
        return rules && cards.size() == 2 && hand.getBet() <= money;
    }

    public void takeTurn(Dealer dealer, Deck deck) {
        System.out.println("\nPlayer " + getId() + "'s turn");
        System.out.println("Money: " + getMoney());
        System.out.println("\nPlace your bet:");
        getHand().setBet(gameSettings.getBet());
        playHand(0, dealer, deck);
    }

    public void playHand(int hand, Dealer dealer, Deck deck) {
        placeBet(hand);
        boolean hasStood = false;

        while (getHand(hand).getTotal() < 21 && !hasStood) {
            System.out.println("\nYour cards: " + getHand(hand));
            System.out.println("Your score: " + getHand(hand).getTotal());
            System.out.println("Dealer's upcard: " + dealer.getUpcard());

            Action choice = getPlayerChoice(hand, dealer.getUpcard().getValue());
            hasStood = performPlayerAction(choice, hand, dealer, deck);
        }
    }

    public Action getPlayerChoice(int handIndex, int upcardValue) {
        Set<Action> choices = getChoices(handIndex);
        Hand hand = getHand(handIndex);

        System.out.println("Choose an option: " + choices);
        Action choice = null;
        if (choices.contains(Action.SPLIT)) {
            Map<Integer, Map<Integer, Action>> splitTable = strategyTableReader.getStrategyTable().get("Split");
            int cardValue = hand.getCards().get(0).getValue();
            choice = splitTable.get(cardValue).get(upcardValue);
        }
        if (choice == null) {
            if (hand.isHard()) {
                Map<Integer, Map<Integer, Action>> hardTable = strategyTableReader.getStrategyTable().get("Hard");
                choice = hardTable.get(hand.getTotal()).get(upcardValue);
            } else {
                Map<Integer, Map<Integer, Action>> softTable = strategyTableReader.getStrategyTable().get("Soft");
                choice = softTable.get(hand.getTotal()).get(upcardValue);
            }
        }

        return choice;
    }

    private Set<Action> getChoices(int hand) {
        Set<Action> choices = new HashSet<>();
        choices.add(Action.HIT);
        choices.add(Action.STAND);
        if (canDouble(hand))
            choices.add(Action.DOUBLE_DOWN);
        if (canSplit(hand))
            choices.add(Action.SPLIT);
        return choices;
    }

    public boolean performPlayerAction(Action choice, int hand, Dealer dealer, Deck deck) {
        switch (choice) {
            case SURRENDER, HIT -> dealer.dealCard(this, hand, deck);   // TODO add surrender capability
            case STAND -> { return true; }
            case DOUBLE_STAND -> {
                if (canDouble(hand)) {
                    placeBet(hand);
                    int curBet = getHand(hand).getBet();
                    getHand(hand).setBet(curBet * 2);
                    dealer.dealCard(this, hand, deck);
                }
                return true;
            }
            case DOUBLE_DOWN -> {
                if (canDouble(hand)) {
                    placeBet(hand);
                    int newBet = getHand(hand).getBet() * 2;
                    getHand(hand).setBet(newBet);
                    dealer.dealCard(this, hand, deck);
                    return true;
                } else {
                    dealer.dealCard(this, hand, deck);
                }
            }
            case SPLIT -> {
                Card temp = getHand(hand).removeCard();
                dealer.dealCard(this, hand, deck);
                addHand(getHand(hand).getBet());
                getHand().addCard(temp);
                dealer.dealCard(this, deck);
                playHand(getNumOfHands() - 1, dealer, deck);
            }
            default -> System.out.println("Invalid choice. Please choose 'h', 's', or 'p'.");
        }
        return false;
    }

    public RoundResult evaluateHand(int handIndex, Dealer dealer, int numOfHands) {
        Hand hand = getHand(0);
        System.out.println("Player " + getId() + " - Hand " + handIndex + ": " + hand);
        System.out.println("Player " + getId() + " - Hand " + handIndex + " score: " + hand.getTotal());

        if (numOfHands == 1 && hand.isBlackjack() && !dealer.getHand().isBlackjack()) {
            System.out.println("Player " + getId() + " - Hand " + handIndex + " gets Blackjack!");
            winBlackjack(0);
            return RoundResult.BLACKJACK;
        } else if (hand.getTotal() > 21) {
            System.out.println("Player " + getId() + " - Hand " + handIndex + " busts!");
            return hand.getBet() == gameSettings.getBet() ? RoundResult.BUST : RoundResult.DOUBLE_BUST;
        } else if (dealer.getHand().getTotal() > 21 || hand.getTotal() > dealer.getHand().getTotal()) {
            System.out.println("Player " + getId() + " - Hand " + handIndex + " wins!");
            winBet(0);
            return hand.getBet() == gameSettings.getBet() ? RoundResult.WIN : RoundResult.DOUBLE_WIN;
        } else if (hand.getTotal() == dealer.getHand().getTotal()) {
            System.out.println("Player " + getId() + " - Hand " + handIndex + " pushes.");
            pushBet(0);
            return RoundResult.PUSH;
        } else {
            System.out.println("Player " + getId() + " - Hand " + handIndex + " loses.");
            return hand.getBet() == gameSettings.getBet() ? RoundResult.LOSE : RoundResult.DOUBLE_LOSE;
        }
    }
}
