package se.glory.zombieworld.model.entities.progressbars;

import se.glory.zombieworld.utilities.Constants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class ProgressBar extends Actor{

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
	
	private ProgressFill[] progressBarAmount = null;
	
	private Image bgActor, fgActor;
	
	public ProgressBar(Stage stage, int x, int y, int xMargin, int yMargin, int fillLength, int maxPercent, Texture bg, Texture fill, Texture fg){

		this.x = x;
		this.y = y;
		this.xMargin = xMargin;
		this.yMargin = yMargin;
		this.fillLength = fillLength;
		maxHealthPercent = maxPercent;

		lastHealthPercent = maxHealthPercent;
		
		//Creating the background layer
		texture = bg;
		bgActor = new Image(texture);
		
		stage.addActor(bgActor);
		bgActor.setPosition(x, y);
		
		//Creating both the infected healthbar and the regular one
		progressBarAmount = new ProgressFill[maxHealthPercent];
//		infectedHealthBarAmount = new HealthFill[maxHealthPercent];
		
		//Creating all the HealthFills and places them in the array
		for(int i=0; i<maxHealthPercent; i++) {
			int newX = i*fillLength+x+xMargin;
			progressBarAmount[i] = new ProgressFill(stage, newX, y+yMargin, i, fill);
	//		infectedHealthBarAmount[i] = new HealthFill(stage, newX, y+yMargin, i, true);
		}
		
		//Creating a visual layer above the healthbar to work as container.
		texture = fg;
		fgActor = new Image(texture);
		
		stage.addActor(fgActor);
		fgActor.setPosition(x, y);
		
		//Resets healthbar
		resetHealthBar();
	}
	
	public void setVisibility(boolean visibility) {
		bgActor.setVisible(visibility);
		for(int i=0; i<maxHealthPercent; i++) {
			progressBarAmount[i].setVisibility(visibility);
		}
		fgActor.setVisible(visibility);
	}
	
	public void updatePosition() {
	//	x = ((Constants.VIEWPORT_WIDTH/2) - (310/2));
	//	y = (62);
		bgActor.setPosition(x, y);
		
		for(int i=0; i<maxHealthPercent; i++) {
			progressBarAmount[i].updatePosition();
//			infectedHealthBarAmount[i].updatePosition();
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
//		activeHealthBar = infectedHealthBarAmount;
		lastHealthPercent = maxHealthPercent;
//		setInfectedState(false);
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
		progressBarAmount[lastHealthPercent++].show();
	}
	
	private void decHealthBar() {
		progressBarAmount[--lastHealthPercent].hide();
	}
	
	//Forcing the entire health to update to the chosen percentage. Useful when changing between infected and normal healthBar
	public void forceHealthUpdate(int newHealth) {
		for(int i=0; i<newHealth; i++) {
			progressBarAmount[i].show();
		}
		
		for(int j=newHealth; j<maxHealthPercent; j++) {
			progressBarAmount[j].hide();
		}
		lastHealthPercent = newHealth;
		healthGoal = newHealth;
	}
	
	//Not in use right now. May be removed later
	public void updateHealth(int healthToUpdate) {
		if(lastHealthPercent < healthToUpdate) {
			for(int i=lastHealthPercent; i<healthToUpdate; i++) {
				progressBarAmount[i].show();
			}
		}else{
			for(int i=lastHealthPercent; i>healthToUpdate; i--) {
				progressBarAmount[i-1].hide();
			}
		}
		lastHealthPercent = healthToUpdate;
	}
	
	/**
	 * Changes the color of the healthbar to give visual feedback of infected state
	 * @param state changes the bar to the colors depending on if it is true or false. It shows if the bar should be in infected
	 * state or not
	 */
	/*public void setInfectedState(boolean state) {
		//Hiding last active healthBar
		forceHealthUpdate(0);
		
	//	activeHealthBar = state ? infectedHealthBarAmount : healthBarAmount;
		forceHealthUpdate(lastHealthPercent);
	}*/
	
	private class ProgressFill {
		private Texture texture;
		private Image actor;
		private float x, y;
		private int index;
		
		private ProgressFill(Stage stage, float x, float y, int index, Texture texture) {
			this.texture = texture;
			this.x = x;
			this.y = y;
			this.index = index;
			actor = new Image(texture);
			
			stage.addActor(actor);
			actor.setPosition(x, y);
		}
		
		private void setVisibility(boolean visibility) {
			actor.setVisible(visibility);
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
