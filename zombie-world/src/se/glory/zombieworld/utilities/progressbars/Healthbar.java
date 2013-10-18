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
	 * either regular healthbar or an infected version
	 */
	public Healthbar(Stage stage) {
		
		healthBar = new ProgressBar(stage, x, y, xMargin, yMargin, fillLength, 
			maxHealthPercent, 
			new Texture(Gdx.files.internal("img/health/healthBarBottom.png")),
			new Texture(Gdx.files.internal("img/health/healthBar.png")),
			new Texture(Gdx.files.internal("img/health/healthBarTop2.png")));
	
		infectedHealthBar = new ProgressBar(stage, x, y, xMargin, yMargin, fillLength, 
			maxHealthPercent, 
			new Texture(Gdx.files.internal("img/health/healthBarBottom.png")),
			new Texture(Gdx.files.internal("img/health/infectedHealthBar.png")),
			new Texture(Gdx.files.internal("img/health/healthBarTop2.png")));
	
		resetHealthBar();
	}

	public void resetHealthBar() {
		activeBar = infectedHealthBar;
		activeBar.forceHealthUpdate(maxHealthPercent);
		setInfectedState(false);
	}

	public int getHealthPercentGoal() {
		
		return activeBar.getHealthPercentGoal();
	}
	
	public void setHealthPercentGoal(int healthToUpdate) {
		activeBar.setHealthPercentGoal(healthToUpdate);
	}

	public void updateHealthMovementSlowly() {
		activeBar.updateHealthMovementSlowly();
	}
	
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
	
	public void updatePosition() {
		healthBar.updatePosition();
		infectedHealthBar.updatePosition();
	}
}
