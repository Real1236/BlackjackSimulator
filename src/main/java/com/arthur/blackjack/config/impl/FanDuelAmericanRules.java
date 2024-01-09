package com.arthur.blackjack.config.impl;

import org.springframework.stereotype.Component;

import com.arthur.blackjack.config.GameRules;

@Component
public class FanDuelAmericanRules implements GameRules {
    private int numOfDecks;
    private boolean standsOnSoft17;
    private boolean doubleAfterSplit;
    private int resplitLimit;
    private boolean resplitAces;
    private boolean hitSplitAces;
    private boolean loseOnlyOGBetAgainstDealerBJ;
    private boolean surrender;
    private double blackjackPayout;
    private boolean dealerPeeks;
    private double depthToReshuffle;

    public FanDuelAmericanRules() {
        this.numOfDecks = 8;
        this.standsOnSoft17 = false;
        this.doubleAfterSplit = true;
        this.resplitLimit = 2;
        this.resplitAces = false; // TODO
        this.hitSplitAces = false; // TODO
        this.loseOnlyOGBetAgainstDealerBJ = false; // TODO
        this.surrender = false; // TODO
        this.blackjackPayout = 1.5;
        this.dealerPeeks = true;
        this.depthToReshuffle = 0.5;
    }

    public int getNumOfDecks() {
        return numOfDecks;
    }

    public boolean isStandsOnSoft17() {
        return standsOnSoft17;
    }

    public boolean isDoubleAfterSplit() {
        return doubleAfterSplit;
    }

    public int getResplitLimit() {
        return resplitLimit;
    }

    public boolean isResplitAces() {
        return resplitAces;
    }

    public boolean isHitSplitAces() {
        return hitSplitAces;
    }

    public boolean isLoseOnlyOGBetAgainstDealerBJ() {
        return loseOnlyOGBetAgainstDealerBJ;
    }

    public boolean isSurrender() {
        return surrender;
    }

    public double getBlackjackPayout() {
        return blackjackPayout;
    }

    public boolean isDealerPeeks() {
        return dealerPeeks;
    }

    public double getDepthToReshuffle() {
        return depthToReshuffle;
    }
}
