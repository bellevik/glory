package se.glory.zombieworld.model.entities.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class HealthFill {
	private Texture texture;
	private Image actor;
	
	public HealthFill(Stage stage, float x, float y) {
		texture = new Texture(Gdx.files.internal("img/healthFill.png"));
		
		actor = new Image(texture);
		
		stage.addActor(actor);
		actor.setPosition(x, y);
	}
	
	public boolean isActorVisible() {
		return actor.isVisible();
	}
	
	public void show() {
		actor.setVisible(true);
	}
	
	public void hide() {
		actor.setVisible(false);
	}
	
	public void setActorPosition(float x, float y) {
		actor.setPosition(x, y);
	}
}