package se.glory.zombieworld.utilities;

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
	private final int FRAME_COLS = 4;
	private final int FRAME_ROWS = 8;
	private final float walkSpeed = 0.525f;
	
	private Texture spriteSheet;
	private TextureRegion currentFrame;
	private TextureRegion[] frames;
	
	private float stateTimer;
	
	private Animation[] humanAnimations = new Animation[8];
	private Animation[] playerAnimations = new Animation[8];
	private Animation[] zombieAnimations = new Animation[8];
	
	/*
	 * Creates all animations for all types of creatures. This will speed up the game
	 * since they are not created on-the-fly.
	 */
	public Animator() {
		//Creating human animation
		for (int i = 0; i<8;i++){
			humanAnimations[i] = createAnimation("humanSheet.png", i);
		}
		//Creating player animation
		for (int i = 0; i<8;i++){
			playerAnimations[i] = createAnimation("playerSheet.png", i);
		}
		//Creating zombie animation
		for (int i = 0; i<8;i++){
			zombieAnimations[i] = createAnimation("zombieSheet.png", i);
		}
	}
	
	/*
	 * Returns different animations depending on the type and index passed along.
	 */
	public Animation getAnimation(String type, int i) {
		if(type.equals("Human")){
			return humanAnimations[i];
		}else if(type.equals("Player")){
			return playerAnimations[i];
		}else if (type.equals("Zombie")){
			return zombieAnimations[i];
		}else{
			return null;
		}
	}
	
	/*
	 * Creates an animation depending on the filename and direction passed along.
	 * Returns the created animation.
	 */
	private Animation createAnimation(String fileName, int direction) {
		Animation animation = null;
		
		//Loading the spritesheet
		spriteSheet = new Texture(Gdx.files.internal("img/" + fileName));
		//Dividing the spritesheet into regions with an image in each frame
		TextureRegion[][] tmp = TextureRegion.split(spriteSheet, spriteSheet.getWidth() / FRAME_COLS, spriteSheet.getHeight() / FRAME_ROWS);

		//Specifiying number of images in the array that will be animated
		frames = new TextureRegion[FRAME_COLS];

		int index = 0;
		//Creating different animations depending which direction the player is facing
		switch (direction){
		//Returns an animation facing east
		case 0:
			for (int i = 0; i < 1 ; i++) {
				for (int j = 0; j < FRAME_COLS; j++) {
					frames[index++] = tmp[i][j];
				}
				//Parameters (update speed, array of TextureRegions to make an animation
				animation = new Animation(walkSpeed, frames);
			};
			return animation;
		//Returns an animation facing northeast
		case 1:
			for (int i = 1; i < 2 ; i++) {
				for (int j = 0; j < FRAME_COLS; j++) {
					frames[index++] = tmp[i][j];
				}
				//Parameters (update speed, array of TextureRegions to make an animation
				animation = new Animation(walkSpeed, frames);
			};
			return animation;
		//Returns an animation facing north
		case 2:
			for (int i = 2; i < 3 ; i++) {
				for (int j = 0; j < FRAME_COLS; j++) {
					frames[index++] = tmp[i][j];
				}
				//Parameters (update speed, array of TextureRegions to make an animation
				animation = new Animation(walkSpeed, frames);
			};
			return animation;
		//Returns an animation facing northwest
		case 3:
			for (int i = 3; i < 4 ; i++) {
				for (int j = 0; j < FRAME_COLS; j++) {
					frames[index++] = tmp[i][j];
				}
				//Parameters (update speed, array of TextureRegions to make an animation
				animation = new Animation(walkSpeed, frames);
			};
			return animation;
		//Returns an animation facing west
		case 4:
			for (int i = 4; i < 5 ; i++) {
				for (int j = 0; j < FRAME_COLS; j++) {
					frames[index++] = tmp[i][j];
				}
				//Parameters (update speed, array of TextureRegions to make an animation
				animation = new Animation(walkSpeed, frames);
			};
			return animation;
		//Returns an animation facing southwest
		case 5:
			for (int i = 5; i < 6 ; i++) {
				for (int j = 0; j < FRAME_COLS; j++) {
					frames[index++] = tmp[i][j];
				}
				//Parameters (update speed, array of TextureRegions to make an animation
				animation = new Animation(walkSpeed, frames);
			};
			return animation;
		//Returns an animation facing south
		case 6:
			for (int i = 6; i < 7 ; i++) {
				for (int j = 0; j < FRAME_COLS; j++) {
					frames[index++] = tmp[i][j];
				}
				//Parameters (update speed, array of TextureRegions to make an animation
				animation = new Animation(walkSpeed, frames);
			};
			return animation;
		//Returns an animation facing southeast
		case 7:
			for (int i = 7; i < 8 ; i++) {
				for (int j = 0; j < FRAME_COLS; j++) {
					frames[index++] = tmp[i][j];
				}
				//Parameters (update speed, array of TextureRegions to make an animation
				animation = new Animation(walkSpeed, frames);
			};
			return animation;
		//If the direction doesn't exist , returns null
		default:
			return null;
		}
	}
	
	/*
	 * Gets the current frame in the animation and draws to the screen.
	 */
	public void drawAnimation(SpriteBatch batch, float x, float y, Animation ani){
		//stateTime = the time spent in the state represented by this animation
        stateTimer += Gdx.graphics.getDeltaTime();
        //Get the current frame
        currentFrame = ani.getKeyFrame(stateTimer, true);
        batch.begin();
        batch.draw(currentFrame, x*Constants.BOX_TO_WORLD - currentFrame.getRegionWidth() / 2, y*Constants.BOX_TO_WORLD - currentFrame.getRegionHeight() / 2);
        batch.end();
	}
}
