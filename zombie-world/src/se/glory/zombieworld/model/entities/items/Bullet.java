package se.glory.zombieworld.model.entities.items;

import java.util.Timer;
import java.util.TimerTask;

import se.glory.zombieworld.model.WorldModel;
import se.glory.zombieworld.model.entities.Identity;
import se.glory.zombieworld.utilities.Constants;
import se.glory.zombieworld.view.TextureHandler;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

/*
 * This class will represent a Bullet. The bullet will be used to kill
 * other entities in the world.
 */
public class Bullet {

	private float damage;
	private float range;
	private Texture[] textures;
	private Timer t;
	private TimerTask tt;
	
	public Bullet (float x, float y, float xAngle, float yAngle, float damage, float range) {
		this.damage = damage;
		this.range = range;
		
		BodyDef bulletDef = new BodyDef();
		bulletDef.type = BodyType.DynamicBody;
		bulletDef.position.set(x, y);
		final Body bulletBody = WorldModel.world.createBody(bulletDef);
		
		CircleShape bulletShape = new CircleShape();
		bulletShape.setRadius(6 * Constants.WORLD_TO_BOX);
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = bulletShape;
		fixtureDef.density = 0.5f;
		fixtureDef.friction = 0.4f;
		fixtureDef.restitution = 0.6f;
		
		bulletBody.createFixture(fixtureDef);
		Texture bulletTexture = TextureHandler.bulletTexture;
		
		bulletShape.dispose();
		
		Identity i = new Identity();
		i.setTexture(bulletTexture);
		i.setType(Constants.MoveableBodyType.BULLET);
		i.setWidth(6);
		i.setHeight(6);
		i.setObj(this);
		bulletBody.setUserData(i);
		
		bulletBody.setLinearVelocity(8 * xAngle, 8 * yAngle);

		t = new Timer();
		tt = new TimerTask() {
		    @Override
		    public void run() {
		    	if (this != null && bulletBody.getUserData() != null) {
		    		Identity tmp = (Identity) bulletBody.getUserData();
			    	tmp.setDead(true);
		    	}
		    };
		};
		
		t.schedule(tt, (long)(range*1000));
		
	}
	
	public void destroyTimer() {
		tt.cancel();
		t.cancel();
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