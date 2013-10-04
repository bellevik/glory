package se.glory.zombieworld.model.entities.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Healthbar extends Actor{
	
	private Texture texture;
	private Image actor;
	
	private int lastHealthPercent;
	private int maxHealthPercent = 100;
	
	//Positioning for the healthbar
	private int x = 25;
	private int y = 545;
	
	private int xMargin = 5;
	private int yMargin = 4;
	private int fillLength = 3;
	
	private HealthFill[] healthBarAmount;
	
	
	public Healthbar(Stage stage){
		lastHealthPercent = maxHealthPercent;
		healthBarAmount = new HealthFill[maxHealthPercent];
		
		texture = new Texture(Gdx.files.internal("img/health/healthBarBottom.png"));
		actor = new Image(texture);
		
		stage.addActor(actor);
		actor.setPosition(x, y);
		
		//Creating all the HealthFills and places them in the array
		for(int i=0; i<maxHealthPercent; i++) {
			int newX = i*fillLength+x+xMargin;
			healthBarAmount[i] = new HealthFill(stage, newX, y+yMargin);
		}
		
		texture = new Texture(Gdx.files.internal("img/health/healthBarTop.png"));
		actor = new Image(texture);
		
		stage.addActor(actor);
		actor.setPosition(x, y);
		
		resetHealthBar();
		
	}
	
	public float getActorX() {
		return actor.getX();
	}
	
	public float getActorY() {
		return actor.getY();
	}
	
	public int getLastKnownHealthPercent() {
		return lastHealthPercent;
	}
	
	public int getMaxHealthPercent() {
		return maxHealthPercent;
	}
	
	public void resetHealthBar() {
		lastHealthPercent = maxHealthPercent;
		updateHealth(lastHealthPercent);
	}
	
	public void updateHealth(int healthToUpdate) {
		if(lastHealthPercent < healthToUpdate) {
			for(int i=lastHealthPercent; i<healthToUpdate; i++) {
				healthBarAmount[i].show();
			}
		}else{
			for(int i=lastHealthPercent; i>healthToUpdate; i--) {
				healthBarAmount[i-1].hide();
			}
		}
		lastHealthPercent = healthToUpdate;
	}
	
	private class HealthFill {
		private Texture texture;
		private Image actor;
		
		public HealthFill(Stage stage, float x, float y) {
			texture = new Texture(Gdx.files.internal("img/health/healthBar.png"));
			
			actor = new Image(texture);
			
			stage.addActor(actor);
			actor.setPosition(x, y);
		}
		
		private void show() {
			actor.setVisible(true);
		}
		
		private void hide() {
			actor.setVisible(false);
		}
	}
}
