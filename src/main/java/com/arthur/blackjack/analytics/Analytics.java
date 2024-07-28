package com.arthur.blackjack.analytics;

public interface Analytics {
    void createNewResultsSheet(int bet);
    void recordNewRound(Integer round, float money);
    void recordInitialBet(Integer round, float bet);
    void recordRoundResult(Integer round, RoundResult result);
    void evaluateFormulas();
    void saveExcel();
}
