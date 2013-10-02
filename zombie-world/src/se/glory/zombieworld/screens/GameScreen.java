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
		
		// -------------REFACTOR THIS METHOD!-------------
		applyRotationToPlayer(delta);
		quickSelection.selectItem();
		
		// Animator.drawAnimation(batch, player.getBody().getPosition().x, player.getBody().getPosition().y);
		// player.getAnimation().drawAnimation(batch, player.getBody().getPosition().x, player.getBody().getPosition().y);
		
		stage.act(delta);
		stage.draw();
		
		WorldModel.world.step(1/60f, 6, 2);
		worldModel.update();
	}
	
	/*
	 * This method will rotate the player according to what angle the touchpads got
	 */
	public void applyRotationToPlayer(float delta) {
		//-------RECHECK THIS CONDITION. IT WILL STOP AT x=0 and y=0. i.e MOVE
		//-------STRAIGHT TO THE RIGHT
		if (fireStick.getTouchpad().getKnobPercentX() != 0 && fireStick.getTouchpad().getKnobPercentY() != 0) {
			
			float knobX = fireStick.getTouchpad().getKnobPercentX();
			float knobY = fireStick.getTouchpad().getKnobPercentY();
			
			float playerDegree = (int) (WorldModel.player.getBody().getTransform().getRotation() * MathUtils.radiansToDegrees);
			float knobDegree, totalRotation;
			
			if (knobY >= 0) {
				knobDegree = (int) (Math.acos(knobX) * MathUtils.radiansToDegrees);
			} else {
				knobDegree = -(int) (Math.acos(knobX) * MathUtils.radiansToDegrees);
			}
			totalRotation = knobDegree - playerDegree;
			
			WorldModel.player.getBody().setTransform(WorldModel.player.getBody().getPosition(), knobDegree * MathUtils.degreesToRadians);
			WorldModel.player.getBody().getJointList().get(0).joint.getBodyB().setTransform(WorldModel.player.getBody().getJointList().get(0).joint.getBodyB().getPosition(), knobDegree * MathUtils.degreesToRadians);
			WorldModel.player.getBody().getJointList().get(0).joint.getBodyB().setAwake(true);
		} else {
			if (moveStick.getTouchpad().getKnobPercentX() != 0 && moveStick.getTouchpad().getKnobPercentY() != 0) {
				float knobX = moveStick.getTouchpad().getKnobPercentX();
				float knobY = moveStick.getTouchpad().getKnobPercentY();
				
				float playerDegree = (int) (WorldModel.player.getBody().getTransform().getRotation() * MathUtils.radiansToDegrees);
				float knobDegree, totalRotation;
				
				if (knobY >= 0) {
					knobDegree = (int) (Math.acos(knobX) * MathUtils.radiansToDegrees);
				} else {
					knobDegree = -(int) (Math.acos(knobX) * MathUtils.radiansToDegrees);
				}
				
				totalRotation = knobDegree - playerDegree;
				
				WorldModel.player.getBody().setTransform(WorldModel.player.getBody().getPosition(), knobDegree * MathUtils.degreesToRadians);
				WorldModel.player.getBody().getJointList().get(0).joint.getBodyB().setTransform(WorldModel.player.getBody().getJointList().get(0).joint.getBodyB().getPosition(), knobDegree * MathUtils.degreesToRadians);
				WorldModel.player.getBody().getJointList().get(0).joint.getBodyB().setAwake(true);
			}
		}
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
		worldModel.getAIModel().addHuman(64, 64);
		
		// ## Add zombies
		// worldModel.getAIModel().addZombie(160, 64);
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