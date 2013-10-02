package se.glory.zombieworld.model.entities;

import java.awt.Point;
import java.util.ArrayList;

import se.glory.zombieworld.utilities.Constants;
import se.glory.zombieworld.utilities.TextureHandler;

public class Zombie extends MoveableBody implements Creature {
	private State state = State.IDLE;
	private ArrayList<Point> walkPath = new ArrayList<Point>();
	
	public Zombie(float x, float y) {
		super(x, y, 15, 15, TextureHandler.zombieTexture, Constants.MoveableBodyShape.CIRCLE , Constants.MoveableBodyType.ZOMBIE);
	}
	
	public void setWalkPath(ArrayList<Point> walkPath) {
		this.walkPath = walkPath;
	}
	
	public void walk() {
		if (walkPath.size() > 0) {
			if (Math.abs(walkPath.get(0).x - getTileX()) < 0.052 && Math.abs(walkPath.get(0).y - getTileY()) < 0.052) {
				walkPath.remove(0);
			}
		}
		
		if (walkPath.size() > 0) {
			float tmpX = walkPath.get(0).x - getTileX();
			float tmpY = walkPath.get(0).y - getTileY();
			
			double size = Math.sqrt(tmpX * tmpX + tmpY * tmpY);
			tmpX /= size * 1.2;
			tmpY /= size * 1.2;
			
			getBody().setLinearVelocity(tmpX, tmpY);
		} else {
			setState(State.IDLE);
		}
	}
	
	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}
	
	public enum State {
		CHASING, WALKING, IDLE;
	}
}