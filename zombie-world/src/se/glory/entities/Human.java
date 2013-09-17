package se.glory.entities;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Human {
	private static final float WORLD_TO_BOX = 0.01f;
	
	private Body body;
	private BodyDef bodyDef;
	
	public Human(World world, int x, int y) {
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(new Vector2(x * WORLD_TO_BOX, y * WORLD_TO_BOX));
		body = world.createBody(bodyDef);
		
		CircleShape circleShape = new CircleShape();
		circleShape.setRadius(15 * WORLD_TO_BOX);
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circleShape;
		fixtureDef.density = 0.5f;
		fixtureDef.friction = 0.4f;
		fixtureDef.restitution = 0.6f;
		
		body.createFixture(fixtureDef);
		body.setUserData(this);
	}
	
	public Vector2 getPos() {
		return body.getPosition();
	}
	
	public void autoUpdateMovement(ArrayList<Zombie> zombies) {
		float totX = 0;
		float totY = 0;
		
		for (Zombie z : zombies) {
			float tmpX = body.getPosition().x - z.getPos().x;
			float tmpY = body.getPosition().y - z.getPos().y;
			
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
			totX = (float) (totX/size);
			totY = (float) (totY/size);
			
			body.setLinearVelocity(totX, totY);
		}
	}
}
