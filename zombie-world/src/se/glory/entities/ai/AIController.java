package se.glory.entities.ai;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import se.glory.entities.Creature;
import se.glory.entities.Human;
import se.glory.entities.Zombie;
import se.glory.utilities.WorldHandler;

public class AIController {
	private ArrayList<Human> humans = new ArrayList<Human>();
	private ArrayList<Zombie> zombies = new ArrayList<Zombie>();
	private ArrayList<Point> blockedTiles = new ArrayList<Point>();
	
	public void addHuman(float x, float y) {
		humans.add(new Human(x, y));
	}
	
	public void removeHuman(Human human) {
		humans.remove(human);
	}
	
	public void addZombie(float x, float y) {
		zombies.add(new Zombie(x, y));
	}
	
	public void removeZombie(Zombie zombie) {
		zombies.remove(zombie);
	}
	
	public void setBlockedTiles(ArrayList<Point> blockedTiles) {
		this.blockedTiles = blockedTiles;
	}
	
	public void update() {
		updateHumans();
		updateZombies();
	}
	
	private void updateHumans() {
		for (Human h : humans) {
			float totX = 0;
			float totY = 0;
			
			for (Zombie z : zombies) {
				float tmpX = h.getBody().getPosition().x - z.getBody().getPosition().x;
				float tmpY = h.getBody().getPosition().y - z.getBody().getPosition().y;
				
				double size = Math.sqrt(tmpX * tmpX + tmpY * tmpY);
				
				// Range 1.5
				if (size < 1.5) {
					double test = 1.5 - size;
					
					tmpX /= (size/test);
					tmpY /= (size/test);
					
					totX += tmpX;
					totY += tmpY;
				}
			}
			
			// If any zombie are nearby, flee
			if (totX + totY != 0) {
				double size = Math.sqrt(totX * totX + totY * totY);
				
				totX /= size;
				totY /= size;
				
				h.getBody().setLinearVelocity(totX, totY);
				h.setState(Human.State.FLEEING);
			} else {
				if (h.getState() == Human.State.IDLE) {
					// TODO: Check if random tile is blocked
					Random generator = new Random();
					
					int goalX = generator.nextInt(40);
					int goalY = generator.nextInt(20);
					
					ArrayList<Point> walkPath = AStarPathFinder.getShortestPath((int) h.getTileX(), (int) h.getTileY(), goalX, goalY, blockedTiles);
					h.setWalkPath(walkPath);
					
					h.setState(Human.State.WALKING);
				}
				
				// WALK
				h.walk();
			}
		}
	}
	
	private void updateZombies() {
		for (Zombie z : zombies) {
			Creature closestTarget = getClosestTarget(z);
			
			// If any humans are nearby, chase
			if (closestTarget != null) {
				float tmpX = closestTarget.getBody().getPosition().x - z.getBody().getPosition().x;
				float tmpY = closestTarget.getBody().getPosition().y - z.getBody().getPosition().y;
				
				double size = Math.sqrt(tmpX*tmpX+tmpY*tmpY);
				
				tmpX /= size;
				tmpY /= size;
				
				z.getBody().setLinearVelocity(tmpX, tmpY);
				z.setState(Zombie.State.CHASING);
			} else {
				if (z.getState() == Zombie.State.IDLE) {
					// TODO: Check if random tile is blocked
					Random generator = new Random();
					
					int goalX = generator.nextInt(40);
					int goalY = generator.nextInt(20);
					
					ArrayList<Point> walkPath = AStarPathFinder.getShortestPath((int) z.getTileX(), (int) z.getTileY(), goalX, goalY, blockedTiles);
					z.setWalkPath(walkPath);
					
					z.setState(Zombie.State.WALKING);
				}
				
				// WALK
				z.walk();
			}
		}
	}
	
	private Creature getClosestTarget(Zombie z) {
		double distance = Double.MAX_VALUE;
		Creature closestTarget = null;
		
		for (Human h : humans) {
			float tmpX = h.getBody().getPosition().x - z.getBody().getPosition().x;
			float tmpY = h.getBody().getPosition().y - z.getBody().getPosition().y;
			
			double size = Math.sqrt(tmpX * tmpX + tmpY * tmpY);
			
			// Range and closest
			if (size < 3.5 && size < distance) {
				closestTarget = h;
				distance = size;
			}
		}
		
		// Check if player is closer
		float tmpX = WorldHandler.player.getBody().getPosition().x - z.getBody().getPosition().x;
		float tmpY = WorldHandler.player.getBody().getPosition().y - z.getBody().getPosition().y;
		
		double size = Math.sqrt(tmpX * tmpX + tmpY * tmpY);
		
		// Range and closest
		if (size < 3.5 && size < distance) {
			closestTarget = WorldHandler.player;
		}
		
		return closestTarget;
	}
}