package com.adventuretron.timtris;

import com.badlogic.gdx.Game;

public class TimtrisGame extends Game {
	@Override
	public void create() {
		this.setScreen(new GameScreen(this));
	}
}
