package com.arthur.blackjack.core;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class GameSettings {
    private int depthToReshuffle;
    private int numOfPlayers;
    private int startingBankroll;
    private int bet;

    public GameSettings() {
    }

    @PostConstruct
    public void load() {
        depthToReshuffle = 75;
        numOfPlayers = 1;
        startingBankroll = 50000;
        bet = 20;
    }
}
