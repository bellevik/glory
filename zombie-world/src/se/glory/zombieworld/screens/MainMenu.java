package se.glory.zombieworld.screens;

import se.glory.zombieworld.utilities.Constants;
import se.glory.zombieworld.utilities.Score;

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
	
	private int yDiff = 75;
	
	private Texture backgroundTexture, buttonExitTexture, buttonPlayTexture, buttonSettingsTexture, buttonHighscoreTexture, buttonTutorialTexture;
	private Image buttonExit, buttonPlay, buttonSettings, buttonHighscore, buttonTutorial;
    
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
		double scale = Constants.VIEWPORT_WIDTH / (double) width;
		Constants.VIEWPORT_HEIGHT = (int) (height * scale);
	}

	@Override
	public void show() {
		
		stage = new Stage();
		
		batch = new SpriteBatch();
		
		backgroundTexture = new Texture(Gdx.files.internal("ui/mainMenuBackground.png"));
		buttonPlayTexture = new Texture(Gdx.files.internal("ui/buttonPlay.png"));
		buttonTutorialTexture = new Texture(Gdx.files.internal("ui/tutorialButton.png"));
		buttonSettingsTexture = new Texture(Gdx.files.internal("ui/buttonSettings.png"));
		buttonHighscoreTexture = new Texture(Gdx.files.internal("ui/highScoreLabel.png"));
		buttonExitTexture = new Texture(Gdx.files.internal("ui/buttonExit.png"));
		
		Gdx.input.setInputProcessor(stage);
		
		//Create buttons
		buttonPlay = new Image(buttonPlayTexture);
		buttonPlay.setX(Constants.VIEWPORT_WIDTH/2 - buttonPlayTexture.getWidth()/2);
		buttonPlay.setY(300);
		
		buttonTutorial = new Image(buttonTutorialTexture);
		buttonTutorial.setX(Constants.VIEWPORT_WIDTH/2 - buttonTutorialTexture.getWidth()/2);
		buttonTutorial.setY(buttonPlay.getY() - yDiff);
		
		
		buttonSettings = new Image(buttonSettingsTexture);
		buttonSettings.setX(Constants.VIEWPORT_WIDTH/2 - buttonSettingsTexture.getWidth()/2);
		buttonSettings.setY(buttonTutorial.getY() - yDiff);
		
		buttonHighscore = new Image(buttonHighscoreTexture);
		buttonHighscore.setX(Constants.VIEWPORT_WIDTH/2 - buttonHighscoreTexture.getWidth()/2);
		buttonHighscore.setY(buttonSettings.getY() - yDiff);
		
		buttonExit = new Image(buttonExitTexture);
		buttonExit.setX(Constants.VIEWPORT_WIDTH/2 - buttonExitTexture.getWidth()/2 - 10);
		buttonExit.setY(buttonHighscore.getY() - 65);

		//Adding listeners to buttons
		buttonPlay.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Score.resetScore();
				((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen());
			}
		});
		buttonTutorial.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				((Game) Gdx.app.getApplicationListener()).setScreen(new TutorialScreen());
			}
		});
		buttonSettings.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				((Game) Gdx.app.getApplicationListener()).setScreen(new SettingsScreen());
			}
		});
		buttonHighscore.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				((Game) Gdx.app.getApplicationListener()).setScreen(new HighscoreScreen());
			}
		});
		buttonExit.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.exit();
			}
		});
		
		stage.addActor(buttonPlay);
		stage.addActor(buttonTutorial);
		stage.addActor(buttonSettings);
		stage.addActor(buttonExit);
		stage.addActor(buttonHighscore);
	}

	@Override
	public void hide() {
		buttonPlay.clear();
		buttonTutorial.clear();
		buttonSettings.clear();
		buttonExit.clear();
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void dispose() {
		stage.dispose();
	}
}
