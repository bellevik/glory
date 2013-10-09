package se.glory.zombieworld.model.entities;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

import se.glory.zombieworld.utilities.Constants;
import se.glory.zombieworld.utilities.Point;
import se.glory.zombieworld.utilities.TextureHandler;

// TODO: Refactor, much of the code is identical to zombies, make cleaner!

public class Human extends MoveableBody implements Creature {
	private State state = State.IDLE;
	private ArrayList<Point> walkPath = new ArrayList<Point>();
	
	private int collidingNumber = 0;
	private Vector2 collidingDirection = new Vector2(1,1);

	public Human(float x, float y) {
		super(x, y, 15, 15, TextureHandler.humanTexture, Constants.MoveableBodyShape.CIRCLE , Constants.MoveableBodyType.HUMAN);
	}
	
	public void setCollidingInfo(Vector2 direction, int collidingNumber) {
		this.collidingDirection = direction;
		this.collidingNumber = collidingNumber;
	}
	
	public void setWalkPath(ArrayList<Point> walkPath) {
		this.walkPath = walkPath;
	}
	
	public void walk() {
		// Check if in colliding state, if so, walk according to the calculated direction
		if (state == State.COLLIDING) {
			float angle = (float) Math.atan(collidingDirection.y/collidingDirection.x);
			
			if (collidingDirection.x < 0)
				angle = angle - (float) Math.PI;
			
			getBody().setLinearVelocity(collidingDirection.x, collidingDirection.y);
			getBody().setTransform(getBody().getPosition(), angle);
			
			collidingNumber--;
			
			// If the number of updates since the collision are reached, 
			// set state to IDLE, i.e. get a new random spot to travel to.
			if (collidingNumber < 1) {
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
		FLEEING, WALKING, IDLE, COLLIDING;
	}
}