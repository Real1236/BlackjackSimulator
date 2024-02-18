package com.arthur.blackjack.strategies;

import com.arthur.blackjack.strategies.impl.BasicStrategy;
import com.arthur.blackjack.strategies.impl.CustomCountingStrategy;
import com.arthur.blackjack.strategies.impl.HiLoStrategy;

public interface StrategyFactory {
    BasicStrategy getBasicStrategy();
    CustomCountingStrategy getCustomCountingStrategy();
    HiLoStrategy getHiLoStrategy();
}
