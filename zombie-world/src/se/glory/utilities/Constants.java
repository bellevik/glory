package se.glory.utilities;

public class Constants {
	public static final float WORLD_TO_BOX = 0.01f;
	public static final float BOX_TO_WORLD = 100f;
	
	public static final boolean DEBUG_MODE = true;
	
	public static final int WEAPON_DAMAGE = 1;
	public static final int WEAPON_RANGE = 2;
	public static final int WEAPON_CLIP_SIZE = 3;
	public static final int WEAPON_CLIPS = 4;
	public static final int WEAPON_CURRENT_CLIP = 5;
	
	public static enum MoveableBodyShape {
		CIRCLE, SQUARE;
	}
	
	public static enum MoveableBodyType {
		PLAYER, HUMAN, ZOMBIE, BULLET, HOUSE, WEAPON;
	}
	
	public static enum TouchpadType {
		MOVEMENT, FIRE, ITEM_SELECTION
	}
}
