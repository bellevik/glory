package se.glory.zombieworld.model;

import se.glory.zombieworld.ui.ItemView;
import se.glory.zombieworld.ui.Joystick;
import se.glory.zombieworld.ui.PauseButton;
import se.glory.zombieworld.ui.QuickSelection;
import se.glory.zombieworld.ui.ShopView;
import se.glory.zombieworld.utilities.Constants;
import se.glory.zombieworld.utilities.progressbars.Healthbar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class StageModel {
	public static Stage stage;
	public static Healthbar healthBar;
	public static QuickSelection quickSelection;
	public static ItemView itemView;
	public static ShopView shopView;
	public static Joystick moveStick, fireStick;
	public static PauseButton pauseButton;
	
	public static void createUI (SpriteBatch batch) {
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true, batch);
		
		moveStick = new Joystick(stage, 15, 15, 128, 128, Constants.TouchpadType.MOVEMENT);
		fireStick = new Joystick(stage, Constants.VIEWPORT_WIDTH - 15 - 128, 15, 128, 128, Constants.TouchpadType.FIRE);
		
		itemView = new ItemView(stage);
		quickSelection = new QuickSelection(stage);

		healthBar = new Healthbar(stage);
		
		shopView = new ShopView(stage);
		
		pauseButton = new PauseButton(stage, 15, Constants.VIEWPORT_HEIGHT - 32 - 15);
		
		Gdx.input.setInputProcessor(stage);
	}
	
}
