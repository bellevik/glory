package se.glory.zombieworld.screens;

import se.glory.zombieworld.utilities.Constants;
import se.glory.zombieworld.utilities.Score;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class GameOverScreen implements Screen {
	private Stage stage;
	private SpriteBatch batch;
	private Image highscoreLabel, backButton;
	
	private TextField textfield;
	
	private Texture backgroundTexture;
	
	private BitmapFont font;
	
	private Skin skin;

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(255, 255, 255, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		
		batch.begin();
		batch.draw(backgroundTexture, 0, 0);
		font.draw(batch, "Your score was: " + Score.currentScore + ".", Constants.VIEWPORT_WIDTH/2, 270);
		font.draw(batch, "You killed " + Score.zombiesKilled + " zombbies!", Constants.VIEWPORT_WIDTH/2, 250);
		font.draw(batch, "You killed " + Score.humansKilled + " humans!", Constants.VIEWPORT_WIDTH/2, 230);
		font.draw(batch, "You fired " + Score.shotsFired + " bullets!", Constants.VIEWPORT_WIDTH/2, 210);
		batch.end();
		
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		// TODO resize as in game screen
	}

	@Override
	public void show() {
		backgroundTexture = new Texture(Gdx.files.internal("ui/mainMenuBackground.png"));
		
		font = new BitmapFont();
		stage = new Stage();
		batch = new SpriteBatch();
		
		highscoreLabel = new Image(new Texture(Gdx.files.internal("ui/gameOverLabel.png")));
		highscoreLabel.setX(Constants.VIEWPORT_WIDTH/2 - highscoreLabel.getWidth()/2);
		highscoreLabel.setY(295);
		
		stage.addActor(highscoreLabel);
		
		backButton = new Image(new Texture(Gdx.files.internal("ui/buttonBack.png")));
		backButton.setX(Constants.VIEWPORT_WIDTH/2 - backButton.getWidth()/2);
		backButton.setY(20);
		
		stage.addActor(backButton);
		
		backButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu());
			}
		});
		
		
		Gdx.input.setInputProcessor(stage);		
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
