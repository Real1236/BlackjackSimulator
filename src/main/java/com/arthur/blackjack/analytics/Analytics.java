package com.arthur.blackjack.analytics;

public interface Analytics {
    void createNewResultsSheet(int gameNum, int bet);
    void writeResults(Integer round, Integer money);
    void recordRoundResult(Integer round, RoundResult result);
    void evaluateFormulas();
    void saveExcel();
}
