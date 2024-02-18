package com.arthur.blackjack.analytics.impl;

import com.arthur.blackjack.analytics.Analytics;
import com.arthur.blackjack.analytics.RoundResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.EnumMap;
import java.util.Map;

public class CsvAnalyticsImpl implements Analytics {
    private static final Logger logger = LogManager.getLogger(CsvAnalyticsImpl.class);

    private static final String OUTPUT_FILE_PATH = "src/main/resources/analytics/Results";

    private PrintWriter writer;
    private Map<RoundResult, Integer> roundResults;

    public CsvAnalyticsImpl(int gameNum) {
        try {
            writer = new PrintWriter(new FileWriter(OUTPUT_FILE_PATH + gameNum + ".csv"));
            roundResults = new EnumMap<>(RoundResult.class);
        } catch (IOException e) {
            logger.error("Failed to create PrintWriter", e);
        }
    }

    @Override
    public void createNewResultsSheet(int bet) {
        writer.println("Round,Money,Initial Bet,BLACKJACK,WIN,DOUBLE_WIN,PUSH,DOUBLE_BUST,BUST,DOUBLE_LOSE,LOSE");
    }

    @Override
    public void recordNewRound(Integer round, Integer money) {
        // Write the results of the previous round to the CSV file
        if (!roundResults.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (RoundResult possibleResult : RoundResult.values()) {
                sb.append(roundResults.getOrDefault(possibleResult, 0)).append(",");
            }
            sb.deleteCharAt(sb.length() - 1); // Remove trailing comma
            writer.println(sb);
            roundResults.clear();
        }

        // Start the new round
        writer.print(round + "," + money + ",");
    }

    @Override
    public void recordInitialBet(Integer round, Integer bet) {
        writer.print(bet + ",");
    }

    @Override
    public void recordRoundResult(Integer round, RoundResult result) {
        roundResults.put(result, roundResults.getOrDefault(result, 0) + 1);
    }

    @Override
    public void evaluateFormulas() {
    }

    @Override
    public void saveExcel() {
        // Write the results of the last round to the CSV file
        if (!roundResults.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (RoundResult possibleResult : RoundResult.values()) {
                sb.append(roundResults.getOrDefault(possibleResult, 0)).append(",");
            }
            sb.deleteCharAt(sb.length() - 1); // Remove trailing comma
            writer.println(sb);
        }

        writer.close();
    }

}
