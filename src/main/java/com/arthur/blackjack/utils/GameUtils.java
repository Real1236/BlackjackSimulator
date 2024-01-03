package com.arthur.blackjack.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.arthur.blackjack.models.hand.DealerHand;
import com.arthur.blackjack.models.hand.Hand;
import com.arthur.blackjack.models.hand.PlayerHand;

public final class GameUtils {
    private static final Logger logger = LogManager.getLogger(GameUtils.class);

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

    public static void displayHandsHiddenUpcard(DealerHand dealerHand, PlayerHand playerHand) {
        logger.info("Dealer's hand: {} (Hidden).", dealerHand.getUpCard());

        StringBuilder playerHandString = new StringBuilder("Player's hand: ");
        for (int i = 0; i < playerHand.getCards().size(); i++) {
            playerHandString.append(playerHand.getCards().get(i));
            if (i < playerHand.getCards().size() - 1) {
                playerHandString.append(", ");
            }
        }
        logger.info(playerHandString.toString());
    }
}
