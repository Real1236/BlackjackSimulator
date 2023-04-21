import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

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
        playHand(player, 0, bet);
    }

    private void playHand(Player player, int hand, int bet) {
        Scanner scanner = new Scanner(System.in);
        player.placeBet(bet);
        boolean hasStood = false;
        while (player.getHand(hand).getTotal() < 21 && !hasStood) {
            System.out.println("\nYour cards: " + player.getHand(hand));
            System.out.println("Your score: " + player.getHand(hand).getTotal());
            System.out.println("Dealer's card: " + dealer.getHand().getCards().get(0));

            String choice;
            if (player.canSplit(hand)) {
                System.out.println("Do you want to hit, stand, or split? (h/s/p)");
                choice = scanner.nextLine();
                while (!Objects.equals(choice, "h") && !Objects.equals(choice, "s") && !Objects.equals(choice, "p")) {
                    System.out.println("Invalid choice, pick again:");
                    choice = scanner.nextLine();
                }
            } else {
                System.out.println("Do you want to hit or stand? (h/s)");
                choice = scanner.nextLine();
                while (!Objects.equals(choice, "h") && !Objects.equals(choice, "s")) {
                    System.out.println("Invalid choice, pick again:");
                    choice = scanner.nextLine();
                }
            }

            switch (choice) {
                case "h" -> dealer.dealCard(player, hand, deck);
                case "s" -> hasStood = true;
                case "p" -> {
                    Card temp = player.getHand(hand).removeCard();
                    dealer.dealCard(player, hand, deck);
                    player.addHand();
                    player.getHand().addCard(temp);
                    dealer.dealCard(player, deck);
                    playHand(player, player.getNumOfHands() - 1, bet);
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
            displayHands(player, playersToRemove);
        }
        while (!playersToRemove.isEmpty()) {
            players.remove(playersToRemove.remove(playersToRemove.size() - 1));
        }
        dealer.clearHand();
    }

    private void displayHands(Player player, List<Player> playersToRemove) {
        int numOfHands = player.getNumOfHands();

        if (numOfHands > 1) {
            for (int i = 1; i <= numOfHands; i++) {
                System.out.println("Player " + player.getId() + " - Hand " + i + ": " + player.getHand(0));
                System.out.println("Player " + player.getId() + " - Hand " + i + " score: " + player.getHand(0).getTotal());
                if (player.getHand(0).getTotal() > 21) {
                    System.out.println("Player " + player.getId() + " - Hand " + i  + " busts!");
                } else if (dealer.getHand().getTotal() > 21 || player.getHand(0).getTotal() > dealer.getHand().getTotal()) {
                    System.out.println("Player " + player.getId() + " - Hand " + i  + " wins!");
                    player.winBet();
                } else if (player.getHand(0).getTotal() == dealer.getHand().getTotal()) {
                    System.out.println("Player " + player.getId() + " - Hand " + i  + " pushes.");
                    player.pushBet();
                } else {
                    System.out.println("Player " + player.getId() + " - Hand " + i  + " loses.");
                }
                System.out.println();
                player.clearHand(0);
            }
        } else {
            System.out.println("Player " + player.getId() + ": " + player.getHand(0));
            System.out.println("Player " + player.getId() + "'s score: " + player.getHand(0).getTotal());
            if (player.getHand(0).getTotal() > 21) {
                System.out.println("Player " + player.getId() + " busts!");
            } else if (dealer.getHand().getTotal() > 21 || player.getHand(0).getTotal() > dealer.getHand().getTotal()) {
                System.out.println("Player " + player.getId() + " wins!");
                player.winBet();
            } else if (player.getHand(0).getTotal() == dealer.getHand().getTotal()) {
                System.out.println("Player " + player.getId() + " pushes.");
                player.pushBet();
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
