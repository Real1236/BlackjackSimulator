package com.arthur.blackjack.core;

import com.arthur.blackjack.component.Deck;
import com.arthur.blackjack.player.Dealer;
import com.arthur.blackjack.player.Player;
import com.arthur.blackjack.player.PlayerFactory;
import com.arthur.blackjack.simulation.Action;
import com.arthur.blackjack.simulation.ResultsTracker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.arthur.blackjack.simulation.StrategyTableReader.readStrategyTable;

@Component
public class Game {
    private final ArrayList<Player> players;
    private final Map<String, Map<Integer, Map<Integer, Action>>> strategyTable;

    private int round;
    public static int totalBet;

    private final GameSettings gameSettings;
    private final Deck deck;
    private final Dealer dealer;
    private final PlayerFactory playerFactory;

    @Autowired
    public Game(GameSettings gameSettings, Deck deck, Dealer dealer, PlayerFactory playerFactory) {
        this.gameSettings = gameSettings;
        this.deck = deck;
        this.dealer = dealer;
        this.playerFactory = playerFactory;
        players = new ArrayList<>();
        strategyTable = readStrategyTable();
        round = 0;
        totalBet = 0;
    }

    public void initializeGame() {
        System.out.println("Starting a game of Blackjack!");

        for (int i = 1; i <= gameSettings.getNumOfPlayers(); i++) {
            Player player = playerFactory.createPlayer();
            player.setId(i);
            player.setMoney(gameSettings.getStartingBankroll());
            players.add(player);
        }

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
            player.takeTurn(dealer, deck);

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

    public Map<String, Map<Integer, Map<Integer, Action>>> getStrategyTable() {
        return strategyTable;
    }
}
