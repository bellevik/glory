package se.glory.zombieworld.model.entities.weapons;

import se.glory.zombieworld.model.WorldModel;

public class ERangedWeapon extends EquipedItem {
	
	private int clipSize;
	private int clips;
	private float fireRate;
	private int currentClipSize;
	
	public ERangedWeapon(String name, float damage, float range, int clipSize, int clips, float fireRate) {
		super(name, damage, range);
		this.clipSize = clipSize;
		this.clips = clips;
		this.fireRate = fireRate;
		this.currentClipSize = clipSize;
		WorldModel.player.emptyClip = false;
	}
	
	/*
	 * This will add a clip to the weapon. It will happen when the player buys an item from
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
}
