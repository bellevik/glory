package se.glory.zombieworld.model.entities.weapons;

import se.glory.zombieworld.model.WorldModel;

public class ERangedWeapon {
	
	public ERangedWeapon(String name, float damage, float range, int clipSize, int clips, float fireRate) {
		//super(name, damage, range, clipSize, clips, fireRate, clipSize);
		WorldModel.player.emptyClip = false;
	}
	
}
