package com.arthur.blackjack.unit;

import com.arthur.blackjack.config.LoggerConfig;
import com.arthur.blackjack.simulation.Action;
import com.arthur.blackjack.simulation.StrategyTableReader;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Map;
import java.util.logging.Logger;

import static org.junit.Assert.*;

public class StrategyTableReaderTest {
    private static final Logger LOGGER = Logger.getLogger(StrategyTableReaderTest.class.getName());

    @BeforeClass
    public static void setUpLogger() {
        LoggerConfig.configure(LOGGER);
    }

    @Test
    public void testReadStrategyTable() {
        LOGGER.info("Testing ability to read basic strategy Excel file");

        Map<String, Map<Integer, Map<Integer, Action>>> strategyTable;

        strategyTable = StrategyTableReader.readStrategyTable();

        assertNotNull(strategyTable);
        assertFalse(strategyTable.isEmpty());

        LOGGER.info("Verifying 'Hard', 'Soft', and 'Split' sheets exist");
        assertTrue(strategyTable.containsKey("Hard"));
        assertTrue(strategyTable.containsKey("Soft"));
        assertTrue(strategyTable.containsKey("Split"));

        Map<Integer, Map<Integer, Action>> hardTable = strategyTable.get("Hard");
        Map<Integer, Map<Integer, Action>> softTable = strategyTable.get("Soft");
        Map<Integer, Map<Integer, Action>> splitTable = strategyTable.get("Split");

        assertNotNull(hardTable);
        assertNotNull(softTable);
        assertNotNull(splitTable);

        assertFalse(hardTable.isEmpty());
        assertFalse(softTable.isEmpty());
        assertFalse(splitTable.isEmpty());

        LOGGER.info("Verifying specific entries in the strategy tables");
        Map<Integer, Action> hardPlayerTotal17 = hardTable.get(17);
        assertNotNull(hardPlayerTotal17);
        assertEquals(Action.STAND, hardPlayerTotal17.get(2));
        assertEquals(Action.STAND, hardPlayerTotal17.get(3));
        assertEquals(Action.STAND, hardPlayerTotal17.get(4));
        assertEquals(Action.STAND, hardPlayerTotal17.get(5));
        assertEquals(Action.STAND, hardPlayerTotal17.get(6));
        assertEquals(Action.STAND, hardPlayerTotal17.get(7));
        assertEquals(Action.STAND, hardPlayerTotal17.get(8));
        assertEquals(Action.STAND, hardPlayerTotal17.get(9));
        assertEquals(Action.STAND, hardPlayerTotal17.get(10));
        assertEquals(Action.STAND, hardPlayerTotal17.get(11));

        Map<Integer, Action> softPlayerTotal12 = softTable.get(12);
        assertNotNull(softPlayerTotal12);
        assertEquals(Action.HIT, softPlayerTotal12.get(2));
        assertEquals(Action.HIT, softPlayerTotal12.get(3));
        assertEquals(Action.HIT, softPlayerTotal12.get(4));
        assertEquals(Action.HIT, softPlayerTotal12.get(5));
        assertEquals(Action.HIT, softPlayerTotal12.get(6));
        assertEquals(Action.HIT, softPlayerTotal12.get(7));
        assertEquals(Action.HIT, softPlayerTotal12.get(8));
        assertEquals(Action.HIT, softPlayerTotal12.get(9));
        assertEquals(Action.HIT, softPlayerTotal12.get(10));
        assertEquals(Action.HIT, softPlayerTotal12.get(11));

        Map<Integer, Action> splitPair2 = splitTable.get(2);
        assertNotNull(splitPair2);
        assertEquals(Action.SPLIT, splitPair2.get(2));
        assertEquals(Action.SPLIT, splitPair2.get(3));
        assertEquals(Action.SPLIT, splitPair2.get(4));
        assertEquals(Action.SPLIT, splitPair2.get(5));
        assertEquals(Action.SPLIT, splitPair2.get(6));
        assertEquals(Action.SPLIT, splitPair2.get(7));
        assertNull(splitPair2.get(8));
        assertNull(splitPair2.get(9));
        assertNull(splitPair2.get(10));
        assertNull(splitPair2.get(11));
    }
}

