package com.arthur.blackjack;

import com.arthur.blackjack.component.Card;
import com.arthur.blackjack.component.Deck;
import com.arthur.blackjack.component.Hand;
import com.arthur.blackjack.player.Dealer;
import com.arthur.blackjack.player.Player;

import java.util.*;

public class Game {
    private Deck deck;
    private final Dealer dealer;
    private final ArrayList<Player> players;
    private int minimumBet;

    public Game() {
        dealer = new Dealer();
        players = new ArrayList<>();
    }

    public void initializeGame() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Starting a game of Blackjack!");

//        // Have user initialize game
//        System.out.println("How many decks do you want to play with?");
//        deck = new Deck(Integer.parseInt(scanner.nextLine()));
//
//        System.out.println("What is the minimum bet?");
//        minimumBet = Integer.parseInt(scanner.nextLine());
//
//        System.out.println("How many players are playing?");
//        int numOfPlayers = Integer.parseInt(scanner.nextLine());
//
//        System.out.println("What is the starting bankroll?");
//        int startingBankroll = Integer.parseInt(scanner.nextLine());

        // Default settings for testing
        deck = new Deck(4);
        minimumBet = 10;
        int numOfPlayers = 1;
        int startingBankroll = 1000;

        for (int i = 0; i < numOfPlayers; i++) {
            players.add(new Player(i + 1, startingBankroll));
        }

        // Loop game until all players are bankrupt (they'll be deleted)
        while (players.size() > 0) {
            startRound();
        }
        System.out.println("GG");
    }

    public void startRound() {
        // Deal two cards to each player and the dealer
        for (Player player : players) {
            player.addHand();
            dealer.dealCard(player, deck);
            dealer.dealCard(player, deck);
        }
        dealer.dealCardToDealer(deck);
        dealer.dealCardToDealer(deck);

        // Players take their turns
        for (Player player : players) {
            takeTurn(player);
        }

        // Dealer takes its turn
        dealer.play(deck);

        // Determine the winners and pay out
        determineWinners();
    }

    private void takeTurn(Player player) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nPlayer " + player.getId() + "'s turn");
        System.out.println("Money: " + player.getMoney());
        System.out.println("\nPlace your bet:");
        int bet = Integer.parseInt(scanner.nextLine());
        while (bet < minimumBet) {
            System.out.println("Place a bet greater than or equal to the minimum bet:");
            bet = Integer.parseInt(scanner.nextLine());
        }
        player.getHand().setBet(bet);
        playHand(player, 0);
    }

    private void playHand(Player player, int hand) {
        player.placeBet(hand);
        boolean hasStood = false;

        while (player.getHand(hand).getTotal() < 21 && !hasStood) {
            System.out.println("\nYour cards: " + player.getHand(hand));
            System.out.println("Your score: " + player.getHand(hand).getTotal());
            System.out.println("Dealer's upcard: " + dealer.getHand().getCards().get(0));

            String choice = getPlayerChoice(player, hand);
            hasStood = performPlayerAction(player, choice, hand);
        }
    }

    private String getPlayerChoice(Player player, int hand) {
        Scanner scanner = new Scanner(System.in);
        Map<String, String> choices = getChoices(player, hand);

        System.out.println("Choose an option: " + choices + "");
        String choice = scanner.nextLine();

        while (!choices.containsKey(choice)) {
            System.out.println("Invalid choice, pick again:");
            choice = scanner.nextLine();
        }

        return choice;
    }

    private Map<String, String> getChoices(Player player, int hand) {
        Map<String, String> choices = new HashMap<>();
        choices.put("h", "hit");
        choices.put("s", "stand");
        if (player.canDouble(hand))
            choices.put("d", "double down");
        if (player.canSplit(hand))
            choices.put("p", "split");
        return choices;
    }

    private boolean performPlayerAction(Player player, String choice, int hand) {
        switch (choice) {
            case "h" -> dealer.dealCard(player, hand, deck);
            case "s" -> { return true; }
            case "d" -> {
                player.placeBet(hand);
                int curBet = player.getHand(hand).getBet();
                player.getHand(hand).setBet(curBet * 2);
                dealer.dealCard(player, hand, deck);
                return true;
            }
            case "p" -> {
                Card temp = player.getHand(hand).removeCard();
                dealer.dealCard(player, hand, deck);
                player.addHand(player.getHand(hand).getBet());
                player.getHand().addCard(temp);
                dealer.dealCard(player, deck);
                playHand(player, player.getNumOfHands() - 1);
            }
            default -> System.out.println("Invalid choice. Please choose 'h', 's', or 'p'.");
        }
        return false;
    }

    private void determineWinners() {
        System.out.println("\nDealer's cards: " + dealer.getHand());
        System.out.println("Dealer's score: " + dealer.getHand().getTotal() + "\n");

        List<Player> playersToRemove = new ArrayList<>();
        for (Player player : players)
            payout(player, playersToRemove);
        while (!playersToRemove.isEmpty())
            players.remove(playersToRemove.remove(playersToRemove.size() - 1));

        dealer.clearHand();
    }

    private void payout(Player player, List<Player> playersToRemove) {
        int numOfHands = player.getNumOfHands();

        for (int i = 1; i <= numOfHands; i++) {
            evaluateHand(player, i);
            player.clearHand(0);
        }

        if (player.getMoney() <= 0) {
            System.out.println("Player " + player.getId() + " is out of money!");
            System.out.println("Player " + player.getId() + " is eliminated from the game!\n");
            playersToRemove.add(player);
        }
    }

    private void evaluateHand(Player player, int handIndex) {
        Hand hand = player.getHand(0);
        System.out.println("Player " + player.getId() + " - Hand " + handIndex + ": " + hand);
        System.out.println("Player " + player.getId() + " - Hand " + handIndex + " score: " + hand.getTotal());

        if (hand.getTotal() == 21 && hand.getCards().size() == 2) {
            System.out.println("Player " + player.getId() + " - Hand " + handIndex + " gets Blackjack!");
            player.winBlackjack(0);
        } else if (hand.getTotal() > 21) {
            System.out.println("Player " + player.getId() + " - Hand " + handIndex + " busts!");
        } else if (dealer.getHand().getTotal() > 21 || hand.getTotal() > dealer.getHand().getTotal()) {
            System.out.println("Player " + player.getId() + " - Hand " + handIndex + " wins!");
            player.winBet(0);
        } else if (hand.getTotal() == dealer.getHand().getTotal()) {
            System.out.println("Player " + player.getId() + " - Hand " + handIndex + " pushes.");
            player.pushBet(0);
        } else {
            System.out.println("Player " + player.getId() + " - Hand " + handIndex + " loses.");
        }

        System.out.println();
    }

}