package com.arthur.blackjack.analytics;

import com.arthur.blackjack.analytics.impl.AnalyticsImpl;
import com.arthur.blackjack.analytics.impl.CsvAnalyticsImpl;

public interface AnalyticsFactory {
    AnalyticsImpl createAnalytics(int gameNum);
    CsvAnalyticsImpl createCsvAnalytics(int gameNum);
}
