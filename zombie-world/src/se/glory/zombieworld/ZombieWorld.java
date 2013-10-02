package se.glory.zombieworld;

import se.glory.zombieworld.screens.GameScreen;
import se.glory.zombieworld.screens.Splash;
import se.glory.zombieworld.utilities.Constants;

import com.badlogic.gdx.Game;

public class ZombieWorld extends Game {

	@Override
	public void create() {
		if (Constants.DEBUG_MODE) {
			setScreen(new GameScreen());
		} else {
			setScreen(new Splash());
		}
	}
	
}
