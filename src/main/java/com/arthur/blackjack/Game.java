package com.arthur.blackjack;

import com.arthur.blackjack.component.Deck;
import com.arthur.blackjack.player.Dealer;
import com.arthur.blackjack.player.Player;
import com.arthur.blackjack.simulation.Action;
import com.arthur.blackjack.simulation.ResultsTracker;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.arthur.blackjack.simulation.StrategyTableReader.readStrategyTable;

public class Game {
    private Deck deck;
    private final Dealer dealer;
    private final ArrayList<Player> players;
    private int minimumBet;
    public static final Map<String, Map<Integer, Map<Integer, Action>>> strategyTable = readStrategyTable();

    private int round;
    public static int totalBet;

    public Game() {
        dealer = new Dealer();
        players = new ArrayList<>();
        round = 0;
        totalBet = 0;
    }

    public void initializeGame() {
        System.out.println("Starting a game of Blackjack!");
//        Scanner scanner = new Scanner(System.in);
//
//        // Have user initialize game
//        System.out.println("How many decks do you want to play with?");
//        deck = new Deck(Integer.parseInt(scanner.nextLine()));
//
//        System.out.println("What deck penetration do you want to reach before reshuffling? (Answer between 0-100)");
//        deck.setDepthToReshuffle(Integer.parseInt(scanner.nextLine()));
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
        deck = new Deck(8);
        deck.setDepthToReshuffle(75);
        minimumBet = 10;
        int numOfPlayers = 1;
        int startingBankroll = 100000;

        for (int i = 1; i <= numOfPlayers; i++)
            players.add(new Player(i, startingBankroll));

        // Track results for player 1
        ResultsTracker resultsTracker = new ResultsTracker();
        resultsTracker.writeResults(0, players.get(0).getMoney());

        // Loop game until all players are bankrupt (they'll be deleted)
        while (players.size() > 0) {
            round++;
            startRound();
            if (!players.isEmpty())
                resultsTracker.writeResults(round, players.get(0).getMoney());
            else
                resultsTracker.writeResults(round, 0);
        }

        resultsTracker.saveExcel();
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
        for (Player player : players)
            player.takeTurn(minimumBet, dealer, deck);

        // Dealer takes its turn
        dealer.play(deck);

        // Determine the winners and pay out
        determineWinners();
        deck.checkReshuffle();
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
            player.evaluateHand(i, dealer);
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
