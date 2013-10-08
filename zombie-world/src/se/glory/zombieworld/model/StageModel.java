package se.glory.zombieworld.model;

import se.glory.zombieworld.model.entities.items.Healthbar;
import se.glory.zombieworld.model.entities.items.ItemView;
import se.glory.zombieworld.model.entities.items.QuickSelection;
import se.glory.zombieworld.utilities.Constants;
import se.glory.zombieworld.utilities.Joystick;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class StageModel {
	public static Stage stage;
	public static Healthbar healthBar;
	public static QuickSelection quickSelection;
	public static ItemView itemView;
	public static Joystick moveStick, fireStick;
	
	public static void createUI (SpriteBatch batch) {
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true, batch);
		
		moveStick = new Joystick(stage, 15, 15, 128, 128, Constants.TouchpadType.MOVEMENT);
		fireStick = new Joystick(stage, Constants.VIEWPORT_WIDTH - 15 - 128, 15, 128, 128, Constants.TouchpadType.FIRE);
		
		quickSelection = new QuickSelection(stage);
		//itemView = new ItemView(stage);
		
		healthBar = new Healthbar(stage);
		
		Gdx.input.setInputProcessor(stage);
	}
	
}