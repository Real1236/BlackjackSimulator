package com.arthur.blackjack.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.arthur.blackjack.models.card.Card;
import com.arthur.blackjack.models.card.Rank;
import com.arthur.blackjack.models.hand.DealerHand;
import com.arthur.blackjack.models.hand.Hand;
import com.arthur.blackjack.models.hand.PlayerHand;

public final class GameUtils {
    private static final Logger logger = LogManager.getLogger(GameUtils.class);

    private GameUtils() {}

    public static boolean playCondition(double bankroll, int roundNumber, int maxRounds) {
        return bankroll > 0 && roundNumber <= maxRounds;
    }

    public static boolean isBlackjack(Hand hand) {
        return hand.getHandValue() == 21 && hand.getCards().size() == 2;
    }

    public static boolean isOpen10Blackjack(DealerHand hand) {
        return isBlackjack(hand) && hand.getUpCard().getRank().getValue() == 10;
    }

    public static boolean isHard(Hand hand) {
        int total = 0;
        int numAces = 0;
        for (Card card : hand.getCards()) {
            total += card.getRank().getValue();
            if (card.getRank() == Rank.ACE)
                numAces++;
        }
        int numAcesDeducted = 0;
        while (numAcesDeducted < numAces && total > 21) {
            total -= 10;
            numAcesDeducted++;
        }
        return numAces == numAcesDeducted;
    }

    public static void displayHandsHiddenUpcard(DealerHand dealerHand, PlayerHand playerHand) {
        logger.info("Dealer's hand: {} (Hidden).", dealerHand.getUpCard());
        logger.info("Dealer's hand value: {}.", dealerHand.getUpCard().getRank().getValue());
        logger.info(buildHand("Player", playerHand));
        logger.info("Player's hand value: {}.", playerHand.getHandValue());
    }

    public static void displayHands(Hand dealerHand, Hand playerHand) {
        logger.info(buildHand("Dealer", dealerHand));
        logger.info("Dealer's hand value: {}.", dealerHand.getHandValue());
        logger.info(buildHand("Player", playerHand));
        logger.info("Player's hand value: {}.", playerHand.getHandValue());
    }

    private static String buildHand(String dealerOrPlayer, Hand hand) {
        StringBuilder handString = new StringBuilder(dealerOrPlayer + "'s hand: ");
        for (int i = 0; i < hand.getCards().size(); i++) {
            handString.append(hand.getCards().get(i));
            if (i < hand.getCards().size() - 1)
                handString.append(", ");
        }
        return handString.toString();
    }
}
