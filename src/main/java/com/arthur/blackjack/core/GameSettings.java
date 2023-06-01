package com.arthur.blackjack.core;

public class GameSettings {
    public static final int depthToReshuffle;
    public static final int numOfPlayers;
    public static final int startingBankroll;
    public static final int bet;

    static {
        depthToReshuffle = 75;
        numOfPlayers = 1;
        startingBankroll = 100000;
        bet = 20;
    }
}
