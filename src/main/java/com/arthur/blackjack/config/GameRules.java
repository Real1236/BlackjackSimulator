package com.arthur.blackjack.config;

public interface GameRules {
    void setRule(Rule rule);
    int getNumOfDecks();
    boolean isStandsOnSoft17();
    boolean isDoubleAfterSplit();
    int getResplitLimit();
    boolean isResplitAces();
    boolean isHitSplitAces();
    boolean isLoseOnlyOGBetAgainstDealerBJ();
    boolean isSurrender();
    double getBlackjackPayout();
    boolean isDealerPeeks();
    double getDepthToReshuffle();
}
