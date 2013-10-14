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
	private int x;
	private int y;
	
	private int xMargin = 5;
	private int yMargin = 4;
	private int fillLength = 3;
	
	private HealthFill[] healthBarAmount = null;
	private HealthFill[] infectedHealthBarAmount = null;
	private HealthFill[] activeHealthBar;
	
	private Image bgActor, fgActor;
	
	
	public Healthbar(Stage stage){
		x = ((Constants.VIEWPORT_WIDTH/2) - (310/2));
		y = (62);

		lastHealthPercent = maxHealthPercent;
		
		//Creating the background layer
		texture = new Texture(Gdx.files.internal("img/health/healthBarBottom.png"));
		bgActor = new Image(texture);
		
		stage.addActor(bgActor);
		bgActor.setPosition(x, y);
		
		//Creating both the infected healthbar and the regular one
		healthBarAmount = new HealthFill[maxHealthPercent];
		infectedHealthBarAmount = new HealthFill[maxHealthPercent];
		
		//Creating all the HealthFills and places them in the array
		for(int i=0; i<maxHealthPercent; i++) {
			int newX = i*fillLength+x+xMargin;
			healthBarAmount[i] = new HealthFill(stage, newX, y+yMargin, i, false);
			infectedHealthBarAmount[i] = new HealthFill(stage, newX, y+yMargin, i, true);
		}
		
		//Creating a visual layer above the healthbar to work as container.
		texture = new Texture(Gdx.files.internal("img/health/healthBarTop.png"));
		fgActor = new Image(texture);
		
		stage.addActor(fgActor);
		fgActor.setPosition(x, y);
		
		//Resets healthbar
		resetHealthBar();
	}
	
	public void updatePosition() {
		x = ((Constants.VIEWPORT_WIDTH/2) - (310/2));
		y = (62);
		bgActor.setPosition(x, y);
		
		for(int i=0; i<maxHealthPercent; i++) {
			healthBarAmount[i].updatePosition();
			infectedHealthBarAmount[i].updatePosition();
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
		activeHealthBar = infectedHealthBarAmount;
		lastHealthPercent = maxHealthPercent;
		setInfectedState(false);
	}
	
	public void setHealthPercentGoal(int healthToUpdate) {
		healthGoal = healthToUpdate;
	}
	
	//Uses inc and dec to slowly change the healthbar towards the set goal making it look smooth when changing
	public void updateHealthMovementSlowly() {
		if(healthGoal != lastHealthPercent) {
			if(healthGoal > lastHealthPercent) {
				incHealthBar();
			}else{
				decHealthBar();
			}
		}
	}
	
	//Inc and dec are used to slowly change the healthbar
	private void incHealthBar() {
		activeHealthBar[lastHealthPercent++].show();
	}
	
	private void decHealthBar() {
		activeHealthBar[--lastHealthPercent].hide();
	}
	
	//Forcing the entire health to update to the chosen percentage. Useful when changing between infected and normal healthBar
	private void forceHealthUpdate(int newHealth) {
		for(int i=0; i<newHealth; i++) {
			activeHealthBar[i].show();
		}
		
		for(int j=newHealth; j<maxHealthPercent; j++) {
			activeHealthBar[j].hide();
		}
	}
	
	//Not in use right now. May be removed later
	public void updateHealth(int healthToUpdate) {
		if(lastHealthPercent < healthToUpdate) {
			for(int i=lastHealthPercent; i<healthToUpdate; i++) {
				activeHealthBar[i].show();
			}
		}else{
			for(int i=lastHealthPercent; i>healthToUpdate; i--) {
				activeHealthBar[i-1].hide();
			}
		}
		lastHealthPercent = healthToUpdate;
	}
	
	/**
	 * Changes the color of the healthbar to give visual feedback of infected state
	 * @param state changes the bar to the colors depending on if it is true or false. It shows if the bar should be in infected
	 * state or not
	 */
	public void setInfectedState(boolean state) {
		//Hiding last active healthBar
		forceHealthUpdate(0);
		
		activeHealthBar = state ? infectedHealthBarAmount : healthBarAmount;
		forceHealthUpdate(lastHealthPercent);
	}
	
	private class HealthFill {
		private Texture texture;
		private Image actor;
		private float x, y;
		private int index;
		
		public HealthFill(Stage stage, float x, float y, int index, boolean infected) {
			texture = infected ? new Texture(Gdx.files.internal("img/health/infectedHealthBar.png")) : new Texture(Gdx.files.internal("img/health/healthBar.png"));
			this.x = x;
			this.y = y;
			this.index = index;
			actor = new Image(texture);
			
			stage.addActor(actor);
			actor.setPosition(x, y);
		}
		
		private void updatePosition() {
			x = ((Constants.VIEWPORT_WIDTH/2) - (310/2) + index*fillLength+xMargin);
			y = (62 + yMargin);
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
