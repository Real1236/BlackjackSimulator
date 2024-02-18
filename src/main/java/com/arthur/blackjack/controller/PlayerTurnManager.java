package com.arthur.blackjack.controller;

import com.arthur.blackjack.models.card.Deck;
import com.arthur.blackjack.models.player.Dealer;
import com.arthur.blackjack.models.player.Player;
import com.arthur.blackjack.strategies.Strategy;

public interface PlayerTurnManager {
    void playerTurn(Dealer dealer, Player player, Deck deck);
    void setStrategy(Strategy strategy);
}
