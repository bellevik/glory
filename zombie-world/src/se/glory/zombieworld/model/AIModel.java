package se.glory.zombieworld.model;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import se.glory.zombieworld.model.entities.Creature;
import se.glory.zombieworld.model.entities.Human;
import se.glory.zombieworld.model.entities.Zombie;
import se.glory.zombieworld.utilities.AStarPathFinder;
import se.glory.zombieworld.utilities.Constants;
import se.glory.zombieworld.utilities.Identity;
import se.glory.zombieworld.utilities.Point;
import se.glory.zombieworld.utilities.Score;
import se.glory.zombieworld.utilities.UtilityTimer;

// TODO: If zombie is chasing a human and lose trail, 
// it needs a new path - now it will use the same => try to walk through walls!

public class AIModel {
	private ArrayList<Human> humans = new ArrayList<Human>();
	private ArrayList<Human> deadHumans = new ArrayList<Human>();
	private ArrayList<Zombie> zombies = new ArrayList<Zombie>();
	private ArrayList<Zombie> deadZombies = new ArrayList<Zombie>();
	
	private ArrayList<Point> blockedTiles = new ArrayList<Point>();
	private int mapWidth = 0;
	private int mapHeight = 0;
	
	private Random gen;
	
	public AIModel() {
		gen = new Random();
	}
	
	public ArrayList<Point> getBlockedTiles() {
		return blockedTiles;
	}
	
	public void addHuman(float x, float y) {
		humans.add(new Human(x, y));
	}
	
	public void removeHuman(Human human) {
		Gdx.app.error("MyTag", "TO REMOVE HUMAN");
		((Identity)human.getBody().getUserData()).setDead(true);
		humans.remove(human);
		Gdx.app.error("MyTag", "REMOVED HUMAN");
	}
	
	public void addZombie(float x, float y) {
		Gdx.app.error("MyTag", "TO ADD ZOMBIE");
		Zombie z = new Zombie(x, y);
		((Identity)z.getBody().getUserData()).setDead(false);
		zombies.add(z);
		Gdx.app.error("MyTag", "ADDED ZOMBIE");
	}
	
	public void removeZombie(Zombie zombie) {
		Gdx.app.error("MyTag", "TO REMOVE ZOMBIE");
		((Identity)zombie.getBody().getUserData()).setDead(true);
		zombies.remove(zombie);
		Gdx.app.error("MyTag", "REMOVED ZOMBIE");
	}
	
	public void setMapSize(int mapWidth, int mapHeight) {
		this.mapWidth = mapWidth;
		this.mapHeight = mapHeight;
	}
	
	public void setBlockedTiles(ArrayList<Point> blockedTiles) {
		this.blockedTiles.clear();
		
		for (Point p : blockedTiles) {
			this.blockedTiles.add(new Point(p.getX(), p.getY()));
			
			// Add "extra blocks" to compensate for AI bodies being 2x2 tiles big.
			this.blockedTiles.add(new Point(p.getX() - 1, p.getY()));
			this.blockedTiles.add(new Point(p.getX(), p.getY() - 1));
		}
	}
	
	public void update() {
		for (Human h : humans) {
			if (h.getBody() == null)
				Gdx.app.error("MyTag", "ERROR 1");
		}
		
		for (Human h : deadHumans) {
			if (h.getBody() == null)
				Gdx.app.error("MyTag", "ERROR 2");
		}
		
		for (Zombie h : zombies) {
			if (h.getBody() == null)
				Gdx.app.error("MyTag", "ERROR 3");
		}
		
		for (Zombie h : deadZombies) {
			if (h.getBody() == null)
				Gdx.app.error("MyTag", "ERROR 4");
		}
		
		
		
		clearZombies();
		turnHumansToZombie();
		//clearZombies();
		// TODO Add new zombie if max creatures > current creatures
		updateHumansDumb();
		updateZombiesDumb();
	}
	
