package com.arthur.blackjack.controller;

import com.arthur.blackjack.strategies.Strategy;

public interface PlayerTurnManager {
    void setStrategy(Strategy strategy);

    void playerTurn();
}
