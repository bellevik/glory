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

public class MainMenu implements Screen {
	private Stage stage;
	
	private SpriteBatch batch;
	
	private Texture backgroundTexture, buttonExitTexture, buttonPlayTexture, buttonSettingsTexture;
	private Image buttonExit, buttonPlay, buttonSettings;
    
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
		buttonExitTexture = new Texture(Gdx.files.internal("ui/buttonExit.png"));
		buttonPlayTexture = new Texture(Gdx.files.internal("ui/buttonPlay.png"));
		buttonSettingsTexture = new Texture(Gdx.files.internal("ui/buttonSettings.png"));
		
		Gdx.input.setInputProcessor(stage);
		
		//Create buttons
		buttonPlay = new Image(buttonPlayTexture);
		buttonPlay.setX(Constants.VIEWPORT_WIDTH/2 - buttonPlayTexture.getWidth()/2);
		buttonPlay.setY(250);
		
		buttonSettings = new Image(buttonSettingsTexture);
		buttonSettings.setX(Constants.VIEWPORT_WIDTH/2 - buttonSettingsTexture.getWidth()/2);
		buttonSettings.setY(buttonPlay.getY() - 75);
		
		buttonExit = new Image(buttonExitTexture);
		buttonExit.setX(Constants.VIEWPORT_WIDTH/2 - buttonExitTexture.getWidth()/2);
		buttonExit.setY(buttonSettings.getY() - 75);

		//Adding listeners to buttons
		buttonExit.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.exit();
			}
		});
		buttonSettings.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				
			}
		});
		buttonPlay.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen());
			}
		});
		
		stage.addActor(buttonPlay);
		stage.addActor(buttonSettings);
		stage.addActor(buttonExit);
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
