package se.glory.entities;

import se.glory.utilities.Constants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Player implements Creature {
	
	private Body playerBody;
	private BodyDef playerDef;
	private Texture texture;

	public Player (World world, float x, float y, float width, float height) {
		playerDef = new BodyDef();
		playerDef.type = BodyType.DynamicBody;
		playerDef.position.set(new Vector2(x * Constants.WORLD_TO_BOX, y * Constants.WORLD_TO_BOX));
		playerBody = world.createBody(playerDef);
		
		PolygonShape playerShape = new PolygonShape();
		playerShape.setAsBox(width * Constants.WORLD_TO_BOX, height * Constants.WORLD_TO_BOX);
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = playerShape;
		fixtureDef.density = 0.5f; 
		fixtureDef.friction = 0.4f;
		fixtureDef.restitution = 0.6f;
		
		playerBody.createFixture(fixtureDef);
		texture = new Texture(Gdx.files.internal("img/player.gif"));
	}
	
	public void draw (SpriteBatch batch) {
		batch.begin();
		batch.draw(texture, playerBody.getPosition().x * Constants.BOX_TO_WORLD, playerBody.getPosition().y * Constants.BOX_TO_WORLD);
		batch.end();
	}
	
	public Body getPlayerBody() {
		return playerBody;
	}

	@Override
	public Vector2 getPosition() {
		return playerBody.getPosition();
	}
	
}
