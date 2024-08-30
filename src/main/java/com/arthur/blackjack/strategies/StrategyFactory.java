package com.arthur.blackjack.strategies;

import com.arthur.blackjack.strategies.impl.*;

public interface StrategyFactory {
    BasicStrategy getBasicStrategy();
    HiLoStrategy getHiLoStrategy();
    CustomCountingStrategy getCustomCountingStrategy();
    HiLoStrategy2TC getHiLoStrategy2TC();
    CustomCountingStrategy2TC getCustomCountingStrategy2TC();
}
