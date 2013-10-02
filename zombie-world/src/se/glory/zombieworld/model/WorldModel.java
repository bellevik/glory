package se.glory.zombieworld.model;

import se.glory.zombieworld.model.entities.Player;
import se.glory.zombieworld.utilities.CollisionDetection;
import se.glory.zombieworld.utilities.Identity;

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
		
		player = new Player (300, 400, 32, 32);
		
		world.setContactListener(new CollisionDetection());
	}
	
	public void update() {
		// sweepDeadBodies();
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
