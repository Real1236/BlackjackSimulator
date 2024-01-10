package com.arthur.blackjack.controller;

import com.arthur.blackjack.strategies.impl.AbstractStrategy;

public interface PlayerTurnManager {
    void setStrategy(AbstractStrategy strategy);
    void playerTurn();
}
