package se.glory.zombieworld.model;

import java.util.ArrayList;

import se.glory.zombieworld.model.entities.Identity;
import se.glory.zombieworld.model.entities.Player;
import se.glory.zombieworld.model.entities.items.Bullet;
import se.glory.zombieworld.model.entities.items.WeaponArsenal;
import se.glory.zombieworld.utilities.CollisionDetection;
import se.glory.zombieworld.utilities.Constants;
import se.glory.zombieworld.utilities.UtilityTimer;
import se.glory.zombieworld.utilities.misc.Point;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class WorldModel {
	public static World world;
	private AIModel aiModel;
	
	public static Player player;
	
	public static WeaponArsenal weaponArsenal;
	
	public static Array<Body> drawableBodies = new Array<Body>();
	public static Array<Body> removeableBodies = new Array<Body>();
	
	public void createWorld() {
		world = new World(new Vector2(0, 0), true);
		aiModel = new AIModel();
		
		player = new Player(600, 600, 16, 16);
		
		weaponArsenal = new WeaponArsenal();
		
		world.setContactListener(new CollisionDetection());
	}
	
	public AIModel getAIModel() {
		return aiModel;
	}
	
	public void setupAIModel(TiledMapTileLayer collideLayer) {
		ArrayList<Point> blockedTiles = new ArrayList<Point>();

		for (int x = 0; x < collideLayer.getWidth(); x++) {
			for (int y = 0; y < collideLayer.getHeight(); y++) {
				Cell c = collideLayer.getCell(x, y);
				if (c != null)
					blockedTiles.add(new Point(x, y));
			}
		}

		aiModel.setBlockedTiles(blockedTiles);
		aiModel.setMapSize(collideLayer.getWidth(), collideLayer.getHeight());
	}
	
	public void update() {
		sweepDeadBodies();
		aiModel.update();
		healthUpdate();
	}
	
	/*
	 * This method will loop through all the bodies in the world and check if the
	 * body got the isDead boolean as true. If it is true then remove the body from
	 * the world. This needs to be done separately and after the world.step
	 * method. Otherwise libgdx will crash. 
	 */
	public void sweepDeadBodies() {
		WorldModel.world.getBodies(WorldModel.removeableBodies);
		
		for (Body body : WorldModel.removeableBodies) {
			if (body != null) {
				if (body.getUserData() != null){
					if (body.getUserData().getClass().equals(Identity.class)) {
						Identity id = (Identity)body.getUserData();
						if (id.isDead()) {
							if (!WorldModel.world.isLocked()) {
								if(id.getType() == Constants.MoveableBodyType.BULLET) {
									((Bullet)id.getObj()).destroyTimer();
								}
								
								WorldModel.world.destroyBody(body);
							}
						}
					}
				}
			}
		}
		
		WorldModel.removeableBodies.clear();
	}
	
	public void healthUpdate() {
		UtilityTimer infectedHealthTimer = player.getInfectedHealthTimer();
		if(infectedHealthTimer != null && infectedHealthTimer.isDone()) {
			player.changeHealth(-Constants.INFECTED_DAMAGE);
			infectedHealthTimer.resetTimer();
		}
		
		if(StageModel.healthBar.getHealthPercentGoal() != player.getHealthPercentage()) {
			StageModel.healthBar.setHealthPercentGoal(player.getHealthPercentage());
		}
		StageModel.healthBar.updateHealthMovementSlowly();
	}
}
