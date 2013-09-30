package se.glory.entities;

import se.glory.utilities.Constants;
import se.glory.utilities.Identity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class Zombie implements Creature {
	private Body body;
	private BodyDef bodyDef;
	private Texture texture;
	
	public Zombie(World world, int x, int y) {
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
		
		texture = new Texture(Gdx.files.internal("img/Zombie.png"));
		
		body.createFixture(fixtureDef);
		
		Identity zombieIdentity = new Identity();
		zombieIdentity.setTexture(texture);
		zombieIdentity.setWidth(15);
		zombieIdentity.setHeight(15);
		zombieIdentity.setType("Zombie");
		
		body.setUserData(zombieIdentity);
	}

	@Override
	public Vector2 getPosition() {
		return body.getPosition();
	}
}
