package se.glory.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Animator {

	private static final int FRAME_COLS = 6;
	private static final int FRAME_ROWS = 2;
	
	private int isRunning;
	
	private Animation animation;
	private Texture spriteSheet;
	private TextureRegion currentFrame;
	private TextureRegion[] frames;
	
	float stateTimer;
	
	float x, y, width, height;
	
	public Animator(String fileName, float x, float y, int running){
		this.x = x;
		this.y = y;
		
		this.isRunning = running;
		
		spriteSheet = new Texture(Gdx.files.internal("img/" + fileName));
		TextureRegion[][] temp = TextureRegion.split(spriteSheet, spriteSheet.getWidth() / FRAME_COLS, spriteSheet.getHeight() / FRAME_ROWS);

		frames = new TextureRegion[FRAME_COLS];

		int index = 0;
		
		switch(isRunning){
		case 0:
			for (int i = 0; i < FRAME_ROWS/2 ; i++) {
				for (int j = 0; j < FRAME_COLS; j++) {
					frames[index++] = temp[i][j];
				}
				animation = new Animation(0.085f, frames);
				stateTimer = 0f;
			};
		case 1:
			for (int i = 6; i < FRAME_ROWS; i++) {
				for (int j = 0; j < FRAME_COLS; j++) {
					frames[index++] = temp[i][j];
				}
				animation = new Animation(0.085f, frames);
				stateTimer = 0f;
			};
		}
		
		
	}
	
	public void drawAnimation(SpriteBatch batch, float x, float y){
		Gdx.app.log("CurrentFrame", "" + currentFrame);
		Gdx.app.log("Animation", "" + animation);
        stateTimer += Gdx.graphics.getDeltaTime();
        currentFrame = animation.getKeyFrame(stateTimer, true);
        batch.begin();
        batch.draw(currentFrame, x*Constants.BOX_TO_WORLD - currentFrame.getRegionWidth() / 2, y*Constants.BOX_TO_WORLD - currentFrame.getRegionHeight() / 2);
        batch.end();
	}
}
