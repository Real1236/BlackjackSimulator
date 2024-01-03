package com.arthur.blackjack.utils;

import com.arthur.blackjack.models.hand.Hand;

public final class GameUtils {
    private GameUtils() {}

    public static boolean playCondition(int bankroll, int roundNumber, int maxRounds) {
        return bankroll > 0 && roundNumber <= maxRounds;
    }

    public static boolean isBlackjack(Hand hand) {
        return hand.getHandValue() == 21 && hand.getCards().size() == 2;
    }
}
