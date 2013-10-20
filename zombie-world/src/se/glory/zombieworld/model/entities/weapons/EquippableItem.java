package se.glory.zombieworld.model.entities.weapons;

import se.glory.zombieworld.model.WorldModel;
import se.glory.zombieworld.utilities.Constants;
import se.glory.zombieworld.utilities.Constants.ItemType;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class EquippableItem {

	protected String name;
	protected ItemType itemType;
	protected float damage;
	protected float range;
	protected int clipSize;
	protected int clips;
	protected float fireRate;
	protected int currentClipSize;
	protected int offset;
	protected int price;

	protected Texture texture;
	protected Texture shopTexture;
	protected Image icon;

	public EquippableItem(String name, String itemType, float damage, float range, int clipSize, int clips, float fireRate, int price) {
		this.name = name;
		this.damage = damage;
		this.range = range;
		this.clipSize = clipSize;
		this.clips = clips;
		this.fireRate = fireRate;
		this.currentClipSize = clipSize;
		this.price = price;

		if(itemType.equals("ranged")) {
			this.itemType = Constants.ItemType.WEAPON;
			this.offset = 24;
		} else if(itemType.equals("consumable")) {
			this.itemType = Constants.ItemType.CONSUMABLE;
			this.offset = 0;
		}
		
		texture = new Texture(Gdx.files.internal("data/weapons/" + name + "/" + name + ".png"));
		shopTexture = new Texture(Gdx.files.internal("data/weapons/" + name + "/shop" + name + ".png"));
		icon = new Image(texture);
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
	
	public Texture getShopTexture() {
		return shopTexture;
	}
	
	public Image getIcon() {
		return icon;
	}
	
	public int getOffset() {
		return offset;
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
	
	/*
	 * This will add a clip to the item. It will happen when the player buys an item from
	 * the shop or if he/she picks up a weapon on the ground that allready exists in
	 * the inventory
	 */
	public void addClip (int clips) {
		if (WorldModel.player.emptyClip) {
			WorldModel.player.emptyClip = false;
		}
		this.clips += clips;
		this.currentClipSize = clipSize;
	}
	
	/*
	 * Whenever the player fires off a shot this method will be called. First it checks if its
	 * bullets left in the clip if so remove 1 bullet from the clip. Otherwise it removes a
	 * clip and sets the bullets in that clip to default value.
	 */
	public void removeBulletFromClip() {
		if (currentClipSize > 1) {
			currentClipSize--;
		} else {
			if (clips > 0) {
				clips--;
				currentClipSize = clipSize;
			} else {
				clips = 0;
				currentClipSize = 0;
				WorldModel.player.emptyClip = true;
			}
		}
	}

	public ItemType getItemType() {
		return itemType;
	}

	public String getItemName() {
		return name;
	}
	
	public int getClipSize() {
		return clipSize;
	}
	
	public int getClips() {
		return clips;
	}
	
	public float getFireRate() {
		return fireRate;
	}
	
	public void setClipSize(int clipSize) {
		this.clipSize = clipSize;
	}
	
	public void setClips(int clips) {
		this.clips = clips;
	}
	
	public void setFireRate(int fireRate) {
		this.fireRate = fireRate;
	}

	public int getCurrentClipSize() {
		return currentClipSize;
	}
	
	public int getPrice () {
		return price;
	}
}
