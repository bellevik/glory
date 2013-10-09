package se.glory.zombieworld.screens;

import java.util.ArrayList;
import java.util.Random;

import se.glory.zombieworld.model.StageModel;
import se.glory.zombieworld.model.WorldModel;
import se.glory.zombieworld.model.entities.items.WeaponLoot;
import se.glory.zombieworld.model.entities.obstacles.CustomObstacle;
import se.glory.zombieworld.utilities.Constants;
import se.glory.zombieworld.utilities.Point;
import se.glory.zombieworld.utilities.TextureHandler;
import se.glory.zombieworld.view.GameView;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;

public class GameScreen implements Screen {
	//private QuickSelection quickSelection;
	//private ItemView itemView;
	private boolean isRunning = true;
	
	//private Healthbar healthBar;
	
	//private Stage stage;
	
	// moveStick controls player movement, fireStick controls item use
	//private Joystick moveStick, fireStick;
	
	private WorldModel worldModel;
	private GameView gameView;
	
	/*
	 * This method will be called all the time throughout the game. Libgdx method!
	 */
	@Override
	public void render(float delta) {
		gameView.render();
		
		// Update player movement
		WorldModel.player.getBody().setLinearVelocity(StageModel.moveStick.getTouchpad().getKnobPercentX() * 10, StageModel.moveStick.getTouchpad().getKnobPercentY() * 10);
		
		//The four floats below will represent the percentage in X and Y direction of the Joysticks
		float moveKnobX = StageModel.moveStick.getTouchpad().getKnobPercentX();
		float moveKnobY = StageModel.moveStick.getTouchpad().getKnobPercentY();
		float fireKnobX = StageModel.fireStick.getTouchpad().getKnobPercentX();
		float fireKnobY = StageModel.fireStick.getTouchpad().getKnobPercentY();
		//This method will rotate the player
		WorldModel.player.applyRotationToPlayer(moveKnobX, moveKnobY, fireKnobX, fireKnobY);
		
		//If someone is touching the right joystick then we need the player to be ready to shoot
		if (fireKnobX != 0 && fireKnobY != 0) {
			WorldModel.player.shoot();
		}
		
		if(isRunning) {
			StageModel.quickSelection.selectItem();
		} else {
			StageModel.itemView.manageItems();
			StageModel.quickSelection.manageItems();
		}
		
		
		// Animator.drawAnimation(batch, player.getBody().getPosition().x, player.getBody().getPosition().y);
		// player.getAnimation().drawAnimation(batch, player.getBody().getPosition().x, player.getBody().getPosition().y);
		
		StageModel.stage.act(delta);
		StageModel.stage.draw();
		
		worldModel.update();
		
		/*float t = 120;
		float fps = Gdx.graphics.getFramesPerSecond();
		if (fps != 0) {
			t = (60 / fps) * 120;
		}*/
		
		WorldModel.world.step(1/120f, 6, 2);
		
		
		// ############### EVENTS
		Cell c = gameView.getMapLayer("events").getCell((int)WorldModel.player.getTileX(), (int)WorldModel.player.getTileY());
		
		if (c != null) {
			 if (c.getTile().getProperties().get("indoors") != null) {
				 if (c.getTile().getProperties().get("indoors").toString().equals("1")) {
					 if (gameView.getMapLayer("roof").isVisible())
						 gameView.getMapLayer("roof").setVisible(false);
				 } else if (c.getTile().getProperties().get("indoors").toString().equals("0")) {
					 if (!gameView.getMapLayer("roof").isVisible())
						 gameView.getMapLayer("roof").setVisible(true);
				 }
			 }
		}
	}

	@Override
	public void resize(int width, int height) {
		double scale = Constants.VIEWPORT_WIDTH / (double) width;
		Constants.VIEWPORT_HEIGHT = (int) (height * scale);
		
		gameView.getCamera().viewportWidth = Constants.VIEWPORT_WIDTH;
		gameView.getCamera().viewportHeight = Constants.VIEWPORT_HEIGHT;
		
	    StageModel.stage.setViewport(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT, false);
	    StageModel.quickSelection.updatePosition();
	    StageModel.healthBar.updatePosition();
	}
	
	/*
	 * This method will set a constant for scaling the window. Really good when Android
	 * got so many different screen sizes.
	 */
	private void adjustViewportScale() {
		double scale = Constants.VIEWPORT_WIDTH / (double) Gdx.graphics.getWidth();
		Constants.VIEWPORT_HEIGHT = (int) (Gdx.graphics.getHeight() * scale);
	}
	
	/*
	 * This method will be called upon screen load. Libgdx method!
	 */
	@Override
	public void show() {
		adjustViewportScale();
		
		worldModel = new WorldModel();
		worldModel.createWorld();
		
		// This line will import all the images that will be used multiple times
		TextureHandler.createTextures();
		
		SpriteBatch batch = new SpriteBatch();
		
		gameView = new GameView(batch);
		worldModel.setupAIModel(gameView.getMapLayer("blocked"));
		
		StageModel.createUI(batch);
		
		new WeaponLoot(100, 100);
		new WeaponLoot(100, 200);
		new WeaponLoot(300, 100);
		new WeaponLoot(200, 200);
		
		// ## Add humans
		addRandomHumans(20);
		
		// ## Add zombies
		//worldModel.getAIModel().addZombie(16+50*16, 16+50*16);
		
		createStaticWalls();
	}
	
	private void addRandomHumans(int num) {
		int mapWidth = gameView.getMapLayer("blocked").getWidth();
		int mapHeight = gameView.getMapLayer("blocked").getHeight();
		ArrayList<Point> blockedTiles = worldModel.getAIModel().getBlockedTiles();
		
		Random generator = new Random();
		
		for (int i = 0; i < num; i++) {
			int goalX = generator.nextInt(mapWidth);
			int goalY = generator.nextInt(mapHeight);
			
			while (blockedTiles.contains(new Point(goalX, goalY))) {
				goalX = generator.nextInt(mapWidth);
				goalY = generator.nextInt(mapHeight);
			}
			
			worldModel.getAIModel().addHuman(16+goalX*16, 16+goalY*16);
		}
	}

	private void createStaticWalls() {
		TiledMapTileLayer collideLayer = gameView.getMapLayer("blocked");
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
							new CustomObstacle(x * 16, start * 16, 16, (end - start + 1) * 16);
						} else {
							if (y == collideLayer.getHeight() - 1)
								lonelyWalls[x][y] = true;
							else
								// TODO: Why do we need this fix?
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
						new CustomObstacle(start * 16, y * 16, (end - start + 1) * 16, 16);
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
		StageModel.stage.dispose();
	}
}