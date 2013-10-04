package se.glory.zombieworld.model.entities.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;


public class ItemContainer {
	
	private Texture texture;
	private Image actor;
	
	public ItemContainer(Stage stage, float x, float y) {
		texture = new Texture(Gdx.files.internal("img/itemBase.png"));
		
		actor = new Image(texture);
		
		stage.addActor(actor);
		actor.setPosition(x, y);
		hide();
	}
	
	public boolean isActorVisible() {
		return actor.isVisible();
	}
	
	public float getActorX() {
		return actor.getX();
	}
	
	public float getActorY() {
		return actor.getY();
	}
	
	public void setActorX(float x) {
		actor.setX(x);
	}
	
	public void setActorY(float y) {
		actor.setY(y);
	}
	
	public void show() {
		actor.setVisible(true);
	}
	
	public void hide() {
		actor.setVisible(false);
	}
}
