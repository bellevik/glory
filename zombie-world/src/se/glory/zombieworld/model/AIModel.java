package se.glory.zombieworld.model;

import java.util.ArrayList;
import java.util.Random;

import se.glory.zombieworld.model.entities.Creature;
import se.glory.zombieworld.model.entities.Human;
import se.glory.zombieworld.model.entities.Zombie;
import se.glory.zombieworld.utilities.AStarPathFinder;
import se.glory.zombieworld.utilities.AStarPathFinder2;
import se.glory.zombieworld.utilities.Constants;
import se.glory.zombieworld.utilities.Point;
import se.glory.zombieworld.utilities.UtilityTimer;

// TODO: If zombie is chasing a human and lose trail, 
// it needs a new path - now it will use the same => try to walk through walls!

public class AIModel {
	private ArrayList<Human> humans = new ArrayList<Human>();
	private ArrayList<Human> zombieTurns = new ArrayList<Human>();
	private ArrayList<Zombie> zombies = new ArrayList<Zombie>();
	
	private ArrayList<Point> blockedTiles = new ArrayList<Point>();
	private boolean[][] blockedTilesBoolean;
	
	private int mapWidth = 0;
	private int mapHeight = 0;
	
	private Random generator = new Random();
	
	public void addHuman(float x, float y) {
		humans.add(new Human(x, y));
	}
	
	public void removeHuman(Human human) {
		WorldModel.world.destroyBody(human.getBody());
		humans.remove(human);
	}
	
	public void addZombie(float x, float y) {
		zombies.add(new Zombie(x, y));
	}
	
	public void removeZombie(Zombie zombie) {
		zombies.remove(zombie);
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
		
		setBlockedTiles2();
	}
	
	public void setBlockedTiles2() {
		blockedTilesBoolean = new boolean[mapWidth][mapHeight];
		
		for (Point p : blockedTiles) {
			if (p.getX() > -1 && p.getY() > -1)
				blockedTilesBoolean[p.getX()][p.getY()] = true;
		}
	}
	
	public ArrayList<Point> getBlockedTiles() {
		return blockedTiles;
	}
	
	public void update() {
		//turnHumansToZombie();
		
		//##
		long startTime = System.currentTimeMillis();
		updateHumans();
		long resultTime = System.currentTimeMillis() - startTime;
		
		/*if (resultTime > 30)
			System.out.println("AI-humans: " + resultTime);*/
			
		//##
		
		//updateZombies();
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
				
				//h.getBody().setTransform(h.getBody().getPosition(), angle);		
				//h.getBody().setLinearVelocity(totX, totY);
				h.setState(Human.State.FLEEING);
			} else {
				// If the human just got away form a zombie, set its state to idle.
				if (h.getState() == Human.State.FLEEING)
					h.setState(Human.State.IDLE);
				
				if (h.getState() == Human.State.IDLE) {
					// ###############
					int startX = (int) h.getTileX();
					int startY = (int) h.getTileY();
					
					int goalMinX = startX - 20;
					goalMinX = goalMinX < 1 ? 1 : goalMinX;
					
					int goalMaxX = startX + 20;
					goalMaxX = goalMaxX > mapWidth - 1 ? mapWidth - 1 : goalMaxX;
					
					int goalMinY = startY - 20;
					goalMinY = goalMinY < 1 ? 1 : goalMinY;
					
					int goalMaxY = startY + 20;
					goalMaxY = goalMaxY > mapHeight - 1 ? mapHeight - 1 : goalMaxY;
					
					//System.out.println("Generate between: " + goalMinX + " and " + goalMaxX);
					// ###############
					
					int goalX = goalMinX + generator.nextInt(goalMaxX - goalMinX + 1);
					int goalY = goalMinY + generator.nextInt(goalMaxY - goalMinY + 1);
					
					while (blockedTiles.contains(new Point(goalX, goalY))) {
						goalX = goalMinX + generator.nextInt(goalMaxX - goalMinX + 1);
						goalY = goalMinY + generator.nextInt(goalMaxY - goalMinY + 1);
					}
					
					// TODO: DEBUG
					long startTime = System.currentTimeMillis();
					
					ArrayList<Point> walkPath = AStarPathFinder.getShortestPath(startX, startY, goalX, goalY, blockedTiles);
					h.setWalkPath(walkPath);
					h.setState(Human.State.WALKING);
					
					// TODO: DEBUG
					long resultTime = System.currentTimeMillis() - startTime;
					
					if (resultTime > 50) {
						System.out.println("Time for A*: " + resultTime + ". Walkpath size: " + walkPath.size());
						//System.out.println();
					}
					
					/*if (walkPath.size() > 70) {
						System.out.println();
						System.out.println("######## FROM " + startX + ":" + startY + " TO " + goalX + ":" + goalY);
						
						for (Point p : walkPath) {
							System.out.println(p.getX() + ":" + p.getY());
						}
						
						System.out.println();
						System.out.println("########");
						System.out.println();
						
						//System.exit(0);
					}*/
				}
				
				if (h.getState() != Human.State.COLLIDING)
					h.setState(Human.State.WALKING);
				
				// WALK
				//h.walk();
			}
			
