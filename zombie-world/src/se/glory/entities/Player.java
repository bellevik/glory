package se.glory.entities;

import se.glory.entities.weapons.Bullet;
import se.glory.utilities.Animator;
import se.glory.utilities.Constants;
import se.glory.utilities.Identity;
import se.glory.utilities.WorldHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

public class Player implements Creature {
	
	private float x, y, width, height;
	
	private Body body;
	private BodyDef bodyDef;
	private Texture texture;
	
	private Body weaponBody;
	
	private RevoluteJoint joint;
	
	private Animation animation;

	public Player (float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(new Vector2(x * Constants.WORLD_TO_BOX, y * Constants.WORLD_TO_BOX));
		body = WorldHandler.world.createBody(bodyDef);

		PolygonShape playerShape = new PolygonShape();
		playerShape.setAsBox(width * Constants.WORLD_TO_BOX, height * Constants.WORLD_TO_BOX);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = playerShape;
		fixtureDef.density = 0.5f; 
		fixtureDef.friction = 0.4f;
		fixtureDef.restitution = 0.6f;

		body.createFixture(fixtureDef);
		texture = new Texture(Gdx.files.internal("img/player.gif"));

		Identity identity = new Identity();
		identity.setTexture(texture);
		identity.setWidth(width);
		identity.setHeight(height);
		identity.setType(Constants.MoveableBodyType.PLAYER);

		body.setUserData(identity);
		
		body.setFixedRotation(true);
		
		createWeaponBody();
		attachWeapon();
		
		//this.animation = new Animator("dudesheet.png", this.x, this.y, 0);
		this.animation = Animator.createAnimation("SpriteSheetMain.png", this.x, this.y, 7);
	}
	
	public void createWeaponBody() {
		//Creating the weapon
		BodyDef weapon = new BodyDef();
		weapon.type = BodyType.DynamicBody;
		weapon.position.set(x * Constants.WORLD_TO_BOX + width * Constants.WORLD_TO_BOX, y * Constants.WORLD_TO_BOX + height * Constants.WORLD_TO_BOX);
		weaponBody = WorldHandler.world.createBody(weapon);
		PolygonShape weaponShape = new PolygonShape();
		weaponShape.setAsBox(16 * Constants.WORLD_TO_BOX, 8 * Constants.WORLD_TO_BOX);
		
		FixtureDef weaponFixture = new FixtureDef();
		weaponFixture.shape = weaponShape;
		weaponFixture.density = 0.5f;
		weaponFixture.friction = 0.4f;
		weaponFixture.restitution = 0.6f;
		weaponBody.createFixture(weaponFixture);
		
		Identity weaponIdentity = new Identity();
		weaponIdentity.setTexture(null);
		weaponIdentity.setType(Constants.MoveableBodyType.WEAPON);
		weaponBody.setUserData(weaponIdentity);
	}
	
	public void attachWeapon() {
		//The joint between weapon and player
		RevoluteJointDef jointDef = new RevoluteJointDef();
        jointDef.bodyA = body;
        jointDef.bodyB = weaponBody;
        //16 here is equal to the weapons width!
        jointDef.localAnchorA.set(width * Constants.WORLD_TO_BOX, 0);
        jointDef.localAnchorB.set(-16 * Constants.WORLD_TO_BOX, 0);
        jointDef.enableLimit = true;

        joint = (RevoluteJoint) WorldHandler.world.createJoint(jointDef);
	}
	
	public void shoot() {
		float rot = (float) (weaponBody.getTransform().getRotation());
        float xAngle = MathUtils.cos(rot);
        float yAngle = MathUtils.sin(rot);
		
		new Bullet(weaponBody.getPosition().x + 14 * xAngle * Constants.WORLD_TO_BOX, weaponBody.getPosition().y + 14 * yAngle * Constants.WORLD_TO_BOX, xAngle, yAngle);
	}
	
	@Override
	public Body getBody() {
		return body;
	}
	
	public Animation getAnimation(){
		return this.animation;
	}
}