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
		fixtureDef.density = 10f;
		fixtureDef.friction = 10f;
		fixtureDef.restitution = .5f;
		
		texture = new Texture(Gdx.files.internal("img/Zombie.png"));
		
		body.createFixture(fixtureDef);
		
		Identity zombieIdentity = new Identity();
		zombieIdentity.setTexture(texture);
		zombieIdentity.setWidth(15);
		zombieIdentity.setHeight(15);
		zombieIdentity.setType("Zombie");
		
		body.setUserData(zombieIdentity);
	}
	
	private Creature getClosestHuman(ArrayList<Creature> humans) {
		double distance = Double.MAX_VALUE;
		Creature closestHuman = null;
		
		for (Creature h : humans) {
			float tmpX = h.getPosition().x - body.getPosition().x;
			float tmpY = h.getPosition().y - body.getPosition().y;
			
			double size = Math.sqrt(tmpX*tmpX+tmpY*tmpY);
			
			if (size < 3.5 && size < distance) {
				closestHuman = h;
				distance = size;
			}
		}
		
		return closestHuman;
	}
	
	public void draw (SpriteBatch batch) {
		batch.begin();
		batch.draw(texture, body.getPosition().x * Constants.BOX_TO_WORLD, body.getPosition().y * Constants.BOX_TO_WORLD);
		batch.end();
	}
	
	public void autoUpdateMovement(ArrayList<Creature> creatures2, Player player) {
		ArrayList<Creature> creatures = (ArrayList<Creature>) creatures2.clone();
		creatures.add((Creature) player);
		
		Creature h = getClosestHuman(creatures);
		
		if (h != null) {
			float tmpX = h.getPosition().x - body.getPosition().x;
			float tmpY = h.getPosition().y - body.getPosition().y;
			
			double size = Math.sqrt(tmpX*tmpX+tmpY*tmpY);
			tmpX = (float) (tmpX/(size*2));
			tmpY = (float) (tmpY/(size*2));
			
			body.setLinearVelocity(tmpX, tmpY);
		} else {
			body.setLinearVelocity(0, 0);
		}
	}

	@Override
	public Vector2 getPosition() {
		return body.getPosition();
	}
}
