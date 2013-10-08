package se.glory.zombieworld.model.entities.items;

import se.glory.zombieworld.utilities.Constants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Healthbar extends Actor{
	
	private Texture texture;
	private Image actor;
	
	private int lastHealthPercent, healthGoal;
	private int maxHealthPercent = 100;
	
	//Positioning for the healthbar
	private int x = 25;
	private int y = 545;
	
	private int xMargin = 5;
	private int yMargin = 4;
	private int fillLength = 3;
	
	private HealthFill[] healthBarAmount;
	private Image[] healthBarActors;
	
	private Image bgActor, fgActor;
	
	
	public Healthbar(Stage stage){
		x = (Constants.VIEWPORT_WIDTH - 15 - 780);
		y = (Constants.VIEWPORT_HEIGHT - 32 - 15);
		lastHealthPercent = maxHealthPercent;
		healthBarAmount = new HealthFill[maxHealthPercent];
		
		texture = new Texture(Gdx.files.internal("img/health/healthBarBottom.png"));
		bgActor = new Image(texture);
		
		stage.addActor(bgActor);
		bgActor.setPosition(x, y);
		
		//Creating all the HealthFills and places them in the array
		for(int i=0; i<maxHealthPercent; i++) {
			int newX = i*fillLength+x+xMargin;
			healthBarAmount[i] = new HealthFill(stage, newX, y+yMargin, i);
		}
		
		texture = new Texture(Gdx.files.internal("img/health/healthBarTop.png"));
		fgActor = new Image(texture);
		
		stage.addActor(fgActor);
		fgActor.setPosition(x, y);
		
		resetHealthBar();
		
	}
	
	public void updatePosition() {
		y = (Constants.VIEWPORT_HEIGHT - 15 - 32);
		x = (Constants.VIEWPORT_WIDTH - 15 - 780);
		bgActor.setPosition(x, y);
		for(int i=0; i<maxHealthPercent; i++) {
			healthBarAmount[i].updatePosition();
		}
		fgActor.setPosition(x, y);
		
		
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
	
	public int getHealthPercentGoal() {
		return healthGoal;
	}
	
	public int getMaxHealthPercent() {
		return maxHealthPercent;
	}
	
	public void resetHealthBar() {
		lastHealthPercent = maxHealthPercent;
		updateHealth(lastHealthPercent);
	}
	
	public void setHealthPercentGoal(int healthToUpdate) {
		healthGoal = healthToUpdate;
	}
	
	public void updateHealthMovementSlowly() {
		if(healthGoal != lastHealthPercent) {
			if(healthGoal > lastHealthPercent) {
				incHealthBar();
			}else{
				decHealthBar();
			}
		}
	}
	
	private void incHealthBar() {
		healthBarAmount[lastHealthPercent++].show();
	}
	
	private void decHealthBar() {
		healthBarAmount[--lastHealthPercent].hide();
	}
	
	//Not in use right now. May be removed later
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
		private float x, y;
		private int index;
		
		public HealthFill(Stage stage, float x, float y, int index) {
			texture = new Texture(Gdx.files.internal("img/health/healthBar.png"));
			this.x = x;
			this.y = y;
			this.index = index;
			actor = new Image(texture);
			
			stage.addActor(actor);
			actor.setPosition(x, y);
		}
		
		private void updatePosition() {
			y = (Constants.VIEWPORT_HEIGHT - 15 - 32 + yMargin);
			x = (Constants.VIEWPORT_WIDTH - 15 - 780 + index*fillLength+xMargin);
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