	private void updateHumansDumb() {
		for (Human h : humans) {
			// ##### Set rotation
			float directionX = h.getBody().getLinearVelocity().x == 0 ? 0.0001f : h.getBody().getLinearVelocity().x;
			float directionY = h.getBody().getLinearVelocity().y;
			
			float angle = (float) Math.atan(directionY/directionX);
			
			if (directionX < 0)
				angle = angle - (float)Math.PI;
			
			h.getBody().setTransform(h.getBody().getPosition(), angle);
			// ##### Set rotation
			
			float totX = 0;
			float totY = 0;
			
			int logicCounter = h.getLogicCounter();
			
			if (logicCounter < 0) {
				h.resetLogicCounter();
				
				for (Zombie z : zombies) {
					float tmpX = h.getBody().getPosition().x - z.getBody().getPosition().x;
					float tmpY = h.getBody().getPosition().y - z.getBody().getPosition().y;
					
					double size = Math.sqrt(tmpX * tmpX + tmpY * tmpY);
					
					// Range 2.0
					if (size < 2.0) {
						double distance = 2.0 - size;
						
						tmpX /= (size/distance);
						tmpY /= (size/distance);
						
						totX += tmpX;
						totY += tmpY;
					}
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
				// If the human just got away form a zombie, set its state to idle.
				if (logicCounter < 0 && h.getState() == Human.State.FLEEING)
					h.setState(Human.State.IDLE);
				
				if (h.getState() == Human.State.IDLE) {
					Random generator = new Random();
					
					int distance = generator.nextInt(200);
					Vector2 direction = new Vector2(0,1);
					direction.setAngle(generator.nextInt(360));
					
					h.setState(Human.State.DUMB_AI);
					h.setCollidingInfo(direction, distance);
				}
				
				h.walk();
			}
			updateHumanHealth(h);
		}
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
					double distance = 1.5 - size;
					
					tmpX /= (size/distance);
					tmpY /= (size/distance);
					
					totX += tmpX;
					totY += tmpY;
				}
			}
			
