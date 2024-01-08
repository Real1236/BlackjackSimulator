package com.arthur.blackjack.strategies;

public enum Action {
    HIT("H"),
    STAND("S"),
    DOUBLE_DOWN("D"),
    SPLIT("Y"),
    NO_SPLIT("N"),
    SURRENDER("R"),
    DOUBLE_STAND("Ds");

    private final String abbreviation;

    Action(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public static Action fromAbbreviation(String abbreviation) {
        for (Action action : Action.values())
            if (action.getAbbreviation().equalsIgnoreCase(abbreviation))
                return action;
        throw new IllegalArgumentException("Invalid action abbreviation: " + abbreviation);
    }
}