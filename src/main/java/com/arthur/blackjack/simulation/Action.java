package com.arthur.blackjack.simulation;

public enum Action {
    HIT("h"),
    STAND("s"),
    DOUBLE_DOWN("d"),
    SPLIT("p"),
    SURRENDER("r"),
    DOUBLE_STAND("ds");

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
