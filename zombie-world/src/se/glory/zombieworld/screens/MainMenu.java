package se.glory.zombieworld.screens;

import se.glory.zombieworld.utilities.Constants;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MainMenu implements Screen {
	private Stage stage;
	private TextureAtlas atlas;
	private Skin skin;
	private Image buttonExit, buttonPlay, buttonSettings;
	private BitmapFont font;
	
	private SpriteBatch batch;
	private Texture backgroundTexture, buttonExitTexture, buttonPlayTexture, buttonSettingsTexture;
	private boolean pressedPlay = false, pressedSettings = false, pressedExit = false;
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
		buttonSettingsTexture = new Texture(Gdx.files.internal("ui/buttonSettings.png"));
		
		Gdx.input.setInputProcessor(stage);
		
		//Create buttons
		buttonPlay = new Image(buttonPlayTexture);
		buttonPlay.setX(Constants.VIEWPORT_WIDTH/2 - 142/2);
		buttonPlay.setY(250);
		
		buttonSettings = new Image(buttonSettingsTexture);
		buttonSettings.setX(Constants.VIEWPORT_WIDTH/2 - 226/2);
		buttonSettings.setY(175);
		
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
		buttonSettings.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				count = 0;
				pressedSettings = true;
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
		stage.addActor(buttonSettings);
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
		}else if(pressedSettings){
			buttonSettingsTexture = new Texture(Gdx.files.internal("ui/buttonSettingsHover.png"));
			count++;
			if(count == 50){
				pressedSettings = false;
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
			buttonSettingsTexture = new Texture(Gdx.files.internal("ui/buttonSettings.png"));
			buttonExitTexture = new Texture(Gdx.files.internal("ui/buttonExit.png"));
		}
		batch.begin();
		batch.draw(buttonPlayTexture, Constants.VIEWPORT_WIDTH/2 - 142/2, 250);
		batch.draw(buttonSettingsTexture, Constants.VIEWPORT_WIDTH/2 - 226/2, 175);
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
