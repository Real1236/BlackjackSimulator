package com.arthur.blackjack.strategies;

import org.springframework.stereotype.Component;

@Component
public class BasicStrategy implements Strategy {

    @Override
    public boolean split() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean doubleDown() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'doubleDown'");
    }

    @Override
    public boolean hit() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'hit'");
    }
    
}
