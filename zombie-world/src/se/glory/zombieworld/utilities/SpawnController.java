package se.glory.zombieworld.utilities;

import java.util.ArrayList;
import java.util.Random;

import se.glory.zombieworld.model.AIModel;

public class SpawnController {
	private int TILE_SIZE = 16;
	
	private ArrayList<Point> spawnPoints = new ArrayList<Point>();
	private Random gen;
	private AIModel aiModel;
	
	public SpawnController(AIModel model) {
		aiModel = model;
		gen = new Random();
		
		spawnPoints.add(new Point(190 * TILE_SIZE, 242 * TILE_SIZE));
		spawnPoints.add(new Point(57 * TILE_SIZE, 245 * TILE_SIZE));
		spawnPoints.add(new Point(192 * TILE_SIZE, 50 * TILE_SIZE));
		spawnPoints.add(new Point(23 * TILE_SIZE, 23 * TILE_SIZE));
		
		spawnPoints.add(new Point(96 * TILE_SIZE, 135 * TILE_SIZE));
		spawnPoints.add(new Point(127 * TILE_SIZE, 224 * TILE_SIZE));
		spawnPoints.add(new Point(21 * TILE_SIZE, 87 * TILE_SIZE));
		spawnPoints.add(new Point(2 * TILE_SIZE, 188 * TILE_SIZE));
	}
	
	public void spawnCreature(Constants.MoveableBodyType type) {
		int random = gen.nextInt(spawnPoints.size());
		
		if (type == Constants.MoveableBodyType.HUMAN) {
			aiModel.addHuman(spawnPoints.get(random).getX(), spawnPoints.get(random).getY());
		} else if (type == Constants.MoveableBodyType.ZOMBIE) {
			aiModel.addZombie(spawnPoints.get(random).getX(), spawnPoints.get(random).getY());
		}
	}
}