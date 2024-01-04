package com.arthur.blackjack;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.arthur.blackjack.controller.Game;
import com.arthur.blackjack.strategies.StrategyFactory;

@SpringBootApplication
public class BlackjackApplication implements CommandLineRunner {

	private final Game game;
	private final StrategyFactory strategyFactory;

	public BlackjackApplication(Game game, StrategyFactory strategyFactory) {
		this.game = game;
		this.strategyFactory = strategyFactory;
	}

	public static void main(String[] args) {
		SpringApplication.run(BlackjackApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		game.setStrategy(strategyFactory.getStrategy("basic"));
		game.play();
	}

}
