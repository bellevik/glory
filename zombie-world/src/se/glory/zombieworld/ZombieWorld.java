package se.glory.zombieworld;

import se.glory.utilities.Constants;
import se.glory.zombieworld.screens.GameScreen;
import se.glory.zombieworld.screens.Splash;

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
