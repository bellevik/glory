package se.glory.zombieworld.model.entities.weapons;

import se.glory.zombieworld.model.entities.items.Item;
import se.glory.zombieworld.utilities.Constants.ItemType;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class EquipedItem implements Item{

	protected String name;
	protected float damage;
	protected float range;

	protected Texture texture;
	protected Image icon;

	public EquipedItem(String name, float damage, float range) {
		this.name = name;
		this.damage = damage;
		this.range = range;

		texture = new Texture(Gdx.files.internal("data/weapons/" + name + "/" + name + ".png"));
		icon = new Image(texture);
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

	public Texture getTexture() {
		return texture;
	}
	
	public Image getIcon() {
		return icon;
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

	public void setTexture(Texture icon) {
		this.texture = icon;
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
