package se.glory.zombieworld.model.entities.weapons;

import se.glory.zombieworld.model.WorldModel;

public class ERangedWeapon extends EMeleeWeapon {
	
	private int clipSize;
	private int clips;
	private int reloadTime;
	private int currentClipSize;
	// Array of textures representing the bullet
	
	public ERangedWeapon(String name, float damage, float range, int clipSize, int clips, int reloadTime) {
		super(name, damage, range);
		this.clipSize = clipSize;
		this.clips = clips;
		this.reloadTime = reloadTime;
		this.currentClipSize = clipSize;
		WorldModel.player.emptyClip = false;
	}
	
	public void addClip (int clips) {
		if (WorldModel.player.emptyClip) {
			WorldModel.player.emptyClip = false;
		}
		this.clips += clips;
		this.currentClipSize = clipSize;
	}
	
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
	
	public int getReloadTime() {
		return reloadTime;
	}
	
	public void setClipSize(int clipSize) {
		this.clipSize = clipSize;
	}
	
	public void setClips(int clips) {
		this.clips = clips;
	}
	
	public void setReloadTimep(int reloadTime) {
		this.reloadTime = reloadTime;
	}

	public int getCurrentClipSize() {
		return currentClipSize;
	}
}
