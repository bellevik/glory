package se.glory.zombieworld.utilities;

import java.util.Random;

import se.glory.zombieworld.model.entities.Zombie;

public class SpawnController {
	private static final int TILE_SIZE = 16;
	
	private static Point northEastSpawn = new Point(190 * TILE_SIZE, 242 * TILE_SIZE);
	private static Point northWestSpawn = new Point(57 * TILE_SIZE, 245 * TILE_SIZE);
	private static Point southEastSpawn = new Point(192 * TILE_SIZE, 50 * TILE_SIZE);
	private static Point southWestSpawn = new Point(23 * TILE_SIZE, 23 * TILE_SIZE);
	
	public static void spawnZombie () {
		Random r = new Random();
		int random = r.nextInt(4);
		switch (random) {
		case 0: 
			//new Zombie(northEastSpawn.getX(), northEastSpawn.getY());
			//new Zombie(1,1);
			break;
		case 1: 
			//new Zombie(northWestSpawn.getX(), northWestSpawn.getY());
			//new Zombie(1,1);
			break;
		case 2: 
			//new Zombie(southEastSpawn.getX(), southEastSpawn.getY());
			//new Zombie(1,1);
			break;
		case 3: 
			//new Zombie(southWestSpawn.getX(), southWestSpawn.getY());
			//new Zombie(1,1);
			break;
		default:
			break;
		}
	}
	
}
