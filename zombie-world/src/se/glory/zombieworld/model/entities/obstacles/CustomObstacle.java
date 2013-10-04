package se.glory.zombieworld.model.entities.obstacles;

import se.glory.zombieworld.model.WorldModel;
import se.glory.zombieworld.utilities.Constants;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;

public class CustomObstacle {
	Body body;
	Shape shape;
	
	private CustomObstacle(float x, float y) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(new Vector2(x * Constants.WORLD_TO_BOX, y * Constants.WORLD_TO_BOX));
		
		body = WorldModel.world.createBody(bodyDef);
	}
	
	public CustomObstacle(float x, float y, float width, float height) {
		this(x + width/2, y + height/2);
		
		shape = new PolygonShape();
		((PolygonShape) shape).setAsBox(width/2 * Constants.WORLD_TO_BOX, height/2 * Constants.WORLD_TO_BOX);
	
		finish();
	}
	
	public CustomObstacle(float x, float y, float radius) {
		this(x, y);
		
		shape = new CircleShape();
		((CircleShape) shape).setRadius(radius * Constants.WORLD_TO_BOX);
		
		finish();
	}
	
	private void finish() {
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 0.5f; 
		fixtureDef.friction = 0.4f;
		fixtureDef.restitution = 0.6f;
		
		body.createFixture(fixtureDef);
	}
}