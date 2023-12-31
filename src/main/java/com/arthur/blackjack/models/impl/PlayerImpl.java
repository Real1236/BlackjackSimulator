package com.arthur.blackjack.models.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.arthur.blackjack.models.Hand;
import com.arthur.blackjack.models.Player;

@Component
public class PlayerImpl implements Player {

    private int bankroll;
    private List<Hand> hands;

    public PlayerImpl() {
        this.bankroll = 1000;   // TODO: make this configurable
        this.hands = new ArrayList<Hand>();
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
    public List<Hand> getHands() {
        return this.hands;
    }

    @Override
    public void addHand(Hand hand) {
        this.hands.add(hand);
    }

    @Override
    public void clearHands() {
        this.hands.clear();
    }
    
}
