package se.glory.zombieworld.ui;

import se.glory.zombieworld.utilities.Constants;
import se.glory.zombieworld.utilities.misc.ScreenCoordinates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/*
 * A button used to pause all processes in the game.
 */
public class PauseButton {
	private Texture texture;
	private Image image;
	private int delay = 0;
	private int delayMax = 15;
	
	public PauseButton(Stage stage, float x, float y) {
		texture = new Texture(Gdx.files.internal("img/pauseButton.png"));
		
		image = new Image(texture);
		stage.addActor(image);
		image.setPosition(x, y);
	}
	
	/*
	 * Repositions elements to fit different screen-sizes. 
	 */
	public void updatePosition() {
		image.setPosition(15, Constants.VIEWPORT_HEIGHT - 32 - 15);
	}
	
	public boolean isTouched() {
		if(delay < delayMax) {
			delay += 1;
		}
		int currentX = ScreenCoordinates.getRealX(Gdx.input.getX());
		int currentY = ScreenCoordinates.getRealY(Gdx.input.getY());
		boolean isTouched = Gdx.input.justTouched();
		
		/* Checks if you touch the screen and is on the pausebutton */
		if(currentX > image.getX() && currentX < image.getX() + 32 && currentY > image.getY() && 
				currentY < image.getY() + 32 && delay >= delayMax && isTouched) {
			delay = 0;
			return true;
		} else {
			return false;
		}
	}
}