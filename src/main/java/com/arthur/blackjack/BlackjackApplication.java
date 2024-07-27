package com.arthur.blackjack;

import com.arthur.blackjack.analytics.AnalyticsType;
import com.arthur.blackjack.config.GameRules;
import com.arthur.blackjack.config.GameSettings;
import com.arthur.blackjack.config.Rule;
import com.arthur.blackjack.controller.GameFactory;
import com.arthur.blackjack.strategies.StrategyType;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.arthur.blackjack.controller.Game;

@SpringBootApplication
public class BlackjackApplication implements CommandLineRunner {

	private final GameFactory gameFactory;
	private final GameSettings gameSettings;
	private final GameRules gameRules;

	public BlackjackApplication(GameFactory gameFactory, GameSettings gameSettings, GameRules gameRules) {
		this.gameFactory = gameFactory;
		this.gameSettings = gameSettings;
		this.gameRules = gameRules;
	}

	public static void main(String[] args) {
		SpringApplication.run(BlackjackApplication.class, args);
	}

	@Override
	public void run(String... args) {
		// Configure game factory settings
		this.gameFactory.setStrategyType(StrategyType.HILO);
		this.gameFactory.setAnalyticsType(AnalyticsType.CSV);

		// Configure game settings
		this.gameSettings.setBetSize(5);
		this.gameSettings.setMinChipSize(1);
		this.gameSettings.setBetSpread(12);
		this.gameSettings.setBankroll(1000 * this.gameSettings.getBetSize()); // 10000 betting units = <1% risk of ruin
		this.gameSettings.setMaxRounds(1000000);

		// Configure game rules
		this.gameRules.setRule(Rule.NORMAL);

		// Configure number of games to run
		int numGames = 1;
		for (int i = 1; i <= numGames; i++) {
			Game game = gameFactory.createGame(i);
			game.start();
		}
	}

}
