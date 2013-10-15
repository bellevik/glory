package se.glory.zombieworld.model.entities;

import se.glory.zombieworld.model.StageModel;
import se.glory.zombieworld.model.WorldModel;
import se.glory.zombieworld.model.entities.items.Item;
import se.glory.zombieworld.model.entities.weapons.Bullet;
import se.glory.zombieworld.model.entities.weapons.EquipedItem;
import se.glory.zombieworld.model.entities.weapons.ERangedWeapon;
import se.glory.zombieworld.utilities.Constants;
import se.glory.zombieworld.utilities.Identity;
import se.glory.zombieworld.utilities.UtilityTimer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
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
	private float width, height;

	private Body body;
	private BodyDef bodyDef;
	
	//This array will contain the items in the Quick-Swap. Usually Weapons or Potions.
	private Array<Item> quickSwapList = new Array<Item>();
	
	private Animation animation;
	
	private boolean isIndoors;
	
	// TODO Set the variable depending on the weapons arsenal class. What weapon is equipped
	//These variables will handle the shooting method
	private boolean readyToFire = true;
	
	private float health;
	private float maxHealth;
	private UtilityTimer infectedHealth = null;
	private ERangedWeapon equippedWeapon = null;

	public static boolean emptyClip;
	
	public Player (float x, float y, float width, float height) {
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

		Identity identity = new Identity();
		identity.setWidth(width);
		identity.setHeight(height);
		identity.setType(Constants.MoveableBodyType.PLAYER);
		
		//setObj needed for drawing the animation of the player in GameView
		identity.setObj(this);

		body.setUserData(identity);
		
		body.setFixedRotation(true);
		
		maxHealth = health = 100;
		quickSwapList.size = 5;
	}
	
	/*
	 * This method will add an Item to the item list. If it fails it
	 * will return false. If it succeeds it will return true. The size of this
	 * inventory will be set to 5 slots
	 */
	public boolean addItemToQuickSwap(Item item) {
		//Loops through the array and checks if its room for an item. If its room it adds tot he array.
		//otherwise return false
		for (int i = 0; i < quickSwapList.size; i++) {
			//If the position is empty and the item doesn't exists in the quickswaplist, add a new item to the list
			if (quickSwapList.get(i) == null && !existsInSwapList(item)) {
				quickSwapList.set(i, item);
				updateQuickSelectionImages();
				return true;
			} else if (quickSwapList.get(i) != null) {
				if (((ERangedWeapon)quickSwapList.get(i)).getName().equals(((ERangedWeapon)item).getName())) {
					((ERangedWeapon)quickSwapList.get(i)).addClip(2);
					emptyClip = false;
				}
			}
		}
		return false;
	}
	
	/*
	 * This method checks if the item already exists in the quickswaplist.
	 */
	public boolean existsInSwapList (Item item) {
		for (int i = 0; i < quickSwapList.size; i++) {
			if (quickSwapList.get(i) != null) {
				if ( ((ERangedWeapon)(item)).getName().equals(((ERangedWeapon)(quickSwapList.get(i))).getName()) ) {
					return true;
				}
			}
		}
		return false;
	}
	
	/*
	 * This method will change the images shown on the UI. This will be done every time
	 * the user picks up new loot (retrieves new items). 
	 */
	public void updateQuickSelectionImages () {
		for (int i = 0; i < quickSwapList.size; i++) {
			if (quickSwapList.get(i) != null) {
				StageModel.quickSelection.changeItem(i, (ERangedWeapon)(quickSwapList.get(i)));
				/*
				Texture tmp = ((ERangedWeapon)(quickSwapList.get(i))).getTexture();
				StageModel.quickSelection.changeItem(i, new Image(tmp));
				*/
			}
		}
	}
	
	/*
	 * This method is called every render update from the quickSelection class.
	 * The int pos is the position of the quickSelection UI that is selected.
	 * Every time this method gets called we will check if its a new position and if so
	 * we will change the item of the player.
	 */
	public void changeEquippedItem (int pos) {
		if(quickSwapList.get(pos) != null && equippedWeapon != (ERangedWeapon)(quickSwapList.get(pos))){
			equippedWeapon = (ERangedWeapon)(quickSwapList.get(pos));
			if (((ERangedWeapon)(quickSwapList.get(pos))).getClips() != 0) {
				emptyClip = false;
			}
		}
		// TODO Maybe add functionality to unequip weapon?
	}
	
	/*
	 * This method will fire a shot everytime the boolean readyToFire is set to true. Right after
	 * the player fired a bullet the boolean will be set to false for a specific amount of time.
	 * This time will be different depending on what weapon is equipped. The reloadTime is the
	 * short amount of time when the player is unable to fire a shot. 
	 */
	public void shoot() {
		if (readyToFire && equippedWeapon != null && !emptyClip) {
			fireBullet();
			equippedWeapon.removeBulletFromClip();
			readyToFire = false;
			//It will take realoadTime seconds to set the boolean to true again
			Timer.schedule(new Task(){
			    @Override
			    public void run() {
			    	readyToFire = true;
			    }
			}, equippedWeapon.getFireRate());
		}	
	}
	
	/*
	 * First we calculate the angle. Then we create a bullet with a constant velocity
	 * to be fired at the angle we calculated
	 */
	public void fireBullet() {
		float rot = (float) (body.getTransform().getRotation());
        float xAngle = MathUtils.cos(rot);
        float yAngle = MathUtils.sin(rot);
		
        //14 here is to create the bullet a fix distance from the weapon
		new Bullet(body.getPosition().x + 14 * xAngle * Constants.WORLD_TO_BOX, body.getPosition().y + 14 * yAngle * Constants.WORLD_TO_BOX, xAngle, yAngle, equippedWeapon.getDamage(), equippedWeapon.getRange());

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
	}
	
	public float getHealth() {
		return health;
	}
	
	public int getHealthPercentage() {
		return (int)((health*100)/maxHealth);
	}
	
	public void changeHealth(float healthChange) {	
		//Remove infected status if getting healed
		if(healthChange >= 0 && infectedHealth != null) {
			StageModel.healthBar.setInfectedState(false);
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
	
	public float getMaxHealth() {
		return maxHealth;
	}
	
	public UtilityTimer getInfectedHealthTimer() {
		return infectedHealth;
	}
	
	public void infect() {
		infectedHealth = new UtilityTimer(Constants.INFECTED_INTERVAL);
		StageModel.healthBar.setInfectedState(true);
	}
	
	public void kill() {
	//	(Identity)player.getBody().getUserData();
		((Identity)this.getBody().getUserData()).setDead(true);
	}
	
	@Override
	public float getTileX() {
		return (getBody().getPosition().x * Constants.BOX_TO_WORLD - width)/16;
	}

	@Override
	public float getTileY() {
		return (getBody().getPosition().y * Constants.BOX_TO_WORLD - height)/16;
	}
	
	@Override
	public Body getBody() {
		return body;
	}
	
	public Animation getAnimation() {
		return this.animation;
	}
	
	public void setIsIndoors(boolean isIndoors) {
		this.isIndoors=isIndoors;
	}
	public boolean getIsIndoors() {
		return this.isIndoors;
	}
		
	@Override
	public boolean isMoving() {
		return getBody().getLinearVelocity().len() != 0;
	}

	public ERangedWeapon getEquippedWeapon() {
		return equippedWeapon;
	}
}