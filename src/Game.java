import java.util.ArrayList;
import java.util.Scanner;

public class Game {
    private Deck deck;
    private Dealer dealer;
    private ArrayList<Player> players;
    private int minimumBet;

    public Game() {
        dealer = new Dealer();
        players = new ArrayList<Player>();
    }

    public void initializeGame() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Starting a game of Blackjack!");

        System.out.println("How many decks do you want to play with?");
        deck = new Deck(Integer.parseInt(scanner.nextLine()));

        System.out.println("What is the minimum bet?");
        minimumBet = Integer.parseInt(scanner.nextLine());

        System.out.println("How many players are playing?");
        int numOfPlayers = Integer.parseInt(scanner.nextLine());

        System.out.println("What is the starting bankroll?");
        int startingBankroll = Integer.parseInt(scanner.nextLine());
        for (int i = 0; i < numOfPlayers; i++) {
            players.add(new Player(startingBankroll));
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
        boolean hasStood = false;
        boolean hasSplit = false;

        System.out.println("\nPlayer " + (players.indexOf(player) + 1) + "'s turn");
        System.out.println("Money: " + player.getMoney());
        System.out.println("\nPlace your bet:");
        int bet = Integer.parseInt(scanner.nextLine());
        while (bet < minimumBet) {
            System.out.println("Place a bet greater than or equal to the minimum bet:");
            bet = Integer.parseInt(scanner.nextLine());
        }
        player.placeBet(bet);

        while (player.getHand().getTotal() < 21 && !hasStood) {
            System.out.println("\nYour cards: " + player.getHand());
            System.out.println("Your score: " + player.getHand().getTotal());
            System.out.println("Dealer's card: " + dealer.getHand().getCards().get(0));
            System.out.println("Do you want to hit, stand, or split? (h/s/p)");
            String choice = scanner.nextLine();
            if (choice.equals("h")) {
                dealer.dealCard(player, deck);
            } else if (choice.equals("s")) {
                hasStood = true;
            } else if (choice.equals("p")) {
//                if (player.canSplit()) {
//                    hasSplit = true;
//                    Player newPlayer = new Player();
//                    newPlayer.receiveCard(player.getHand().get(1));
//                    player.receiveCard(deck.deal());
//                    newPlayer.receiveCard(deck.deal());
//                    players.add(players.indexOf(player) + 1, newPlayer);
//                    takeTurn(player);
//                } else {
//                    System.out.println("You cannot split with these cards.");
//                }
            } else {
                System.out.println("Invalid choice. Please choose 'h', 's', or 'p'.");
            }
            if (hasSplit) {
                takeTurn(player);
            }
        }
    }


    private void determineWinners() {
        System.out.println("\nDealer's cards: " + dealer.getHand());
        System.out.println("Dealer's score: " + dealer.getHand().getTotal());

        for (Player player : players) {
            System.out.println("Player " + (players.indexOf(player) + 1) + "'s cards: " + player.getHand());
            System.out.println("Player " + (players.indexOf(player) + 1) + "'s score: " + player.getHand().getTotal());
            if (player.getHand().getTotal() > 21) {
                System.out.println("Player " + (players.indexOf(player) + 1) + " busts!");
            } else if (dealer.getHand().getTotal() > 21 || player.getHand().getTotal() > dealer.getHand().getTotal()) {
                System.out.println("Player " + (players.indexOf(player) + 1) + " wins!");
                player.winBet();
            } else if (player.getHand().getTotal() == dealer.getHand().getTotal()) {
                System.out.println("Player " + (players.indexOf(player) + 1) + " pushes.");
                player.pushBet();
            } else {
                System.out.println("Player " + (players.indexOf(player) + 1) + " loses.");
            }
            player.clearHand();
        }
        dealer.clearHand();
    }
}
