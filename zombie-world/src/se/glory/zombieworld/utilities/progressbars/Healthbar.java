package se.glory.zombieworld.utilities.progressbars;

import se.glory.zombieworld.model.StageModel;
import se.glory.zombieworld.utilities.Animator;
import se.glory.zombieworld.utilities.Constants;
import se.glory.zombieworld.utilities.Constants.MoveableBodyType;
import se.glory.zombieworld.view.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Healthbar {
	private int maxHealthPercent = 100;
	
	//Positioning for the healthbar
	private int x = ((Constants.VIEWPORT_WIDTH/2) - (310/2));
	private int y = (62);
	
	private int xMargin = 5;
	private int yMargin = 4;
	private int fillLength = 3;
	
	private ProgressBar healthBar, infectedHealthBar, activeBar;
	
	/*
	 * Special healthbar class to support the fact that the healthbar uses 2 bars to show
	 * either regular healthbar or an infected version. Therefor it does not extend the progressbar
	 * This is to keep the objects of the healthbars in the world instead
	 * of creating a new one evertime we have to switch between regular and infected
	 */
	public Healthbar(Stage stage) {
		
		healthBar = new ProgressBar(stage, x, y, xMargin, yMargin, fillLength, 
			maxHealthPercent, 
			new Texture(Gdx.files.internal("img/health/healthBarBottom2.png")),
			new Texture(Gdx.files.internal("img/health/healthBar.png")),
			new Texture(Gdx.files.internal("img/health/healthBarTop.png")));
	
		infectedHealthBar = new ProgressBar(stage, x, y, xMargin, yMargin, fillLength, 
			maxHealthPercent, 
			new Texture(Gdx.files.internal("img/health/healthBarBottom2.png")),
			new Texture(Gdx.files.internal("img/health/infectedHealthBar2.png")),
			new Texture(Gdx.files.internal("img/health/healthBarTop.png")));
	
		resetHealthBar();
	}

	public void resetHealthBar() {
		activeBar = infectedHealthBar;
		activeBar.forceHealthUpdate(maxHealthPercent);
		setInfectedState(false);
	}

	//Returns the goal from the current used Healthbar
	public int getHealthPercentGoal() {	
		return activeBar.getHealthPercentGoal();
	}
	
	//Uses the same method for the activeBar
	public void setHealthPercentGoal(int healthToUpdate) {
		activeBar.setHealthPercentGoal(healthToUpdate);
	}

	//Uses the same method for the activeBar
	public void updateHealthMovementSlowly() {
		activeBar.updateHealthMovementSlowly();
	}
	
	//Changes the bar and updates the states and goals in the new bar by using the
	//data from the old one
	public void setInfectedState(boolean state) {
		//Hiding last active healthBar
		int currentHealth = activeBar.getLastKnownHealthPercent();
		int currentHealthGoal = activeBar.getHealthPercentGoal();
		activeBar.setVisibility(false);
		
		activeBar = state ? infectedHealthBar : healthBar;
		activeBar.setVisibility(true);
		activeBar.forceHealthUpdate(currentHealth);
		activeBar.setHealthPercentGoal(currentHealthGoal);
		
	}
	
	//Used to update the positioning for both healthbars
	public void updatePosition() {
		healthBar.updatePosition();
		infectedHealthBar.updatePosition();
	}
}
