package com.arthur.blackjack.models.player.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.arthur.blackjack.config.GameSettings;
import com.arthur.blackjack.models.hand.PlayerHand;
import com.arthur.blackjack.models.player.Player;

@Component
public class PlayerImpl implements Player {

    private int bankroll;
    private List<PlayerHand> hands;

    public PlayerImpl(GameSettings settings) {
        this.bankroll = settings.getBankroll();
        this.hands = new ArrayList<PlayerHand>();
    }

    @Override
    public int getBankroll() {
        return this.bankroll;
    }

    @Override
    public void subtractFromBankroll(int amount) {
        this.bankroll -= amount;
    }

    @Override
    public void addToBankroll(int amount) {
        this.bankroll += amount;
    }

    @Override
    public List<PlayerHand> getHands() {
        return this.hands;
    }

    @Override
    public void addHand(PlayerHand hand) {
        this.hands.add(hand);
    }

    @Override
    public void clearHands() {
        this.hands.clear();
    }
    
}
