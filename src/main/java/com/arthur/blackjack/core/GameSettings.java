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

    @PostConstruct
    public void load() {
        depthToReshuffle = 50;
        numOfPlayers = 1;
        startingBankroll = 25000;
        bet = 20;
    }
}
