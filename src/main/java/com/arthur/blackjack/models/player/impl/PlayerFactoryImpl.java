package com.arthur.blackjack.models.player.impl;

import com.arthur.blackjack.config.GameRules;
import com.arthur.blackjack.config.GameSettings;
import com.arthur.blackjack.models.player.Dealer;
import com.arthur.blackjack.models.player.Player;
import com.arthur.blackjack.models.player.PlayerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PlayerFactoryImpl implements PlayerFactory {

    private final GameSettings settings;
    private final GameRules rules;

    @Autowired
    public PlayerFactoryImpl(GameSettings settings, GameRules rules) {
        this.settings = settings;
        this.rules = rules;
    }

    public Player createPlayer() {
        return new PlayerImpl(settings);
    }

    public Dealer createDealer() {
        return new DealerImpl(rules);
    }
}
