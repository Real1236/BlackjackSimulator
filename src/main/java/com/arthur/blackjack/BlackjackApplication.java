package com.arthur.blackjack;

import com.arthur.blackjack.controller.GameFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.arthur.blackjack.controller.Game;

@SpringBootApplication
public class BlackjackApplication implements CommandLineRunner {

	private final GameFactory gameFactory;

	public BlackjackApplication(GameFactory gameFactory) {
		this.gameFactory = gameFactory;
	}

	public static void main(String[] args) {
		SpringApplication.run(BlackjackApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		for (int i = 1; i <= 5; i++) {
			Game game = gameFactory.createGame(i);
			game.start();
		}
	}

}
