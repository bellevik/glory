package se.glory.zombieworld.screens;

import se.glory.zombieworld.model.WorldModel;
import se.glory.zombieworld.model.entities.items.QuickSelection;
import se.glory.zombieworld.utilities.Constants;
import se.glory.zombieworld.utilities.Joystick;
import se.glory.zombieworld.utilities.TextureHandler;
import se.glory.zombieworld.view.GameView;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class GameScreen implements Screen {
	private QuickSelection quickSelection;
	
	private Stage stage;
	
	// moveStick controls player movement, fireStick controls item use
	private Joystick moveStick, fireStick;
	
	private WorldModel worldModel;
	private GameView gameView;
	
	@Override
	public void render(float delta) {
		gameView.render();
		
		// Update player movement
		WorldModel.player.getBody().setLinearVelocity(moveStick.getTouchpad().getKnobPercentX() * 2, moveStick.getTouchpad().getKnobPercentY() * 2);
		
		//The four floats below will represent the percentage in X and Y direction of the Joysticks
		float moveKnobX = moveStick.getTouchpad().getKnobPercentX();
		float moveKnobY = moveStick.getTouchpad().getKnobPercentY();
		float fireKnobX = fireStick.getTouchpad().getKnobPercentX();
		float fireKnobY = fireStick.getTouchpad().getKnobPercentY();
		//This method will rotate the player
		WorldModel.player.applyRotationToPlayer(moveKnobX, moveKnobY, fireKnobX, fireKnobY);
		
		quickSelection.selectItem();
		
		// Animator.drawAnimation(batch, player.getBody().getPosition().x, player.getBody().getPosition().y);
		// player.getAnimation().drawAnimation(batch, player.getBody().getPosition().x, player.getBody().getPosition().y);
		
		stage.act(delta);
		stage.draw();
		
		WorldModel.world.step(1/60f, 6, 2);
		worldModel.update();
	}

	@Override
	public void resize(int width, int height) {
		gameView.getCamera().viewportWidth = width;
		gameView.getCamera().viewportHeight = height;
	}

	@Override
	public void show() {
		worldModel = new WorldModel();
		worldModel.createWorld();
		
		// This line will import all the images that will be used multiple times
		TextureHandler.createTextures();
		
		SpriteBatch batch = new SpriteBatch();
		
		gameView = new GameView(batch);
		worldModel.setupAIModel(gameView.getMapLayer(1));
		
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true, batch);
		
		moveStick = new Joystick(stage, 15, 15, 128, 128, Constants.TouchpadType.MOVEMENT);
		fireStick = new Joystick(stage, Gdx.graphics.getWidth() - 15 - 128, 15, 128, 128, Constants.TouchpadType.FIRE);
		
		quickSelection = new QuickSelection(stage);
		
		Gdx.input.setInputProcessor(stage);
		
		// ## Add humans
		worldModel.getAIModel().addHuman(496, 272);
		
		// ## Add zombies
		worldModel.getAIModel().addZombie(272, 272);
	}

	@Override
	public void hide() {
		
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void dispose() {
		WorldModel.world.dispose();
		gameView.dispose();
		stage.dispose();
	}
	
}