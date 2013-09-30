package se.glory.entities;

import se.glory.utilities.Constants;
import se.glory.utilities.Identity;
import se.glory.utilities.WorldHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class Human implements Creature {
	private Body body;
	private BodyDef bodyDef;
	private Texture texture;
	
	public Human(int x, int y) {
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(new Vector2(x * Constants.WORLD_TO_BOX, y * Constants.WORLD_TO_BOX));
		body = WorldHandler.world.createBody(bodyDef);
		
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

	@Override
	public Vector2 getPosition() {
		return body.getPosition();
	}
}
