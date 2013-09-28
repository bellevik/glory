package se.glory.entities.weapons;

import se.glory.utilities.Constants;
import se.glory.utilities.Identity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

/*
 * This class will represent a Bullet. The bullet will be used to kill
 * other entities in the world.
 */
public class Bullet {

	private float damage;
	private float range;
	private Texture[] textures;
	
	public Bullet (float x, float y, World world) {
		BodyDef bulletDef = new BodyDef();
		bulletDef.type = BodyType.DynamicBody;
		bulletDef.position.set(x, y);
		Body bulletBody = world.createBody(bulletDef);
		
		CircleShape bulletShape = new CircleShape();
		bulletShape.setRadius(6 * Constants.WORLD_TO_BOX);
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = bulletShape;
		fixtureDef.density = 0.5f;
		fixtureDef.friction = 0.4f;
		fixtureDef.restitution = 0.6f;
		
		bulletBody.createFixture(fixtureDef);
		Texture bulletTexture = new Texture(Gdx.files.internal("img/bullet.png"));
		
		bulletShape.dispose();
		
		Identity i = new Identity();
		i.setTexture(bulletTexture);
		i.setType("Bullet");
		i.setWidth(6);
		i.setHeight(6);
		bulletBody.setUserData(i);
		
		bulletBody.setLinearVelocity(2, 0);
	}

	public Bullet(String name, float damage, float range, World world) {
		this.damage = damage;
		this.range = range;

		textures = new Texture[8];
		for(int i = 0; i < textures.length; i++) {
			textures[i] = new Texture(Gdx.files.internal("data/weapons/" + name + "/" + "bullet" + i + ".png"));
		}
	}

	public float getDamage() {
		return damage;
	}

	public float getRange() {
		return range;
	}

	public Texture getTexture(int index) {
		return textures[index];
	}

	public void setDamage(float damage) {
		this.damage = damage;
	}

	public void setRange(float range) {
		this.range = range;
	}

	public void setTextures(Texture[] textures) {
		for(int i = 0; i < textures.length; i++) {
			this.textures[i] = textures[i];
		}
	}
}