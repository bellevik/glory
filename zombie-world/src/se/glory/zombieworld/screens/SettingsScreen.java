package se.glory.zombieworld.screens;

import se.glory.zombieworld.utilities.Constants;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class SettingsScreen implements Screen {
	private Stage stage;
	private TextureAtlas atlas;
	private Skin skin;
	private Image buttonExit, buttonPlay;
	private BitmapFont font;
	
	private SpriteBatch batch;
	private Texture backgroundTexture, buttonExitTexture, buttonPlayTexture, buttonSettingsTexture;
	private boolean pressedPlay = false, pressedExit = false;
	private int count = 0;
    
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(255, 255, 255, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		batch.draw(backgroundTexture, 0, 0);
		batch.end();
		
		stage.act(delta);
		stage.draw();
		
		drawMenu(batch);
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void show() {
		stage = new Stage();
		
		batch = new SpriteBatch();
		
		backgroundTexture = new Texture(Gdx.files.internal("ui/mainMenuBackground.png"));
		buttonExitTexture = new Texture(Gdx.files.internal("ui/buttonExit.png"));
		buttonPlayTexture = new Texture(Gdx.files.internal("ui/buttonPlay.png"));
		
		Gdx.input.setInputProcessor(stage);
		
		//Create buttons
		buttonPlay = new Image(buttonPlayTexture);
		buttonPlay.setX(Constants.VIEWPORT_WIDTH/2 - 142/2);
		buttonPlay.setY(250);
		
		
		
		buttonExit = new Image(buttonExitTexture);
		buttonExit.setX(Constants.VIEWPORT_WIDTH/2 - 144/2);
		buttonExit.setY(100);

		//Adding listeners to buttons
		buttonExit.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				count = 0;
				pressedExit = true;
				Gdx.app.exit();
			}
		});
		
		buttonPlay.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				count = 0;
				pressedPlay = true;
				((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen());
			}
		});
		
		stage.addActor(buttonPlay);
		stage.addActor(buttonExit);
	}

	public void drawMenu(SpriteBatch batch){
		if(pressedPlay){
			buttonPlayTexture = new Texture(Gdx.files.internal("ui/buttonPlayHover.png"));
			count++;
			if(count == 50){
				pressedPlay = false;
				count = 0;
			}
		}else if(pressedExit){
			buttonExitTexture = new Texture(Gdx.files.internal("ui/buttonExitHover.png"));
			count++;
			if(count == 50){
				pressedExit = false;
				count = 0;
			}
		}else{
			buttonPlayTexture = new Texture(Gdx.files.internal("ui/buttonPlay.png"));
			buttonExitTexture = new Texture(Gdx.files.internal("ui/buttonExit.png"));
		}
		batch.begin();
		batch.draw(buttonPlayTexture, Constants.VIEWPORT_WIDTH/2 - 142/2, 250);
		batch.draw(buttonExitTexture, Constants.VIEWPORT_WIDTH/2 - 144/2, 100);
		batch.end();
	}
	
	@Override
	public void hide() {
		
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void dispose() {
		
	}
}
