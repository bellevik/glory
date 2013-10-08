package se.glory.zombieworld.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class PauseButton {
	private Texture texture;
	private Image image;
	
	public PauseButton(Stage stage, float x, float y) {
		texture = new Texture(Gdx.files.internal("img/pauseButton.png"));
		image = new Image(texture);
		stage.addActor(image);
		image.setPosition(x, y);
		
	}
	
	public boolean isTouched() {
		int currentX = ScreenCoordinates.getRealX(Gdx.input.getX());
		int currentY = ScreenCoordinates.getRealY(Gdx.input.getY());
		
		if(currentX > image.getX() && currentX < image.getX() + 32 && currentY > image.getY() && currentY < image.getY() + 32) {
			System.out.println("Du trycker");
		}
		
		return false;
	}
}
