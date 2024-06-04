package com.arthur.blackjack.config.impl;

import com.arthur.blackjack.config.GameRules;
import com.arthur.blackjack.config.Rule;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Properties;

@Component
@Getter
public class GameRulesImpl implements GameRules {
    private static final Logger logger = LogManager.getLogger(GameRulesImpl.class);
    private final Properties rules;

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

    public GameRulesImpl() {
        rules = new Properties();
    }

    @Override
    public void setRule(Rule rule) {
        String path = rule.getPath();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(path)) {
            rules.load(input);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        this.numOfDecks = Integer.parseInt(rules.getProperty("numOfDecks"));
        this.standsOnSoft17 = Boolean.parseBoolean(rules.getProperty("standsOnSoft17"));
        this.doubleAfterSplit = Boolean.parseBoolean(rules.getProperty("doubleAfterSplit"));
        this.resplitLimit = Integer.parseInt(rules.getProperty("resplitLimit")) == -1 ? Integer.MAX_VALUE : Integer.parseInt(rules.getProperty("resplitLimit"));
        this.resplitAces = Boolean.parseBoolean(rules.getProperty("resplitAces"));
        this.hitSplitAces = Boolean.parseBoolean(rules.getProperty("hitSplitAces"));
        this.loseOnlyOGBetAgainstDealerBJ = Boolean.parseBoolean(rules.getProperty("loseOnlyOGBetAgainstDealerBJ"));
        this.surrender = Boolean.parseBoolean(rules.getProperty("surrender"));
        this.blackjackPayout = Double.parseDouble(rules.getProperty("blackjackPayout"));
        this.dealerPeeks = Boolean.parseBoolean(rules.getProperty("dealerPeeks"));
        this.depthToReshuffle = Double.parseDouble(rules.getProperty("depthToReshuffle"));
    }
}
