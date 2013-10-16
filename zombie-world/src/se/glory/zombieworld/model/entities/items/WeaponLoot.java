package se.glory.zombieworld.model.entities.items;

import se.glory.zombieworld.model.WorldModel;
import se.glory.zombieworld.model.entities.weapons.EquippableItem;
import se.glory.zombieworld.model.entities.weapons.WeaponArsenal;
import se.glory.zombieworld.utilities.Constants;
import se.glory.zombieworld.utilities.Constants.ItemType;
import se.glory.zombieworld.utilities.Identity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

/*
 * This class represents a weapon lying on the ground. All WeaponLoot is
 * available for pick-up by the player. If the player collides with a WeaponLoot
 * in the world and got space in its inventory the player will get the Weapon in its
 * inventory. 
 */
public class WeaponLoot {
	private EquippableItem weapon;
	private Constants.ItemType type;
	private final float width = 16;
	private final float height = 16;
	
	private Body body;
	private BodyDef bodyDef;
	
	/*
	 * This constructor will place a random weapon at the location (x, y)
	 */
	public WeaponLoot(float x, float y) {
		this(WorldModel.weaponArsenal.getRandomWeapon(), x, y);
	}
	
	/*
	 * This constructor will place a weapon on the type weaponName at (x, y)
	 */
	public WeaponLoot(EquippableItem weapon, float x, float y) {
		this.weapon = weapon;
		
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(new Vector2(x * Constants.WORLD_TO_BOX, y * Constants.WORLD_TO_BOX));
		body = WorldModel.world.createBody(bodyDef);
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(width * Constants.WORLD_TO_BOX, height * Constants.WORLD_TO_BOX);
		
		FixtureDef fixtureDef= new FixtureDef();
		fixtureDef.shape = shape;
		body.createFixture(fixtureDef);
		
		//Texture texture = new Texture(Gdx.files.internal("img/loot.png"));
		
		Identity identity = new Identity();
		identity.setWidth(width);
		identity.setHeight(height);
		identity.setTexture(weapon.getTexture());
		identity.setType(Constants.MoveableBodyType.ITEM);
		identity.setObj(this);
		
		body.setUserData(identity);
	}

	public ItemType getItemType() {
		return type;
	}

	public String getItemName() {
		return weapon.getItemName();
	}

	public EquippableItem getWeapon() {
		return weapon;
	}
}