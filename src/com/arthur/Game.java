package com.arthur;

import com.arthur.component.Card;
import com.arthur.component.Deck;
import com.arthur.player.Dealer;
import com.arthur.player.Player;

import java.util.*;

public class Game {
    private Deck deck;
    private Dealer dealer;
    private ArrayList<Player> players;
    private int minimumBet;

    public Game() {
        dealer = new Dealer();
        players = new ArrayList<>();
    }

    public void initializeGame() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Starting a game of Blackjack!");

        // Have user initialize game
        System.out.println("How many decks do you want to play with?");
        deck = new Deck(Integer.parseInt(scanner.nextLine()));

        System.out.println("What is the minimum bet?");
        minimumBet = Integer.parseInt(scanner.nextLine());

        System.out.println("How many players are playing?");
        int numOfPlayers = Integer.parseInt(scanner.nextLine());

        System.out.println("What is the starting bankroll?");
        int startingBankroll = Integer.parseInt(scanner.nextLine());

//        // Default settings for testing
//        deck = new Deck(4);
//        minimumBet = 10;
//        int numOfPlayers = 1;
//        int startingBankroll = 1000;

        for (int i = 0; i < numOfPlayers; i++) {
            players.add(new Player(i + 1, startingBankroll));
        }

        deck.shuffle();

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
        Scanner scanner = new Scanner(System.in);
        player.placeBet(hand);
        boolean hasStood = false;
        while (player.getHand(hand).getTotal() < 21 && !hasStood) {
            System.out.println("\nYour cards: " + player.getHand(hand));
            System.out.println("Your score: " + player.getHand(hand).getTotal());
            System.out.println("Dealer's upcard: " + dealer.getHand().getCards().get(0));

            String choice;
            if (player.canDouble(hand) && player.canSplit(hand)) {
                System.out.println("Do you want to hit, stand, double down, or split? (h/s/d/p)");
                choice = scanner.nextLine();
                Set<String> choices = new HashSet<>(Arrays.asList("h", "s", "d", "p"));
                while (!choices.contains(choice)) {
                    System.out.println("Invalid choice, pick again:");
                    choice = scanner.nextLine();
                }
            } else if (player.canSplit(hand)) {
                System.out.println("Do you want to hit, stand, or split? (h/s/p)");
                choice = scanner.nextLine();
                Set<String> choices = new HashSet<>(Arrays.asList("h", "s", "p"));
                while (!choices.contains(choice)) {
                    System.out.println("Invalid choice, pick again:");
                    choice = scanner.nextLine();
                }
            } else if (player.canDouble(hand)) {
                System.out.println("Do you want to hit, stand, or double down? (h/s/d)");
                choice = scanner.nextLine();
                Set<String> choices = new HashSet<>(Arrays.asList("h", "s", "d"));
                while (!choices.contains(choice)) {
                    System.out.println("Invalid choice, pick again:");
                    choice = scanner.nextLine();
                }
            } else {
                System.out.println("Do you want to hit or stand? (h/s)");
                choice = scanner.nextLine();
                Set<String> choices = new HashSet<>(Arrays.asList("h", "s"));
                while (!choices.contains(choice)) {
                    System.out.println("Invalid choice, pick again:");
                    choice = scanner.nextLine();
                }
            }

            switch (choice) {
                case "h" -> dealer.dealCard(player, hand, deck);
                case "s" -> hasStood = true;
                case "d" -> {
                    player.placeBet(hand);
                    int curBet = player.getHand(hand).getBet();
                    player.getHand(hand).setBet(curBet * 2);
                    dealer.dealCard(player, hand, deck);
                    hasStood = true;
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
        }
    }

    private void determineWinners() {
        System.out.println("\nDealer's cards: " + dealer.getHand());
        System.out.println("Dealer's score: " + dealer.getHand().getTotal() + "\n");

        List<Player> playersToRemove = new ArrayList<>();
        for (Player player : players) {
            payout(player, playersToRemove);
        }
        while (!playersToRemove.isEmpty()) {
            players.remove(playersToRemove.remove(playersToRemove.size() - 1));
        }
        dealer.clearHand();
    }

    private void payout(Player player, List<Player> playersToRemove) {
        int numOfHands = player.getNumOfHands();

        if (numOfHands > 1) {
            for (int i = 1; i <= numOfHands; i++) {
                System.out.println("Player " + player.getId() + " - Hand " + i + ": " + player.getHand(0));
                System.out.println("Player " + player.getId() + " - Hand " + i + " score: " + player.getHand(0).getTotal());
                if (player.getHand(0).getTotal() == 21 && player.getHand(0).getCards().size() == 2) {
                    System.out.println("Player " + player.getId() + " - Hand " + i  + " gets Blackjack!");
                    player.winBlackjack(0);
                } else if (player.getHand(0).getTotal() > 21) {
                    System.out.println("Player " + player.getId() + " - Hand " + i  + " busts!");
                } else if (dealer.getHand().getTotal() > 21 || player.getHand(0).getTotal() > dealer.getHand().getTotal()) {
                    System.out.println("Player " + player.getId() + " - Hand " + i  + " wins!");
                    player.winBet(0);
                } else if (player.getHand(0).getTotal() == dealer.getHand().getTotal()) {
                    System.out.println("Player " + player.getId() + " - Hand " + i  + " pushes.");
                    player.pushBet(0);
                } else {
                    System.out.println("Player " + player.getId() + " - Hand " + i  + " loses.");
                }
                System.out.println();
                player.clearHand(0);
            }
        } else {
            System.out.println("Player " + player.getId() + ": " + player.getHand(0));
            System.out.println("Player " + player.getId() + "'s score: " + player.getHand(0).getTotal());
            if (player.getHand(0).getTotal() == 21 && player.getHand(0).getCards().size() == 2) {
                System.out.println("Player " + player.getId() + "gets Blackjack!");
                player.winBlackjack(0);
            } else if (player.getHand(0).getTotal() > 21) {
                System.out.println("Player " + player.getId() + " busts!");
            } else if (dealer.getHand().getTotal() > 21 || player.getHand(0).getTotal() > dealer.getHand().getTotal()) {
                System.out.println("Player " + player.getId() + " wins!");
                player.winBet(0);
            } else if (player.getHand(0).getTotal() == dealer.getHand().getTotal()) {
                System.out.println("Player " + player.getId() + " pushes.");
                player.pushBet(0);
            } else {
                System.out.println("Player " + player.getId() + " loses.");
            }
            System.out.println();
            player.clearHand(0);
        }

        if (player.getMoney() <= 0) {
            System.out.println("Player " + player.getId() + " is out of money!");
            System.out.println("Player " + player.getId() + " is eliminated from the game!\n");
            playersToRemove.add(player);
        }
    }
}
