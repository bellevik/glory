package se.glory.zombieworld.model.entities;

import se.glory.zombieworld.model.WorldModel;
import se.glory.zombieworld.model.entities.items.Item;
import se.glory.zombieworld.model.entities.weapons.Bullet;
import se.glory.zombieworld.utilities.Animator;
import se.glory.zombieworld.utilities.Constants;
import se.glory.zombieworld.utilities.Identity;
import se.glory.zombieworld.utilities.UtilityTimer;

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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

/*
 * This class will represent the moveable Player in the world.
 * Our player will be moveable by the two Joysticks on the screen.
 * Rotation will be applied to the player through the joysticks aswell.
 * The player will be a Box2D body and added to the Box2d world for simulation with
 * other bodies. The player will be moved and rotated from the GameScreen class.
 */
public class Player implements Creature {
	
	private float x, y, width, height;
	
	private Body body;
	private BodyDef bodyDef;
	private Texture texture;
	
	private Body weaponBody;
	
	//This array will contain the items in the Quick-Swap. Usually Weapons or Potions.
	private Array<Item> quickSwapList = new Array<Item>();
	
	private RevoluteJoint joint;
	private Animation animation;
	
	// TODO Set the variable depending on the weapons arsenal class. What weapon is equipped
	//These variables will handle the shooting method
	private boolean readyToFire = true;
	private float reloadTime = 1;
	
	private int health;
	private int maxHealth;
	private UtilityTimer infectedHealth = null;

	public Player (float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(new Vector2(x * Constants.WORLD_TO_BOX, y * Constants.WORLD_TO_BOX));
		body = WorldModel.world.createBody(bodyDef);

		PolygonShape playerShape = new PolygonShape();
		playerShape.setAsBox(width * Constants.WORLD_TO_BOX, height * Constants.WORLD_TO_BOX);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = playerShape;
		fixtureDef.density = 0.5f; 
		fixtureDef.friction = 0.4f;
		fixtureDef.restitution = 0.6f;

		body.createFixture(fixtureDef);
		texture = new Texture(Gdx.files.internal("img/player.png"));

		Identity identity = new Identity();
		identity.setTexture(texture);
		identity.setWidth(width);
		identity.setHeight(height);
		identity.setType(Constants.MoveableBodyType.PLAYER);
		//setObj needed for drawing the animation of the player in GameView
		identity.setObj(this);

		body.setUserData(identity);
		
		body.setFixedRotation(true);
		
		createWeaponBody();
		attachWeapon();
		
		maxHealth = health = 100;
	}
	
	/*
	 * This method will create a weapon as a Box2d body.
	 */
	public void createWeaponBody() {
		//Creating the weapon
		BodyDef weapon = new BodyDef();
		weapon.type = BodyType.DynamicBody;
		weapon.position.set(x * Constants.WORLD_TO_BOX + width * Constants.WORLD_TO_BOX, y * Constants.WORLD_TO_BOX + height * Constants.WORLD_TO_BOX);
		weaponBody = WorldModel.world.createBody(weapon);
		PolygonShape weaponShape = new PolygonShape();
		weaponShape.setAsBox(8 * Constants.WORLD_TO_BOX, 4 * Constants.WORLD_TO_BOX);
		
		FixtureDef weaponFixture = new FixtureDef();
		weaponFixture.shape = weaponShape;
		weaponFixture.density = 0.5f;
		weaponFixture.friction = 0.4f;
		weaponFixture.restitution = 0.6f;
		weaponBody.createFixture(weaponFixture);
		
		Identity weaponIdentity = new Identity();
		weaponIdentity.setTexture(null);
		//Rethink if the weapons type should be player or weapon. 
		//This will cause problem in the loot pickup
		weaponIdentity.setType(Constants.MoveableBodyType.PLAYER);
		weaponBody.setUserData(weaponIdentity);
	}
	
	/*
	 * This method will attach the weapon body to the player body.
	 * It will use a Joint. Joints are given to us by the library.
	 */
	public void attachWeapon() {
		//The joint between weapon and player
		RevoluteJointDef jointDef = new RevoluteJointDef();
        jointDef.bodyA = body;
        jointDef.bodyB = weaponBody;
        //16 here is equal to the weapons width!
        jointDef.localAnchorA.set(width * Constants.WORLD_TO_BOX, 0);
        jointDef.localAnchorB.set(-8 * Constants.WORLD_TO_BOX, 0);
        //This row will enable a limit for the weapon. I.e the weapon is fixed to the PlayerBody.
        jointDef.enableLimit = true;

        joint = (RevoluteJoint) WorldModel.world.createJoint(jointDef);
	}
	
	/*
	 * This method will add an Item to the item list. If it fails it
	 * will return false. If it succeeds it will return true. The size of this
	 * inventory will be set to 5 slots
	 */
	public boolean addItemToQuickSwap(Item item) {
		updateQuickSelectionImages();
		if(quickSwapList.size < 5) {
			quickSwapList.add(item);
			return true;
		}
		return false;
	}
	
