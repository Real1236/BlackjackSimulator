package com.arthur.blackjack.analytics.impl;

import com.arthur.blackjack.analytics.AnalyticsFactory;
import org.springframework.stereotype.Component;

@Component
public class AnalyticsFactoryImpl implements AnalyticsFactory {
    @Override
    public AnalyticsImpl createAnalytics(int gameNum) {
        return new AnalyticsImpl(gameNum);
    }

    @Override
    public CsvAnalyticsImpl createCsvAnalytics(int gameNum) {
        return new CsvAnalyticsImpl(gameNum);
    }
}