			//updateHumanHealth(h);
		}
	}
	
	public void updateHumansWalk() {
		for (Human h: humans) {
			if (h.getState() == Human.State.WALKING || h.getState() == Human.State.COLLIDING)
				h.walk();
		}
	}
	
	public void updateHuman2() {
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
				
				//h.getBody().setTransform(h.getBody().getPosition(), angle);		
				//h.getBody().setLinearVelocity(totX, totY);
				h.setState(Human.State.FLEEING);
			} else {
				// If the human just got away form a zombie, set its state to idle.
				if (h.getState() == Human.State.FLEEING)
					h.setState(Human.State.IDLE);
				
				if (h.getState() == Human.State.IDLE) {
					// ###############
					int startX = (int) h.getTileX();
					int startY = (int) h.getTileY();
					
					int goalMinX = startX - 20;
					goalMinX = goalMinX < 1 ? 1 : goalMinX;
					
					int goalMaxX = startX + 20;
					goalMaxX = goalMaxX > mapWidth - 1 ? mapWidth - 1 : goalMaxX;
					
					int goalMinY = startY - 20;
					goalMinY = goalMinY < 1 ? 1 : goalMinY;
					
					int goalMaxY = startY + 20;
					goalMaxY = goalMaxY > mapHeight - 1 ? mapHeight - 1 : goalMaxY;
					
					//System.out.println("Generate between: " + goalMinX + " and " + goalMaxX);
					// ###############
					
					int goalX = goalMinX + generator.nextInt(goalMaxX - goalMinX + 1);
					int goalY = goalMinY + generator.nextInt(goalMaxY - goalMinY + 1);
					
					while (blockedTiles.contains(new Point(goalX, goalY))) {
						goalX = goalMinX + generator.nextInt(goalMaxX - goalMinX + 1);
						goalY = goalMinY + generator.nextInt(goalMaxY - goalMinY + 1);
					}
					
					// TODO: DEBUG
					long startTime = System.currentTimeMillis();
					
					ArrayList<Point> walkPath = AStarPathFinder2.getShortestPath(startX, startY, goalX, goalY, blockedTilesBoolean);
					h.setWalkPath(walkPath);
					h.setState(Human.State.WALKING);
					
					// TODO: DEBUG
					long resultTime = System.currentTimeMillis() - startTime;
					
					if (resultTime > 50) {
						System.out.println("Time for A*: " + resultTime + ". Walkpath size: " + walkPath.size());
						//System.out.println();
					}
					
					/*if (walkPath.size() > 70) {
						System.out.println();
						System.out.println("######## FROM " + startX + ":" + startY + " TO " + goalX + ":" + goalY);
						
						for (Point p : walkPath) {
							System.out.println(p.getX() + ":" + p.getY());
						}
						
						System.out.println();
						System.out.println("########");
						System.out.println();
						
						//System.exit(0);
					}*/
				}
				
				// WALK
				h.walk();
			}
			
			//updateHumanHealth(h);
		}
	}
	
	private void updateHumanHealth(Human h) {
		UtilityTimer infectedHealthTimer = h.getInfectedHealthTimer();
		
		if(infectedHealthTimer != null && infectedHealthTimer.isDone()) {
			h.changeHealth(-Constants.INFECTED_DAMAGE);
			infectedHealthTimer.resetTimer();
			//System.out.println(h.getHealth());
		}
		
		if(h.getHealth() == 0) {
			zombieTurns.add(h);
		}
	}
	
	private void turnHumansToZombie() {
		for (Human h : zombieTurns) {
			float xPos = h.getBody().getPosition().x/Constants.WORLD_TO_BOX;
			float yPos = h.getBody().getPosition().y/Constants.WORLD_TO_BOX;
			removeHuman(h);
			addZombie(xPos, yPos);
		}
		
		zombieTurns.clear();
	}
	
	private void updateZombies() {
		for (Zombie z : zombies) {
			Creature closestTarget = getClosestTarget(z);
			
			// If any humans are nearby, chase
			if (closestTarget != null) {
				float tmpX = closestTarget.getBody().getPosition().x - z.getBody().getPosition().x;
				float tmpY = closestTarget.getBody().getPosition().y - z.getBody().getPosition().y;
				
				double size = Math.sqrt(tmpX*tmpX+tmpY*tmpY);
				
				tmpX /= size * 0.8;
				tmpY /= size * 0.8;
				
				float angle = (float) Math.atan(tmpY/tmpX);
				
				if (tmpX < 0)
					angle = angle - (float)Math.PI;
				
				z.getBody().setTransform(z.getBody().getPosition(), angle);	
				z.getBody().setLinearVelocity(tmpX, tmpY);
				z.setState(Zombie.State.CHASING);
			} else {
				if (z.getState() == Zombie.State.IDLE) {
					// ###############
					int startX = (int) z.getTileX();
					int startY = (int) z.getTileY();
					
					int goalMinX = startX - 40;
					goalMinX = goalMinX < 1 ? 1 : goalMinX;
					
					int goalMaxX = startX + 40;
					goalMaxX = goalMaxX > mapWidth - 1 ? mapWidth - 1 : goalMaxX;
					
					int goalMinY = startY - 40;
					goalMinY = goalMinY < 1 ? 1 : goalMinY;
					
					int goalMaxY = startY + 40;
					goalMaxY = goalMaxY > mapHeight - 1 ? mapHeight - 1 : goalMaxY;
					
					//System.out.println("Generate between: " + goalMinX + " and " + goalMaxX);
					// ###############
					
					int goalX = goalMinX + generator.nextInt(goalMaxX - goalMinX + 1);
					int goalY = goalMinY + generator.nextInt(goalMaxY - goalMinY + 1);
					
					while (blockedTiles.contains(new Point(goalX, goalY))) {
						goalX = goalMinX + generator.nextInt(goalMaxX - goalMinX + 1);
						goalY = goalMinY + generator.nextInt(goalMaxY - goalMinY + 1);
					}
					
					ArrayList<Point> walkPath = AStarPathFinder.getShortestPath(startX, startY, goalX, goalY, blockedTiles);
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
			if (size < 1.5 && size < distance) {
				closestTarget = h;
				distance = size;
			}
		}
		
		// Check if player is closer
		float tmpX = WorldModel.player.getBody().getPosition().x - z.getBody().getPosition().x;
		float tmpY = WorldModel.player.getBody().getPosition().y - z.getBody().getPosition().y;
		
		double size = Math.sqrt(tmpX * tmpX + tmpY * tmpY);
		
		// Range and closest
		if (size < 1.5 && size < distance) {
			closestTarget = WorldModel.player;
		}
		
		return closestTarget;
	}
}