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
	
	private Texture backgroundTexture, SFXLabelTexture, musicLabelTexture, backButtonTexture;
	private Image SFXLabel, musicLabel, backButton;
    
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
		musicLabelTexture = new Texture(Gdx.files.internal("ui/buttonMusic.png"));
		SFXLabelTexture = new Texture(Gdx.files.internal("ui/buttonSFX.png"));
		backButtonTexture = new Texture(Gdx.files.internal("ui/buttonBack.png"));
		
		Gdx.input.setInputProcessor(stage);
		
		//Create buttons
		musicLabel = new Image(musicLabelTexture);
		musicLabel.setX(Constants.VIEWPORT_WIDTH/4 - musicLabelTexture.getWidth()/4);
		musicLabel.setY(250);
		
		SFXLabel = new Image(SFXLabelTexture);
		SFXLabel.setX(Constants.VIEWPORT_WIDTH/4 - SFXLabelTexture.getWidth()/4);
		SFXLabel.setY(200);
		
		backButton = new Image(backButtonTexture);
		backButton.setX(Constants.VIEWPORT_WIDTH/2 - SFXLabelTexture.getWidth()/2);
		backButton.setY(100);
		
		//Adding listeners to buttons
		SFXLabel.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				
			}
		});
		musicLabel.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				
			}
		});
		backButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu());
			}
		});
		
		stage.addActor(musicLabel);
		stage.addActor(SFXLabel);
		stage.addActor(backButton);
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
