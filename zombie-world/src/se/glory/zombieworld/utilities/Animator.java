package se.glory.zombieworld.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Animator {

	private static final int FRAME_COLS = 4;
	private static final int FRAME_ROWS = 8;
	
	private int isRunning;
	
	private static Animation animation;
	private static Texture spriteSheet;
	private static TextureRegion currentFrame;
	private static TextureRegion[] frames;
	
	static float stateTimer;
	
	float x, y, width, height;
	
	public static Animation createAnimation(String fileName, float x, float y, int direction){
		
		spriteSheet = new Texture(Gdx.files.internal("img/" + fileName));
		TextureRegion[][] tmp = TextureRegion.split(spriteSheet, spriteSheet.getWidth() / FRAME_COLS, spriteSheet.getHeight() / FRAME_ROWS);

		frames = new TextureRegion[FRAME_COLS];

		int index = 0;
		
		switch (direction){
		//EAST
		case 0:
			for (int i = 0; i < 1 ; i++) {
				for (int j = 0; j < FRAME_COLS; j++) {
					frames[index++] = tmp[i][j];
				}
				animation = new Animation(0.125f, frames);
			};
			return animation;
		//NORTHEAST
		case 1:
			for (int i = 1; i < 2 ; i++) {
				for (int j = 0; j < FRAME_COLS; j++) {
					frames[index++] = tmp[i][j];
				}
				animation = new Animation(0.125f, frames);
			};
			return animation;
		//NORTH
		case 2:
			for (int i = 2; i < 3 ; i++) {
				for (int j = 0; j < FRAME_COLS; j++) {
					frames[index++] = tmp[i][j];
				}
				animation = new Animation(0.125f, frames);
			};
			return animation;
		//NORTHWEST
		case 3:
			for (int i = 3; i < 4 ; i++) {
				for (int j = 0; j < FRAME_COLS; j++) {
					frames[index++] = tmp[i][j];
				}
				animation = new Animation(0.125f, frames);
			};
			return animation;
		//WEST
		case 4:
			for (int i = 4; i < 5 ; i++) {
				for (int j = 0; j < FRAME_COLS; j++) {
					frames[index++] = tmp[i][j];
				}
				animation = new Animation(0.125f, frames);
			};
			return animation;
		//SOUTHWEST
		case 5:
			for (int i = 5; i < 6 ; i++) {
				for (int j = 0; j < FRAME_COLS; j++) {
					frames[index++] = tmp[i][j];
				}
				animation = new Animation(0.125f, frames);
			};
			return animation;
		//SOUTH
		case 6:
			for (int i = 6; i < 7 ; i++) {
				for (int j = 0; j < FRAME_COLS; j++) {
					frames[index++] = tmp[i][j];
				}
				animation = new Animation(0.125f, frames);
			};
			return animation;
		//SOUTHEAST
		case 7:
			for (int i = 7; i < 8 ; i++) {
				for (int j = 0; j < FRAME_COLS; j++) {
					frames[index++] = tmp[i][j];
				}
				animation = new Animation(0.125f, frames);
			};
			return animation;
		default:
			return null;
		}
	}
	
	public static void drawAnimation(SpriteBatch batch, float x, float y){
        stateTimer += Gdx.graphics.getDeltaTime();
        currentFrame = animation.getKeyFrame(stateTimer, true);
        batch.begin();
        batch.draw(currentFrame, x*Constants.BOX_TO_WORLD - currentFrame.getRegionWidth() / 2, y*Constants.BOX_TO_WORLD - currentFrame.getRegionHeight() / 2);
        batch.end();
	}
}
