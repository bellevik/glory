package se.glory.zombieworld.model.entities;

import com.badlogic.gdx.physics.box2d.Body;

public interface Creature {
	public Body getBody();
	public float getTileX();
	public float getTileY();
	public boolean isMoving();
}
