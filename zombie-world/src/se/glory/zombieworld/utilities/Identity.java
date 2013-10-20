package se.glory.zombieworld.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/*
 * This class will be the UserData object of every Box2d Body that will persist in 
 * our Box2d World.
 */
public class Identity {
	private Texture texture = null;
	private boolean isDead = false, isOpen = false;
	private Constants.MoveableBodyType type;
	private float width, height;
	private Object obj = null;
	
	public Texture getTexture() {
		return texture;
	}
	public void setTexture(Texture texture) {
		this.texture = texture;
	}
	public boolean isOpen() {
		return isOpen;
	}
	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
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
	public Object getObj() {
		return obj;
	}
	public void setObj(Object obj) {
		this.obj = obj;
	}
}
