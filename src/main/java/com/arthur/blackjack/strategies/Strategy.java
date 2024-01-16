package com.arthur.blackjack.strategies;

import com.arthur.blackjack.models.card.Card;
import com.arthur.blackjack.models.hand.Hand;

/**
 * The Strategy interface represents a strategy for playing blackjack.
 * It defines methods for making decisions during the game.
 */
public interface Strategy {

    /**
     * Returns the bet size for the current hand.
     *
     * @return the bet size
     */
    int getBetSize();

    /**
     * Determines whether to hit (draw another card) on the current hand.
     *
     * @return true if another card should be drawn, false otherwise
     */
    boolean hit(Hand playerHand, int dealerUpcardValue);

    /**
     * Determines whether to double down on the current hand.
     *
     * @return true if the hand should be doubled down, false otherwise
     */
    boolean doubleDown(Hand playerHand, int dealerUpcardValue);

    /**
     * Determines whether to split the current hand.
     *
     * @return true if the hand should be split, false otherwise
     */
    boolean split(int playerOneCardValue, int dealerUpcardValue);

    /**
     * Counts card that were dealt and decrements them from the Excel
     *
     * @param card the card that was dealt
     */
    void countCard(Card card);

    /**
     * Resets the deck composition in the Excel
     */
    void resetDeckComposition();

    /**
     * Returns file path of Excel that contains strategy logic
     *
     * @return String
     */
    String getFilePath();
}
