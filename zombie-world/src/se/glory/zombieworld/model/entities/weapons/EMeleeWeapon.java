package se.glory.zombieworld.model.entities.weapons;

import se.glory.zombieworld.model.entities.items.Item;
import se.glory.zombieworld.utilities.Constants.ItemType;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class EMeleeWeapon implements Item {

	protected String name;
	protected float damage;
	protected float range;
	// Difficulty to find weapon higher number = harder to find
	protected int level = 0;
	// Array of textures representing the weapon
	protected Texture[] textures;
	
	public EMeleeWeapon(String name, float damage, float range) {
		this.name = name;
		this.damage = damage;
		this.range = range;
		
		textures = new Texture[8];
		
		textures[0] = new Texture(Gdx.files.internal("data/weapons/" + name + "/" + name + ".png"));
		textures[1] = new Texture(Gdx.files.internal("data/weapons/" + name + "/shop" + name + ".png"));
	}
	
	public int getLevel() {
		return level;
	}

	public String getName() {
		return name;
	}

	public float getDamage() {
		return damage;
	}

	public float getRange() {
		return range;
	}

	public Texture getTexture(int index) {
		return textures[index];
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDamage(float damage) {
		this.damage = damage;
	}

	public void setRange(float range) {
		this.range = range;
	}

	public void setTextures(Texture[] texture) {
		for(int i = 0; i < texture.length; i++) {
			this.textures[i] = texture[i];
		}
	}

	@Override
	public ItemType getItemType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getItemName() {
		// TODO Auto-generated method stub
		return null;
	}

}
