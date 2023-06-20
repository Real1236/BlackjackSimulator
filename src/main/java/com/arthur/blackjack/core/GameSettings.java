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
public class GameSettings {
    private int depthToReshuffle;
    private int numOfPlayers;
    private int startingBankroll;
    private int bet;
    private int numOfGames;

    @PostConstruct
    public void load() {
        depthToReshuffle = 0;
        numOfPlayers = 1;
        startingBankroll = 200000;
        bet = 20;
        numOfGames = 5;
    }
}
