package com.arthur.blackjack.controller;

public interface GameFactory {
    Game createGame(int gameNum);
    void setStrategyType(String strategyType);
    void setAnalyticsType(String analyticsType);
}