			// If any zombie are nearby, flee
			if (totX + totY != 0) {
				double size = Math.sqrt(totX * totX + totY * totY);
				
				totX /= size;
				totY /= size;
				
				float angle = (float) Math.atan(totY/totX);
				
				if (totX < 0)
					angle = angle - (float) Math.PI;
				
				h.getBody().setTransform(h.getBody().getPosition(), angle);		
				h.getBody().setLinearVelocity(totX, totY);
				h.setState(Human.State.FLEEING);
			} else {
				// If the human just got away form a zombie, set its state to idle.
				if (h.getState() == Human.State.FLEEING)
					h.setState(Human.State.IDLE);
				
				if (h.getState() == Human.State.IDLE) {
					Random generator = new Random();
					
					int goalX = generator.nextInt(mapWidth);
					int goalY = generator.nextInt(mapHeight);
					
					while (blockedTiles.contains(new Point(goalX, goalY))) {
						goalX = generator.nextInt(mapWidth);
						goalY = generator.nextInt(mapHeight);
					}
					
					int x = (int) h.getTileX();
					int y = (int) h.getTileY();
					
					if (x != 0 || y != 0) {
						ArrayList<Point> walkPath = AStarPathFinder.getShortestPath(x, y, goalX, goalY, blockedTiles);
						h.setWalkPath(walkPath);
						h.setState(Human.State.WALKING);
					} else {
						System.out.println("### calculate zombie new path: error: human");
					}
				}
				
				// WALK
				h.walk();
			}
			updateHumanHealth(h);
		}
	}
	
	private void updateZombiesDumb() {
		for (Zombie z : zombies) {
			// ##### Set rotation
			float directionX = z.getBody().getLinearVelocity().x == 0 ? 0.0001f : z.getBody().getLinearVelocity().x;
			float directionY = z.getBody().getLinearVelocity().y;
			
			float angle = (float) Math.atan(directionY/directionX);
			
			if (directionX < 0)
				angle = angle - (float)Math.PI;
			
			z.getBody().setTransform(z.getBody().getPosition(), angle);
			// ##### Set rotation
			
			Creature closestTarget = null;
			
			int logic = z.getLogicCounter();
			if (logic < 0) {
				closestTarget = getClosestTarget(z);
				z.resetLogicCounter();
			}
			
			// If any humans are nearby, chase
			if (closestTarget != null) {
				float tmpX = closestTarget.getBody().getPosition().x - z.getBody().getPosition().x;
				float tmpY = closestTarget.getBody().getPosition().y - z.getBody().getPosition().y;
				
				double size = Math.sqrt(tmpX*tmpX+tmpY*tmpY);
				
				tmpX /= size * 1.2;
				tmpY /= size * 1.2;
					
				z.getBody().setLinearVelocity(tmpX, tmpY);
				z.setState(Zombie.State.CHASING);
			} else {
				// If the zombie just ended chasing a human, set its state to idle.
				if (logic < 0 && z.getState() == Zombie.State.CHASING)
					z.setState(Zombie.State.IDLE);
				
				if (z.getState() == Zombie.State.IDLE) {
					Random generator = new Random();
					
					int distance = generator.nextInt(200);
					Vector2 direction = new Vector2(0,1);
					direction.setAngle(generator.nextInt(360));
					
					z.setState(Zombie.State.DUMB_AI);
					z.setCollidingInfo(direction, distance);
				}
				
				z.walk();
			}
			checkZombieHealth(z);
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
				
				tmpX /= size * 1.2;
				tmpY /= size * 1.2;
				
				float angle = (float) Math.atan(tmpY/tmpX);
				
				if (tmpX < 0)
					angle = angle - (float)Math.PI;
				
				z.getBody().setTransform(z.getBody().getPosition(), angle);	
				z.getBody().setLinearVelocity(tmpX, tmpY);
				z.setState(Zombie.State.CHASING);
			} else {
				if (z.getState() == Zombie.State.IDLE) {
					Random generator = new Random();
					
					int goalX = generator.nextInt(mapWidth);
					int goalY = generator.nextInt(mapHeight);
					
					while (blockedTiles.contains(new Point(goalX, goalY))) {
						goalX = generator.nextInt(mapWidth);
						goalY = generator.nextInt(mapHeight);
					}
					
					int x = (int) z.getTileX();
					int y = (int) z.getTileY();
					
					if (x != 0 || y != 0) {
						ArrayList<Point> walkPath = AStarPathFinder.getShortestPath(x, y, goalX, goalY, blockedTiles);
						z.setWalkPath(walkPath);
						z.setState(Zombie.State.WALKING);
					} else {
						System.out.println("### calculate zombie new path: error: zombie");
					}
				}
				
				// WALK
				z.walk();
			}
			checkZombieHealth(z);
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
			if (size < 6 && size < distance) {
				closestTarget = h;
				distance = size;
			}
		}
		
		// Check if player is closer
		float tmpX = WorldModel.player.getBody().getPosition().x - z.getBody().getPosition().x;
		float tmpY = WorldModel.player.getBody().getPosition().y - z.getBody().getPosition().y;
		
		double size = Math.sqrt(tmpX * tmpX + tmpY * tmpY);
		
		// Range and closest
		if (size < 6 && size < distance) {
			closestTarget = WorldModel.player;
		}
		
		return closestTarget;
	}
	
	//Updates human health if they are infected
	private void updateHumanHealth(Human h) {
		if (h == null)
			Gdx.app.error("MyTag", "ERROR 7");
		
		UtilityTimer infectedHealthTimer = h.getInfectedHealthTimer();
		
		if(infectedHealthTimer != null && infectedHealthTimer.isDone()) {
			h.changeHealth(-Constants.INFECTED_DAMAGE);
			infectedHealthTimer.resetTimer();
		}
		
		if(h.getHealth() == 0) {
			deadHumans.add(h);
		}
	}
	
	//Adds zombie to the deadZombie list if the health is zero so the body can be removed correctly
	private void checkZombieHealth(Zombie z) {
		if (z == null)
			Gdx.app.error("MyTag", "ERROR 6");
		
		if(z.getHealth() <= 0) {
			deadZombies.add(z);
		}
	}
	
	//Will replace the dead humans to zombies if they were infected. otherwise they will just be removed
	private void turnHumansToZombie() {
		for (Human h : deadHumans) {
			float xPos = h.getBody().getPosition().x/Constants.WORLD_TO_BOX;
			float yPos = h.getBody().getPosition().y/Constants.WORLD_TO_BOX;
			removeHuman(h);
			if(h.getInfectedHealthTimer() != null) {
				addZombie(xPos, yPos);
			}
		}
		
		deadHumans.clear();
	}
	
	//Clears the zombies from the screen
	private void clearZombies() {
		for (Zombie z : deadZombies) {
			removeZombie(z);
		}
		deadZombies.clear();
	}
}