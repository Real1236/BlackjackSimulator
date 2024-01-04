package com.arthur.blackjack;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.arthur.blackjack.controller.Game;

@SpringBootApplication
public class BlackjackApplication implements CommandLineRunner {

	private final Game game;

	public BlackjackApplication(Game game) {
		this.game = game;
	}

	public static void main(String[] args) {
		SpringApplication.run(BlackjackApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		game.play();
	}

}
