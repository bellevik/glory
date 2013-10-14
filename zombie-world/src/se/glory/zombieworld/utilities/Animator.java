package se.glory.zombieworld.utilities;

import se.glory.zombieworld.utilities.Constants.MoveableBodyType;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/*
 * This class creates animations from sprite sheets. This will make it look like the 
 * player is walking around on the screen. This class will also draw the animations on
 * the screen.
 */
public class Animator {
	// Rows and columns for the characters animations
	private final int CHAR_FRAME_COLS = 4;
	private final int CHAR_FRAME_ROWS = 8;
	
	// Rows and columns for the door animations
	private final int DOOR_FRAME_COLS = 4;
	private final int DOOR_FRAME_ROWS = 3;
	
	// Animation speeds
	private final float WALKSPEED = 1.525f;
	private final float OPENINGSPEED = 0.385f;
	private final float ROTATESPEED = 0.2f;
	
	private Texture spriteSheet;
	private TextureRegion currentFrame;
	private TextureRegion[] frames;
	
	private float stateTimer;
	
	// Arrays for all animations
	private Animation[] humanAnimations = new Animation[8];
	private Animation[] playerAnimations = new Animation[8];
	private Animation[] zombieAnimations = new Animation[8];
	private Animation[] doorAnimations = new Animation[3];
	//private Animation[] weaponLootAnimations = new Animation[];
	
	private Texture closedDoor;
	private Animation weaponLoot;
	
	private String character = "character", door = "door", weapon = "weapon";
	
	/*
	 * Creates all animations for all types of creatures. This will speed up the game
	 * since they are not created on-the-fly.
	 */
	public Animator() {
		//Creating human animation
		for (int i = 0; i< humanAnimations.length;i++){
			humanAnimations[i] = createAnimation("humanSheet.png", character, i);
		}
		//Creating player animation
		for (int i = 0; i< playerAnimations.length;i++){
			playerAnimations[i] = createAnimation("playerSheet.png", character, i);
		}
		//Creating zombie animation
		for (int i = 0; i< zombieAnimations.length;i++){
			zombieAnimations[i] = createAnimation("zombieSheet.png", character, i);
		}
		for (int i = 0; i < doorAnimations.length; i++){
			doorAnimations[i] = createAnimation("doorSheet.png", door, i);
		}
		// TODO: Ekman får fixa med weaponsloot animationerna
		
		/*for (int i = 0; i < weaponLootAnimations.length; i++){
			weaponLootAnimations[i] = createAnimation("AKSheet66.png", weapon, i);
		}*/
		weaponLoot = createAnimation("AKSheet66.png", weapon, 0);
		
	}
	
	public Texture getClosedDoor(){
		return closedDoor;
	}
	
	/*
	 * Returns different animations depending on the type and index passed along.
	 */
	public Animation getAnimation(MoveableBodyType type, int i) {
		if(type == MoveableBodyType.HUMAN){
			return humanAnimations[i];
		}else if(type == MoveableBodyType.PLAYER){
			return playerAnimations[i];
		}else if (type == MoveableBodyType.ZOMBIE){
			return zombieAnimations[i];
		}else if (type == MoveableBodyType.DOOR){
			return doorAnimations[i];
		}else if (type == MoveableBodyType.WEAPON){
			return weaponLoot;
		}else{
			return null;
		}
	}
	
	/*
	 * Creates an animation depending on the filename and direction passed along.
	 * Returns the created animation.
	 */
	public Animation createAnimation (String fileName, String type, int direction){
		Animation animation = null;
		
		//Loading the spritesheet
		spriteSheet = new Texture(Gdx.files.internal("img/" + fileName));

		int index = 0;

		if(type.equals(character)){
			
			//Dividing the spritesheet into regions with an image in each frame
			TextureRegion[][] tmp = TextureRegion.split(spriteSheet, spriteSheet.getWidth() / CHAR_FRAME_COLS, spriteSheet.getHeight() / CHAR_FRAME_ROWS);

			//Specifiying number of images in the array that will be animated
			frames = new TextureRegion[CHAR_FRAME_COLS];
			
			//Creates different animations depending on the direction
			for (int i = 0; i < CHAR_FRAME_COLS; i++) {
				frames[index++] = tmp[direction][i];
			}
			//Parameters (update speed, array of TextureRegions to make an animation)
			animation = new Animation(WALKSPEED, frames);
			index = 0;
			
		}else if(type.equals(door)){
			
			//Dividing the spritesheet into regions with an image in each frame
			TextureRegion[][] tmp = TextureRegion.split(spriteSheet, spriteSheet.getWidth() / DOOR_FRAME_COLS, spriteSheet.getHeight() / DOOR_FRAME_ROWS);
			
			//Specifiying number of images in the array that will be animated
			frames = new TextureRegion[DOOR_FRAME_COLS];
			
			//Creates the animation for a closed door
			closedDoor = new Texture("img/closedDoor_" + direction + ".png");
			
			//Creates different animations depending on the door type
			for (int i = 0; i < DOOR_FRAME_COLS; i++) {
				frames[index++] = tmp[direction][i];
			}
			//Parameters (update speed, array of TextureRegions to make an animation)
			animation = new Animation(OPENINGSPEED, frames);
			index = 0;
			
		}else if(type.equals(weapon)){
			
			int col = spriteSheet.getWidth() / 64;
			int row = spriteSheet.getHeight() / 64;
			
			TextureRegion[][] tmp = TextureRegion.split(spriteSheet, spriteSheet.getWidth() / col, spriteSheet.getHeight() / row);
			
			frames = new TextureRegion[row * col];
			
			for (int i = 0; i < row ; i++) {
				for (int j = 0; j < col; j++) {
					frames[index++] = tmp[i][j];
				}
			}
			//Parameters (update speed, array of TextureRegions to make an animation)
			animation = new Animation(ROTATESPEED, frames);
			index = 0;
			
		}
		return animation;
	}
	
	/*
	 * Gets the current frame in the animation and draws to the screen.
	 */
	public void drawAnimation(SpriteBatch batch, float x, float y, Animation ani, boolean isLooping){

		//stateTimer = the time spent in the state represented by this animation
        stateTimer += Gdx.graphics.getDeltaTime();
        
        if(Constants.gameState == Constants.GameState.RUNNING) {
        	//Get the current frame and loops if the creature is moving
            currentFrame = ani.getKeyFrame(stateTimer, isLooping);
        } else {
        	//Show the stand-still-frame if the game is paused
        	currentFrame = ani.getKeyFrame(stateTimer, false);
        }
        
        batch.begin();
        batch.draw(currentFrame, x*Constants.BOX_TO_WORLD - currentFrame.getRegionWidth() / 2, y*Constants.BOX_TO_WORLD - currentFrame.getRegionHeight() / 2);
        batch.end();
	}
}
