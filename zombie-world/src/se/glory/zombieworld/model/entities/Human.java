package se.glory.zombieworld.model.entities;

import java.util.ArrayList;
import java.util.Random;

import se.glory.zombieworld.utilities.Constants;
import se.glory.zombieworld.utilities.misc.Point;

import com.badlogic.gdx.math.Vector2;

// TODO: Refactor, much of the code is identical to zombies, make cleaner!

public class Human extends MoveableBody implements Creature {
	private State state = State.IDLE;
	private ArrayList<Point> walkPath = new ArrayList<Point>();
	
	private int logicCounter;
	
	private int walkDirectionCounter = 0;
	private Vector2 walkDirection = null;

	public Human(float x, float y) {
		super(x, y, 15, 15, null, Constants.MoveableBodyShape.CIRCLE , Constants.MoveableBodyType.HUMAN);
		logicCounter = new Random().nextInt(60);
	}
	
	public int getLogicCounter() {
		logicCounter--;
		return logicCounter;
	}
	
	public void resetLogicCounter() {
		logicCounter = 60;
	}
	
	public void setDumbWalk(Vector2 direction, int length) {
		walkDirection = direction;
		walkDirectionCounter = length;
	}
	
	public void setWalkPath(ArrayList<Point> walkPath) {
		this.walkPath = walkPath;
	}
	
	public void walk() {
		if (Constants.DUMB_AI_MODE) {
			walkDumb();
		} else {
			walkSmart();
		}
	}
	
	public void walkDumb() {
		if (state == State.DUMB_AI || state == State.COLLIDING) {
			getBody().setLinearVelocity(walkDirection.x, walkDirection.y);
			
			walkDirectionCounter--;
			if (walkDirectionCounter < 1)
				state = State.IDLE;
		}
	}
	
	public void walkSmart() {
		// Check if in colliding state, if so, walk according to the calculated direction
		if (state == State.COLLIDING) {
			float angle = (float) Math.atan(walkDirection.y/walkDirection.x);
			
			if (walkDirection.x < 0)
				angle = angle - (float) Math.PI;
			
			getBody().setLinearVelocity(walkDirection.x, walkDirection.y);
			getBody().setTransform(getBody().getPosition(), angle);
			
			walkDirectionCounter--;
			
			// If the number of updates since the collision are reached, 
			// set state to IDLE, i.e. get a new random spot to travel to.
			if (walkDirectionCounter < 1) {
				walkPath.clear();
				state = State.IDLE;
			}
			
			return;
		}
		
		// Do walk
		if (walkPath.size() > 0) {
			if (Math.abs(walkPath.get(0).getX() - getTileX()) < 0.1 && Math.abs(walkPath.get(0).getY() - getTileY()) < 0.1) {
				walkPath.remove(0);
			}
		}
		
		if (walkPath.size() > 0) {
			float tmpX = walkPath.get(0).getX() - getTileX();
			float tmpY = walkPath.get(0).getY() - getTileY();
			
			double size = Math.sqrt(tmpX * tmpX + tmpY * tmpY);
			tmpX /= size;
			tmpY /= size;
			
			float angle = (float) Math.atan(tmpY/tmpX);
			
			if (tmpX < 0)
				angle = angle - (float)Math.PI;
			
			getBody().setLinearVelocity(tmpX, tmpY);
			getBody().setTransform(getBody().getPosition(), angle);
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
		FLEEING, WALKING, IDLE, COLLIDING, DUMB_AI;
	}
}