package se.glory.entities;

import se.glory.utilities.Constants;
import se.glory.utilities.Identity;
import se.glory.utilities.WorldHandler;
import se.glory.utilities.Constants.MoveableBodyShape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class MoveableBody implements Creature {
	
	private Body body;
	private BodyDef bodyDef;
	private CircleShape circleShape;
	private PolygonShape squareShape;
	
	public MoveableBody(float x, float y, float width, float height, Texture texture, Constants.MoveableBodyShape shape, Constants.MoveableBodyType type) {
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(new Vector2(x * Constants.WORLD_TO_BOX, y * Constants.WORLD_TO_BOX));
		body = WorldHandler.world.createBody(bodyDef);
		
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
		
		body.setUserData(identity);
	}

	@Override
	public Body getBody() {
		return body;
	}
}
