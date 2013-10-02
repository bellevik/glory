package se.glory.zombieworld.model.entities.items;

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
	
	private HealthFill[] healthBarAmount = new HealthFill[100];
	
	
	public Healthbar(Stage stage, int currentHealth, int maxHealth){
		this.currentHealth = currentHealth;
		this.maxHealth = maxHealth;
		int x = 25;
		int y = 525;
		
	//	texture = new Texture(Gdx.files.internal("img/itemBase.png"));
		texture = new Texture(Gdx.files.internal("img/healthBase.png"));
		
		actor = new Image(texture);
		for(int i=0; i<100; i++) {
			healthBarAmount[i] = new HealthFill(stage, x+2+i*3, y);
		}
		
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
