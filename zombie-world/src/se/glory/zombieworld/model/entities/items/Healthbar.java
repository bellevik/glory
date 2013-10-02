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
	
	//Positioning for the healthbar
	private int x = 25;
	private int y = 525;
	
	private HealthFill[] healthBarAmount;
	
	
	public Healthbar(Stage stage, int currentHealth, int maxHealth){
		this.currentHealth = currentHealth;
		this.maxHealth = maxHealth;
		
		healthBarAmount = new HealthFill[maxHealth];
		
		texture = new Texture(Gdx.files.internal("img/healthBase.png"));
		
		actor = new Image(texture);
		
		stage.addActor(actor);
		actor.setPosition(x, y);
		
		//Creating all the HealthFills and places them in the array
		for(int i=0; i<maxHealth; i++) {
			int newX = i*3+x+2;
			healthBarAmount[i] = new HealthFill(stage, newX, y);
		}
		updateHealth(currentHealth);
		
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
		for(int i=0; i<currentHealth; i++) {
			healthBarAmount[i].show();
		}
		for(int j=currentHealth; j<maxHealth; j++) {
			healthBarAmount[j].hide();
		}
	}

}
