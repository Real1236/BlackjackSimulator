package com.arthur.blackjack.strategies;

/**
 * The Strategy interface represents a strategy for playing blackjack.
 * It defines methods for making decisions during the game.
 */
public interface Strategy {
    /**
     * Determines whether to split the current hand.
     * 
     * @return true if the hand should be split, false otherwise
     */
    public boolean split();

    /**
     * Determines whether to double down on the current hand.
     * 
     * @return true if the hand should be doubled down, false otherwise
     */
    public boolean doubleDown();

    /**
     * Determines whether to hit (draw another card) on the current hand.
     * 
     * @return true if another card should be drawn, false otherwise
     */
    public boolean hit();
}
