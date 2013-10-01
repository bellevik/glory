package se.glory.utilities;

import se.glory.entities.Player;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class WorldHandler {
	
	public static World world;
	public static Array<Body> drawableBodies = new Array<Body>();
	public static Array<Body> removeableBodies = new Array<Body>();
	public static Player player;
	
	public static void createWorld() {
		world = new World(new Vector2(0, 0), true);
	}

}
