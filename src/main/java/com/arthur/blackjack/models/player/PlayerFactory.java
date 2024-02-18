package com.arthur.blackjack.models.player;

public interface PlayerFactory {
    Player createPlayer();
    Dealer createDealer();
}
