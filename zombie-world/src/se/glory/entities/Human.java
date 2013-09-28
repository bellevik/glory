package se.glory.entities;

import java.util.ArrayList;

import se.glory.utilities.Constants;
import se.glory.utilities.Identity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class Human implements Creature{
	private Body body;
	private BodyDef bodyDef;
	private Texture texture;
	
	public Human(World world, int x, int y) {
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(new Vector2(x * Constants.WORLD_TO_BOX, y * Constants.WORLD_TO_BOX));
		body = world.createBody(bodyDef);
		
		CircleShape circleShape = new CircleShape();
		circleShape.setRadius(15 * Constants.WORLD_TO_BOX);
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circleShape;
		fixtureDef.density = 0.5f; 
		fixtureDef.friction = 0.4f;
		fixtureDef.restitution = 0.6f;
		
		texture = new Texture(Gdx.files.internal("img/mario.png"));
		
		body.createFixture(fixtureDef);
		
		Identity humanIdentity = new Identity();
		humanIdentity.setTexture(texture);
		humanIdentity.setWidth(15);
		humanIdentity.setHeight(15);
		humanIdentity.setType("Human");
		
		body.setUserData(humanIdentity);
	}
	
	public void draw (SpriteBatch batch) {
		batch.begin();
		batch.draw(texture, body.getPosition().x * Constants.BOX_TO_WORLD, body.getPosition().y * Constants.BOX_TO_WORLD);
		batch.end();
	}
	
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
	}

	@Override
	public Vector2 getPosition() {
		return body.getPosition();
	}
}
