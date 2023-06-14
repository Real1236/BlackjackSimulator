package com.arthur.blackjack.simulation;

public enum RoundResult {
    BLACKJACK(2),
    WIN(3),
    DOUBLE_WIN(4),
    PUSH(5),
    DOUBLE_BUST(6),
    BUST(7),
    DOUBLE_LOSE(8),
    LOSE(9);

    private final int colIndex;

    RoundResult(int colIndex) {
        this.colIndex = colIndex;
    }

    public int getColIndex() {
        return colIndex;
    }
}
