package se.glory.entities.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Healthbar extends Actor{
	
	private Texture texture;
	private Image actor;
	
	private int currentHealth;
	private int maxHealth;
	
	
	public Healthbar(Stage stage, float x, float y, int currentHealth, int maxHealth){
		this.currentHealth = currentHealth;
		this.maxHealth = maxHealth;
		
	//	texture = new Texture(Gdx.files.internal("img/itemBase.png"));
		texture = new Texture(Gdx.files.internal("img/healthBase.png"));
		
		actor = new Image(texture);
		
		stage.addActor(actor);
		actor.setPosition(x, y);
		
	}
	
	public float getActorX() {
		return actor.getX();
	}
	
	public float getActorY() {
		return actor.getY();
	}
	
	public int getHealth() {
		return currentHealth;
	}
	
	public int getMaxHealth() {
		return maxHealth;
	}
	
	public void resetHealthBar() {
		currentHealth = maxHealth;
	}
	
	public void updateHealth(int healthToUpdate) {
		currentHealth = healthToUpdate;
	}

}
