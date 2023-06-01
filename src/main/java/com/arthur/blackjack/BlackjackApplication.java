package com.arthur.blackjack;

import com.arthur.blackjack.core.Game;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BlackjackApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlackjackApplication.class, args);
        Game game = new Game();
        game.initializeGame();
    }

}
