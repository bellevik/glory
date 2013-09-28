package se.glory.entities;

import se.glory.utilities.Constants;
import se.glory.utilities.Identity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Player implements Creature {
	
	private Body body;
	private BodyDef bodyDef;
	private Texture texture;

	public Player (World world, float x, float y, float width, float height) {
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(new Vector2(x * Constants.WORLD_TO_BOX, y * Constants.WORLD_TO_BOX));
		body = world.createBody(bodyDef);
		
		PolygonShape playerShape = new PolygonShape();
		playerShape.setAsBox(width * Constants.WORLD_TO_BOX, height * Constants.WORLD_TO_BOX);
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = playerShape;
		fixtureDef.density = 0.5f; 
		fixtureDef.friction = 0.4f;
		fixtureDef.restitution = 0.6f;
		
		body.createFixture(fixtureDef);
		texture = new Texture(Gdx.files.internal("img/player.gif"));
		
		Identity playerIdentity = new Identity();
		playerIdentity.setTexture(texture);
		playerIdentity.setWidth(width);
		playerIdentity.setHeight(height);
		playerIdentity.setType("Player");
		
		body.setUserData(playerIdentity);
	}
	
	public Body getPlayerBody() {
		return body;
	}

	@Override
	public Vector2 getPosition() {
		return body.getPosition();
	}
	
}
