package se.glory.zombieworld.screens;

import se.glory.zombieworld.model.WorldModel;
import se.glory.zombieworld.model.entities.items.QuickSelection;
import se.glory.zombieworld.model.entities.obstacles.CustomObstacle;
import se.glory.zombieworld.utilities.Constants;
import se.glory.zombieworld.utilities.Joystick;
import se.glory.zombieworld.utilities.TextureHandler;
import se.glory.zombieworld.view.GameView;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
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
		double scale = 1000 / (double) width;
		
		gameView.getCamera().viewportWidth = 1000;
		gameView.getCamera().viewportHeight = (int) (height * scale);
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
		worldModel.getAIModel().addHuman(16+10*32, 16+3*32);
		worldModel.getAIModel().addHuman(16+15*32, 16+15*32);
		worldModel.getAIModel().addHuman(16+16*32, 16+20*32);
		worldModel.getAIModel().addHuman(16+8*32, 16+20*32);
		worldModel.getAIModel().addHuman(16+30*32, 16+15*32);
		worldModel.getAIModel().addHuman(16+30*32, 16+23*32);
		
		// ## Add zombies
		// worldModel.getAIModel().addZombie(272, 272);
		
		createStaticWalls();
	}

	private void createStaticWalls() {
		TiledMapTileLayer collideLayer = gameView.getMapLayer(1);
		boolean[][] lonelyWalls = new boolean[collideLayer.getWidth()][collideLayer.getHeight()];
		
		for (int x = 0; x < collideLayer.getWidth(); x++) {
			int start = -1;
			int end = -1;
			
			for (int y = 0; y < collideLayer.getHeight(); y++) {
				Cell c = collideLayer.getCell(x, y);
				
				if (c != null) {
					// Is blocked
					
					if (start == -1)
						start = y;
					end = y;
				}
				
				if (c == null || y == collideLayer.getHeight() - 1) {
					if (start != -1) {
						if (start != end) {
							new CustomObstacle(x * 32, start * 32, 32, (end - start + 1) * 32);
						} else {
							// TODO: Why do we need this fix?
							if (y == collideLayer.getHeight() - 1)
								lonelyWalls[x][y] = true;
							else
								lonelyWalls[x][y-1] = true;
						}
					}
					
					start = -1;
					end = -1;
				}
			}
		}
		
		createStaticWallsHorizontal(lonelyWalls);
	}
	
	private void createStaticWallsHorizontal(boolean[][] lonelyWalls) {
		for (int y = 0; y < lonelyWalls[0].length; y++) {
			int start = -1;
			int end = -1;
			
			for (int x = 0; x < lonelyWalls.length; x++) {
				boolean c = lonelyWalls[x][y];
				
				if (c == true) {
					// Is blocked
					
					if (start == -1)
						start = x;
					end = x;
				}
				
				if (c == false || x == lonelyWalls.length - 1) {
					if (start != -1) {
						new CustomObstacle(start * 32, y * 32, (end - start + 1) * 32, 32);
					}
					
					start = -1;
					end = -1;
				}
			}
		}
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