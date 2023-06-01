package com.arthur.blackjack.core;

public class GameRules {
    public static final int numOfDecks;
    public static final boolean standsOnSoft17;
    public static final boolean doubleAfterSplit;
    public static final int resplitLimit;
    public static final boolean resplitAces;
    public static final boolean hitSplitAces;
    public static final boolean loseOnlyOGBetAgainstDealerBJ;
    public static final boolean surrender;
    public static final double blackjackPayout;

    static {
        numOfDecks = 8;
        standsOnSoft17 = true;
        doubleAfterSplit = false;
        resplitLimit = 2;
        resplitAces = false;
        hitSplitAces = false;
        loseOnlyOGBetAgainstDealerBJ = false;
        surrender = false;
        blackjackPayout = 3.0/2.0;
    }
}
