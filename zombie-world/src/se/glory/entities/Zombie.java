package se.glory.entities;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Zombie {
	private static final float WORLD_TO_BOX = 0.01f;
	
	private Body body;
	private BodyDef bodyDef;
	
	public Zombie(World world, int x, int y) {
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
	
	private Human getClosestHuman(ArrayList<Human> humans) {
		double distance = Double.MAX_VALUE;
		Human closestHuman = null;
		
		for (Human h : humans) {
			float tmpX = h.getPos().x - body.getPosition().x;
			float tmpY = h.getPos().y - body.getPosition().y;
			
			double size = Math.sqrt(tmpX*tmpX+tmpY*tmpY);
			
			if (size < 1.5 && size < distance) {
				closestHuman = h;
				distance = size;
			}
		}
		
		return closestHuman;
	}
	
	public void autoUpdateMovement(ArrayList<Human> humans) {
		Human h = getClosestHuman(humans);
		
		if (h != null) {
			float tmpX = h.getPos().x - body.getPosition().x;
			float tmpY = h.getPos().y - body.getPosition().y;
			
			double size = Math.sqrt(tmpX*tmpX+tmpY*tmpY);
			tmpX = (float) (tmpX/(size*0.9));
			tmpY = (float) (tmpY/(size*0.9));
			
			body.setLinearVelocity(tmpX, tmpY);
		}
	}
}
