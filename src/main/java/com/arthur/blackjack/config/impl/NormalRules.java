package com.arthur.blackjack.config.impl;

import com.arthur.blackjack.config.GameRules;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class NormalRules implements GameRules {
    private final int numOfDecks;
    private final boolean standsOnSoft17;
    private final boolean doubleAfterSplit;
    private final int resplitLimit;
    private final boolean resplitAces;
    private final boolean hitSplitAces;
    private final boolean loseOnlyOGBetAgainstDealerBJ;
    private final boolean surrender;
    private final double blackjackPayout;
    private final boolean dealerPeeks;
    private final double depthToReshuffle;

    public NormalRules() {
        this.numOfDecks = 6;
        this.standsOnSoft17 = true;
        this.doubleAfterSplit = true;
        this.resplitLimit = Integer.MAX_VALUE;
        this.resplitAces = true;
        this.hitSplitAces = true;
        this.loseOnlyOGBetAgainstDealerBJ = false; // TODO
        this.surrender = false; // TODO
        this.blackjackPayout = 1.5;
        this.dealerPeeks = true;
        this.depthToReshuffle = 0.5;
    }

}
