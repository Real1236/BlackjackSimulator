package com.arthur.blackjack.core;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class GameRules {
    private int numOfDecks;
    private boolean standsOnSoft17;
    private boolean doubleAfterSplit;
    private int resplitLimit;
    private boolean resplitAces;
    private boolean hitSplitAces;
    private boolean loseOnlyOGBetAgainstDealerBJ;
    private boolean surrender;
    private double blackjackPayout;

    @PostConstruct
    public void load() {
        numOfDecks = 8;
        standsOnSoft17 = true;
        doubleAfterSplit = false;
        resplitLimit = 2;
        resplitAces = true;
        hitSplitAces = false;
        loseOnlyOGBetAgainstDealerBJ = true;
        surrender = false;
        blackjackPayout = 3.0/2.0;
    }
}
