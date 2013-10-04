package se.glory.zombieworld.model.entities.weapons;

public class ERangedWeapon extends EMeleeWeapon {
	
	private int clipSize;
	private int clips;
	private int currentClip;
	// Array of textures representing the bullet
	
	public ERangedWeapon(String name, float damage, float range, int clipSize, int clips, int currentClip) {
		super(name, damage, range);
		this.clipSize = clipSize;
		this.clips = clips;
		this.currentClip = currentClip;
	}
	
	public int getClipSize() {
		return clipSize;
	}
	
	public int getClips() {
		return clips;
	}
	
	public int getCurrentClip() {
		return currentClip;
	}
	
	public void setClipSize(int clipSize) {
		this.clipSize = clipSize;
	}
	
	public void setClips(int clips) {
		this.clips = clips;
	}
	
	public void setCurrentClip(int currentClip) {
		this.currentClip = currentClip;
	}
}
