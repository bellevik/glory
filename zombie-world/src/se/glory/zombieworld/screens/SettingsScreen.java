package se.glory.zombieworld.screens;

import se.glory.zombieworld.utilities.Constants;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class SettingsScreen implements Screen {
	private Stage stage;
	
	private SpriteBatch batch;
	
	private Texture backgroundTexture, buttonSFXTexture, buttonMusicTexture, buttonBackTexture;
	private Image buttonSFX, buttonMusic, buttonBack;
    
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(255, 255, 255, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		batch.draw(backgroundTexture, 0, 0);
		batch.end();
		
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void show() {
		stage = new Stage();
		
		batch = new SpriteBatch();
		
		backgroundTexture = new Texture(Gdx.files.internal("ui/mainMenuBackground.png"));
		buttonMusicTexture = new Texture(Gdx.files.internal("ui/buttonMusic.png"));
		buttonSFXTexture = new Texture(Gdx.files.internal("ui/buttonSFX.png"));		
		
		Gdx.input.setInputProcessor(stage);
		
		//Create buttons
		buttonMusic = new Image(buttonMusicTexture);
		buttonMusic.setX(Constants.VIEWPORT_WIDTH/4 - buttonMusicTexture.getWidth()/4);
		buttonMusic.setY(250);
		
		buttonSFX = new Image(buttonSFXTexture);
		buttonSFX.setX(Constants.VIEWPORT_WIDTH/4 - buttonSFXTexture.getWidth()/4);

		buttonBack = new Image(buttonBackTexture);
		buttonBack.setX(Constants.VIEWPORT_WIDTH/4 - buttonSFXTexture.getWidth()/4);
		
		//Adding listeners to buttons
		buttonSFX.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				
			}
		});
		buttonMusic.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				
			}
		});
		buttonBack.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				
			}
		});
		
		stage.addActor(buttonMusic);
		stage.addActor(buttonSFX);
		stage.addActor(buttonBack);
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