	/*
	 * This method will change the images shown on the UI. This will be done every time
	 * the user picks up new loot (retreives new items). 
	 */
	public void updateQuickSelectionImages () {
		//Reach the quickselection from a new StageHandler
	}
	
	/*
	 * This method is called every render update from the quickSelection class.
	 * The int pos is the position of the quickSelection UI that is selected.
	 * Every time this method gets called we will check if its a new position and if so
	 * we will change the item of the player.
	 */
	public void changeEquippedItem (int pos) {
		
	}
	
	/*
	 * This method will fire a shot everytime the boolean readyToFire is set to true. Right after
	 * the player fired a bullet the boolean will be set to false for a specific amount of time.
	 * This time will be different depending on what weapon is equipped. The reloadTime is the
	 * short amount of time when the player is unable to fire a shot. 
	 */
	public void shoot() {
		if (readyToFire) {
			fireBullet();
			readyToFire = false;
			//It will take realoadTime seconds to set the boolean to true again
			Timer.schedule(new Task(){
			    @Override
			    public void run() {
			    	readyToFire = true;
			    }
			}, reloadTime);
		}	
	}
	
	/*
	 * First we calculate the angle. Then we create a bullet with a constant velocity
	 * to be fired at the angle we calculated
	 */
	public void fireBullet() {
		float rot = (float) (weaponBody.getTransform().getRotation());
        float xAngle = MathUtils.cos(rot);
        float yAngle = MathUtils.sin(rot);
		
        //14 here is to create the bullet a fix distance from the weapon
		new Bullet(weaponBody.getPosition().x + 14 * xAngle * Constants.WORLD_TO_BOX, weaponBody.getPosition().y + 14 * yAngle * Constants.WORLD_TO_BOX, xAngle, yAngle);
	}
	
	/*
	 * This method will choose weather to rotate the player according to the Fire-Joystick
	 * or the Movement-Joystick. The three cases in order are: if only the movement joystick
	 * is touched. Secondly if both joysticks are touched (use the direction of the Fire-Joystick).
	 * Thirdly if only the Fire-Joystick is touched.
	 */
	public void applyRotationToPlayer(float moveKnobX, float moveKnobY, float fireKnobX, float fireKnobY) {
		if (moveKnobX != 0 && moveKnobY != 0 && fireKnobX == 0 && fireKnobY == 0) {
			rotatePlayer(moveKnobX, moveKnobY);
		} else if (moveKnobX != 0 && moveKnobY != 0 && fireKnobX != 0 && fireKnobY != 0) {
			rotatePlayer(fireKnobX, fireKnobY);
		} else if (moveKnobX == 0 && moveKnobY == 0 && fireKnobX != 0 && fireKnobY != 0) {
			rotatePlayer(fireKnobX, fireKnobY);
		}
	}
	
	/*
	 * This method will actually rotate the player according to the angle received
	 * from the joysticks. The float knobDegree will be the angle of the Joystick.
	 * The player body AND the joint (the weapon) will be rotated to the same angle
	 * as the joystick.
	 */
	public void rotatePlayer (float knobX, float knobY) {
		float knobDegree;
		
		if (knobY >= 0) {
			knobDegree = (int) (Math.acos(knobX) * MathUtils.radiansToDegrees);
		} else {
			knobDegree = -(int) (Math.acos(knobX) * MathUtils.radiansToDegrees);
		}
		
		WorldModel.player.getBody().setTransform(WorldModel.player.getBody().getPosition(), knobDegree * MathUtils.degreesToRadians);
		WorldModel.player.getBody().getJointList().get(0).joint.getBodyB().setTransform(WorldModel.player.getBody().getJointList().get(0).joint.getBodyB().getPosition(), knobDegree * MathUtils.degreesToRadians);
		WorldModel.player.getBody().getJointList().get(0).joint.getBodyB().setAwake(true);
	}
	
	public int getHealth() {
		return health;
	}
	
	public int getHealthPercentage() {
		return ((health*100)/maxHealth);
	}
	
	public void changeHealth(int healthChange) {
		
		//Remove infected status if getting healed
		if(healthChange >= 0) {
			infectedHealth = null;
		}
		
		//Add or remove health depending on the change
		health += healthChange;
		
		//Set health to 0 if it is less than 0 and to maxHealth if going above it
		if(health<0) {
			health = 0;
		}else if(health > maxHealth) {
			health = maxHealth;
		}
	}
	
	public int getMaxHealth() {
		return maxHealth;
	}
	
	public UtilityTimer getInfectedHealthTimer() {
		return infectedHealth;
	}
	
	public void infect() {
		infectedHealth = new UtilityTimer(Constants.INFECTED_INTERVAL);
	}
	
	public void kill() {
	//	(Identity)player.getBody().getUserData();
		((Identity)this.getBody().getUserData()).setDead(true);
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
	public Body getBody() {
		return body;
	}
	
	public Animation getAnimation(){
		return this.animation;
	}
	
	@Override
	public boolean isMoving() {
		return getBody().getLinearVelocity().len() != 0;
	}
}