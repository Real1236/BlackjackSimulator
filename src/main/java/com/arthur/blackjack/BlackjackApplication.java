package com.arthur.blackjack;

import com.arthur.blackjack.config.GameSettings;
import com.arthur.blackjack.controller.GameFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.arthur.blackjack.controller.Game;

@SpringBootApplication
public class BlackjackApplication implements CommandLineRunner {

	private final GameFactory gameFactory;
	private final GameSettings gameSettings;

	public BlackjackApplication(GameFactory gameFactory, GameSettings gameSettings) {
		this.gameFactory = gameFactory;
		this.gameSettings = gameSettings;
	}

	public static void main(String[] args) {
		SpringApplication.run(BlackjackApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// Configure game factory settings
		this.gameFactory.setStrategyType("customCounting");
		this.gameFactory.setAnalyticsType("csv");

		// Configure game settings
		this.gameSettings.setBetSize(10);
		this.gameSettings.setBankroll(10000 * this.gameSettings.getBetSize()); // 10000 betting units = <1% risk of ruin
		this.gameSettings.setMaxRounds(5); // (int) Math.pow(10, 5) * 2);

		// Configure number of games to run
		int numGames = 1;
		for (int i = 1; i <= numGames; i++) {
			Game game = gameFactory.createGame(i);
			game.start();
		}
	}

}
