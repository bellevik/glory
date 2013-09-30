package se.glory.utilities;

import com.badlogic.gdx.graphics.Texture;

/*
 * This class will be the UserData object of every Box2d Body that will persist in 
 * our Box2d World.
 */
public class Identity {
	private Texture texture;
	private boolean isDead = false;
	private Constants.MoveableBodyType type;
	private float width, height;
	
	public Texture getTexture() {
		return texture;
	}
	public void setTexture(Texture texture) {
		this.texture = texture;
	}
	public boolean isDead() {
		return isDead;
	}
	public void setDead(boolean isDead) {
		this.isDead = isDead;
	}
	public Constants.MoveableBodyType getType() {
		return type;
	}
	public void setType(Constants.MoveableBodyType type) {
		this.type = type;
	}
	public float getWidth() {
		return width;
	}
	public void setWidth(float width) {
		this.width = width;
	}
	public float getHeight() {
		return height;
	}
	public void setHeight(float height) {
		this.height = height;
	}
}
