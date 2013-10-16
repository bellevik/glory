package se.glory.zombieworld.screens;

import se.glory.zombieworld.model.StageModel;
import se.glory.zombieworld.model.WorldModel;
import se.glory.zombieworld.model.entities.items.ShopView;
import se.glory.zombieworld.model.entities.items.WeaponLoot;
import se.glory.zombieworld.model.entities.obstacles.CustomObstacle;
import se.glory.zombieworld.model.entities.obstacles.StreetObject;
import se.glory.zombieworld.utilities.Constants;
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
	//private boolean isRunning = true;
	
	//private Healthbar healthBar;
	
	//private Stage stage;
	
	// moveStick controls player movement, fireStick controls item use
	//private Joystick moveStick, fireStick;
	
	private WorldModel worldModel;
	private GameView gameView;
	private ShopView shopView;
	
	/*
	 * This method will be called all the time throughout the game. Libgdx method!
	 */
	@Override
	public void render(float delta) {
		gameView.render();
		
		if(Constants.isRunning) {
			// Update player movement
			WorldModel.player.getBody().setLinearVelocity(StageModel.moveStick.getTouchpad().getKnobPercentX() * 2, StageModel.moveStick.getTouchpad().getKnobPercentY() * 2);
			
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
			
			WorldModel.world.step(1/60f, 6, 2);
			worldModel.update();
			
			StageModel.quickSelection.selectItem();
			StageModel.itemView.hideContainers();
			if(StageModel.pauseButton.isTouched()) {
				Constants.isRunning = false;
			}
		} else {
			if(StageModel.pauseButton.isTouched()) {
				Constants.isRunning = true;
			}
			StageModel.itemView.manageItems();
			StageModel.quickSelection.manageItems();
		}
		
		// Animator.drawAnimation(batch, player.getBody().getPosition().x, player.getBody().getPosition().y);
		// player.getAnimation().drawAnimation(batch, player.getBody().getPosition().x, player.getBody().getPosition().y);
		
		StageModel.stage.act(delta);
		StageModel.stage.draw();
		
		WorldModel.world.step(1/60f, 6, 2);
		worldModel.update();
		
		testHealthBar();
		
		//System.out.println("player at : "+ worldModel.player.getBody().getPosition().x+ ", "+worldModel.player.getBody().getPosition().y);
		
		// ###############
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
			 }else if (c.getTile().getProperties().get("buyzone") != null&& !WorldModel.player.getRecentlyBought()){
				 WorldModel.player.setRecentlyBought(true);
				 StageModel.shopView.setShopViewVisability(true);	
			 }
		}else{
			 WorldModel.player.setRecentlyBought(false);
		 }if (StageModel.shopView.getShopViewVisabiliy()){
			 StageModel.shopView.checkClicked();
		 }
	}
	
	
	private int healthVar = 0;
	private int negVar = 1;
	private void testHealthBar() {
		healthVar += negVar;
		StageModel.healthBar.updateHealth(healthVar);
		if(healthVar == 100 || healthVar == 0) {
			negVar *= -1;

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
	    StageModel.itemView.updatePosition();
	    StageModel.pauseButton.updatePosition();
	}
	
	/*
	 * This method will set a constant for scaling the window. Really good when Android
	 * got so many different screen siezes.
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
		
		// ## Add humans
		worldModel.getAIModel().addHuman(16+22*16, 16+8*16);
		worldModel.getAIModel().addHuman(16+22*16, 16+15*16);
		
		// ## Add zombies
		// worldModel.getAIModel().addZombie(272, 272);
		
		createStaticWalls();
		createObjects();
		
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
	private void createObjects(){
		TiledMapTileLayer objectLayer = gameView.getMapLayer("objects");
		
		for (int x = 0; x < objectLayer.getWidth(); x++) {
			for (int y = 0; y < objectLayer.getHeight(); y++) {
				Cell c = objectLayer.getCell(x, y);
				
				if (c != null) {
					new StreetObject(c.getTile().getProperties().get("object").toString(),x*16+8,y*16+8);
					System.out.println("created at : "+ x+ ", "+y);
				}
				
			}		
		}
		
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