package se.glory.utilities;

import se.glory.entities.Player;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

/*
 * This class will hold variables associated with the world. All of the
 * variable will be static so we can easily reach them from other classes.
 * World and Player will always be the same throughout the game.
 */
public class WorldHandler {
	
	public static World world;
	
	public static Player player;
	
	public static Array<Body> drawableBodies = new Array<Body>();
	public static Array<Body> removeableBodies = new Array<Body>();
	
	public static void createWorld() {
		world = new World(new Vector2(0, 0), true);
		player = new Player (300, 400, 32, 32);
	}

}
