package se.glory.zombieworld.utilities;

import com.badlogic.gdx.Gdx;

public class ScreenCoordinates {
	public static int getRealX(int x) {
		return (int)(x * ((double)Constants.VIEWPORT_WIDTH / Gdx.graphics.getWidth()));	
	}
	public static int getRealY(int y) {
		return (int)((Gdx.graphics.getHeight() - y) * ((double)Constants.VIEWPORT_HEIGHT / Gdx.graphics.getHeight()));
	}
}
