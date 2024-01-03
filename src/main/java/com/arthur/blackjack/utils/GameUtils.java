package com.arthur.blackjack.utils;

import com.arthur.blackjack.models.hand.DealerHand;
import com.arthur.blackjack.models.hand.Hand;

public final class GameUtils {
    private GameUtils() {}

    public static boolean playCondition(int bankroll, int roundNumber, int maxRounds) {
        return bankroll > 0 && roundNumber <= maxRounds;
    }

    public static boolean isBlackjack(Hand hand) {
        return hand.getHandValue() == 21 && hand.getCards().size() == 2;
    }

    public static boolean isOpen10Blackjack(DealerHand hand) {
        return isBlackjack(hand) && hand.getUpCard().getRank().getValue() == 10;
    }
}
