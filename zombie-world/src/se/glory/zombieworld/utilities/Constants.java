package se.glory.zombieworld.utilities;

public class Constants {
	public static final float WORLD_TO_BOX = 0.01f;
	public static final float BOX_TO_WORLD = 100f;
	
	public static final boolean DEBUG_MODE = false;
	
	public static final int WEAPON_TYPE = 0;
	public static final int WEAPON_DAMAGE = 1;
	public static final int WEAPON_RANGE = 2;
	public static final int WEAPON_CLIP_SIZE = 3;
	public static final int WEAPON_CLIPS = 4;
	public static final int WEAPON_FIRE_RATE = 5;
	
	public static final int VIEWPORT_WIDTH = 800;
	public static int VIEWPORT_HEIGHT;
	

	public static final int INFECTED_INTERVAL = 1500;
	public static final int INFECTED_DAMAGE = 2;
	public static final int ZOMBIE_DAMAGE = 10;
	
	public static GameState gameState = GameState.RUNNING;

	//public static boolean isRunning = true;
	
	public static enum GameState {
		RUNNING, PAUSE, SHOP;
	}
	
	public static enum MoveableBodyShape {
		CIRCLE, SQUARE;
	}
	
	public static enum MoveableBodyType {
		PLAYER, HUMAN, ZOMBIE, BULLET, HOUSE, ITEM, WEAPON, DOOR, STREETOBJECT;
	}
	
	public static enum TouchpadType {
		MOVEMENT, FIRE, ITEM_SELECTION
	}
	
	public static enum ItemType {
		WEAPON, CONSUMABLE;
	}
	
	public static enum ScoreType {
		KILL_ZOMBIE, KILL_HUMAN, TIME;
	}
	
}
