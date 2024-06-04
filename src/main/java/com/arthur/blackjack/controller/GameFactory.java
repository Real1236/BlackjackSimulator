package com.arthur.blackjack.controller;

import com.arthur.blackjack.analytics.AnalyticsType;
import com.arthur.blackjack.strategies.StrategyType;

public interface GameFactory {
    Game createGame(int gameNum);
    void setStrategyType(StrategyType strategyType);
    void setAnalyticsType(AnalyticsType analyticsType);
}
