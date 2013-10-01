package se.glory.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Animator {

	private static final int FRAME_COLS = 6;
	private static final int FRAME_ROWS = 6;
	
	private Animation animation;
	private Texture spriteSheet;
	private TextureRegion currentFrame;
	private TextureRegion[] frames;
	
	float stateTimer;
	
	public Animator(){
		spriteSheet = new Texture(Gdx.files.internal("img/spritesheet.png"));
		TextureRegion[][] temp = TextureRegion.split(spriteSheet, spriteSheet.getWidth() / FRAME_COLS, spriteSheet.getHeight() / FRAME_ROWS);

		frames = new TextureRegion[FRAME_COLS * FRAME_ROWS];

		int index = 0;
		
		for (int i = 0; i < FRAME_ROWS; i++) {
			for (int j = 0; j < FRAME_COLS; j++) {
				frames[index++] = temp[i][j];
			}
			animation = new Animation(0.085f, frames);
			stateTimer = 0f;
		}
	}
	
	public void drawAnimation(SpriteBatch batch){
        stateTimer += Gdx.graphics.getDeltaTime();
        currentFrame = animation.getKeyFrame(stateTimer, true);
        batch.begin();
        batch.draw(currentFrame, 100, 100);
        batch.end();
	}
}
