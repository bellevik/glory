package se.glory.zombieworld.model.entities;

import se.glory.zombieworld.model.WorldModel;
import se.glory.zombieworld.utilities.Constants;
import se.glory.zombieworld.utilities.Identity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class MoveableBody implements Creature {
	
	private Body body;
	private BodyDef bodyDef;
	private CircleShape circleShape;
	private PolygonShape squareShape;
	private float width, height;
	
	private int health;
	private int maxHealth;
	
	public MoveableBody(float x, float y, float width, float height, Texture texture, Constants.MoveableBodyShape shape, Constants.MoveableBodyType type) {
		this.width = width;
		this.height = height;
		
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(new Vector2(x * Constants.WORLD_TO_BOX, y * Constants.WORLD_TO_BOX));
		body = WorldModel.world.createBody(bodyDef);
		
		if (shape == Constants.MoveableBodyShape.CIRCLE) {
			circleShape = new CircleShape();
			circleShape.setRadius(width * Constants.WORLD_TO_BOX);
		} else if (shape == Constants.MoveableBodyShape.SQUARE) {
			squareShape = new PolygonShape();
			squareShape.setAsBox(width * Constants.WORLD_TO_BOX, height * Constants.WORLD_TO_BOX);
		}
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = (shape == Constants.MoveableBodyShape.CIRCLE) ? circleShape : squareShape;
		fixtureDef.density = 0.5f; 
		fixtureDef.friction = 0.4f;
		fixtureDef.restitution = 0.6f;
		
		body.createFixture(fixtureDef);
		
		Identity identity = new Identity();
		identity.setTexture(texture);
		identity.setWidth(width);
		identity.setHeight(height);
		identity.setType(type);
		identity.setObj(this);
		
		body.setUserData(identity);
		
		maxHealth = health = 100;
	}
	
	public int getHealth() {
		return health;
	}
	
	public void takeDamage(int damage) {
		health -= damage;
	}
	
	public int getMaxHealth() {
		return maxHealth;
	}

	@Override
	public Body getBody() {
		return body;
	}

	@Override
	public float getTileX() {
		return (getBody().getPosition().x * Constants.BOX_TO_WORLD - width)/32;
	}

	@Override
	public float getTileY() {
		return (getBody().getPosition().y * Constants.BOX_TO_WORLD - height)/32;
	}

	@Override
	public boolean isMoving() {
		return getBody().getLinearVelocity().len() != 0;
	}
}
