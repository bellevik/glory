package se.glory.zombieworld.screens;

import se.glory.zombieworld.utilities.Constants;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

public class SettingsScreen implements Screen {
	private Stage stage;
	
	private SpriteBatch batch;
	
	private Texture backgroundTexture, SFXLabelTexture, musicLabelTexture, backButtonTexture, sliderBarTexture, sliderKnobTexture;
	private Image SFXLabel, musicLabel, backButton;
	
	private Slider musicVolume, SFXVolume;
	
	private Drawable sliderBar, sliderKnob;
	
	private Sprite sliderBarSprite, sliderKnobSprite;
	
	private BitmapFont font;
	
	private CharSequence SFXValue, musicValue;
    
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(255, 255, 255, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		SFXValue = (int) SFXVolume.getVisualValue() + "%";
		musicValue = (int) musicVolume.getVisualValue() + "%";
		
		batch.begin();
		batch.draw(backgroundTexture, 0, 0);
		font.draw(batch, musicValue, musicVolume.getX() + musicVolume.getWidth() + 10, 300);
		font.draw(batch, SFXValue, SFXVolume.getX() + SFXVolume.getWidth() + 10, 250);
		batch.end();
	
		
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void show() {
		
		font = new BitmapFont();
		
		stage = new Stage();
		
		batch = new SpriteBatch();
		
		backgroundTexture = new Texture(Gdx.files.internal("ui/mainMenuBackground.png"));
		musicLabelTexture = new Texture(Gdx.files.internal("ui/buttonMusic.png"));
		SFXLabelTexture = new Texture(Gdx.files.internal("ui/buttonSFX.png"));
		backButtonTexture = new Texture(Gdx.files.internal("ui/buttonBack.png"));
		
		sliderBarTexture = new Texture(Gdx.files.internal("ui/sliderBar.png"));
		sliderKnobTexture = new Texture(Gdx.files.internal("ui/sliderKnob.png"));
		
		sliderBarSprite = new Sprite(sliderBarTexture);
		sliderKnobSprite = new Sprite(sliderKnobTexture);
		
		
		sliderBar = new SpriteDrawable(sliderBarSprite);
		sliderKnob = new SpriteDrawable(sliderKnobSprite);
		
		Gdx.input.setInputProcessor(stage);
		
		//Create buttons
		musicLabel = new Image(musicLabelTexture);
		musicLabel.setX(Constants.VIEWPORT_WIDTH/4 - musicLabelTexture.getWidth()/4);
		musicLabel.setY(250);
		
		musicVolume = new Slider(0, 100, 1, false, new Slider.SliderStyle(sliderBar, sliderKnob));
		musicVolume.setX(Constants.VIEWPORT_WIDTH/2 - musicVolume.getWidth()/2);
		musicVolume.setY(250 + musicVolume.getHeight());
		musicVolume.setValue(100);
		
		
		SFXLabel = new Image(SFXLabelTexture);
		SFXLabel.setX(Constants.VIEWPORT_WIDTH/4 - SFXLabelTexture.getWidth()/4);
		SFXLabel.setY(200);
		
		SFXVolume = new Slider(0, 100, 1, false, new Slider.SliderStyle(sliderBar, sliderKnob));
		SFXVolume.setX(Constants.VIEWPORT_WIDTH/2 - SFXVolume.getWidth()/2);
		SFXVolume.setY(200 + SFXVolume.getHeight());
		SFXVolume.setValue(100);
		
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
		
		
		stage.addActor(SFXVolume);
		stage.addActor(musicVolume);
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
