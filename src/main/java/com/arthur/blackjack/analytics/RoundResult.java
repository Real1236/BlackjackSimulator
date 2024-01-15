package com.arthur.blackjack.analytics;

public enum RoundResult {
    BLACKJACK(3),
    WIN(BLACKJACK.getColIndex() + 1),
    DOUBLE_WIN(WIN.getColIndex() + 1),
    PUSH(DOUBLE_WIN.getColIndex() + 1),
    DOUBLE_BUST(PUSH.getColIndex() + 1),
    BUST(DOUBLE_BUST.getColIndex() + 1),
    DOUBLE_LOSE(BUST.getColIndex() + 1),
    LOSE(DOUBLE_LOSE.getColIndex() + 1);

    private final int colIndex;

    RoundResult(int colIndex) {
        this.colIndex = colIndex;
    }

    public int getColIndex() {
        return colIndex;
    }
}
