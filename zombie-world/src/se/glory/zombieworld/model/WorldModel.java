package se.glory.zombieworld.model;

import java.util.ArrayList;

import se.glory.zombieworld.model.entities.Player;
import se.glory.zombieworld.utilities.CollisionDetection;
import se.glory.zombieworld.utilities.Identity;
import se.glory.zombieworld.utilities.Point;

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
	
	public static Array<Body> drawableBodies = new Array<Body>();
	public static Array<Body> removeableBodies = new Array<Body>();
	
	public void createWorld() {
		world = new World(new Vector2(0, 0), true);
		aiModel = new AIModel();
		
		player = new Player (300, 400, 16, 16);
		
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
	}
	
	public void update() {
		// sweepDeadBodies();
		aiModel.update();
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
			if ( body.getUserData().getClass().equals(Identity.class) ) {
				if(body!=null) {
					if (((Identity)(body.getUserData())).isDead()) {
						if (!WorldModel.world.isLocked()) {
							WorldModel.world.destroyBody(body);
						}
					}
				}
			}
		}
		
		WorldModel.removeableBodies.clear();
	}
}