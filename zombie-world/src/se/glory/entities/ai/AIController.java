package se.glory.entities.ai;

import java.util.ArrayList;

import se.glory.entities.Human;
import se.glory.entities.Zombie;

import com.badlogic.gdx.physics.box2d.World;

public class AIController {
	private ArrayList<Human> humans = new ArrayList<Human>();
	private ArrayList<Zombie> zombies = new ArrayList<Zombie>();
	
	private World world;
	
	public void addHuman(int x, int y) {
		humans.add(new Human(x, y));
	}
	
	public void addZombie(int x, int y) {
		zombies.add(new Zombie(x, y));
	}
	
	public void update() {
		/*
			public void autoUpdateMovement(ArrayList<Zombie> zombies) {
			float totX = 0;
			float totY = 0;
			
			for (Zombie z : zombies) {
				float tmpX = body.getPosition().x - z.getPosition().x;
				float tmpY = body.getPosition().y - z.getPosition().y;
				
				double size = Math.sqrt(tmpX*tmpX+tmpY*tmpY);
				
				if (size < 1.5) {
					double test = 1.5 - size;
					
					tmpX = (float) (tmpX/(size/test));
					tmpY = (float) (tmpY/(size/test));
					
					totX += tmpX;
					totY += tmpY;
				}
			}
			
			double size = Math.sqrt(totX*totX+totY*totY);
			
			if (size > 0) {
				totX = (float) (totX/(size*2));
				totY = (float) (totY/(size*2));
				
				body.setLinearVelocity(totX, totY);
			} else {
				body.setLinearVelocity(0, 0);
			}
		*/
		
		/*
			private Creature getClosestHuman(ArrayList<Creature> humans) {
				double distance = Double.MAX_VALUE;
				Creature closestHuman = null;
				
				for (Creature h : humans) {
					float tmpX = h.getPosition().x - body.getPosition().x;
					float tmpY = h.getPosition().y - body.getPosition().y;
					
					double size = Math.sqrt(tmpX*tmpX+tmpY*tmpY);
					
					if (size < 3.5 && size < distance) {
						closestHuman = h;
						distance = size;
					}
				}
				
				return closestHuman;
			}
			
			public void autoUpdateMovement(ArrayList<Creature> creatures2, Player player) {
				ArrayList<Creature> creatures = (ArrayList<Creature>) creatures2.clone();
				creatures.add((Creature) player);
				
				Creature h = getClosestHuman(creatures);
				
				if (h != null) {
					float tmpX = h.getPosition().x - body.getPosition().x;
					float tmpY = h.getPosition().y - body.getPosition().y;
					
					double size = Math.sqrt(tmpX*tmpX+tmpY*tmpY);
					tmpX = (float) (tmpX/(size*2));
					tmpY = (float) (tmpY/(size*2));
					
					body.setLinearVelocity(tmpX, tmpY);
				} else {
					body.setLinearVelocity(0, 0);
				}
			}
		*/
	}
}
