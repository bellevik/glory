package se.glory.zombieworld.screens;

import java.util.HashMap;

import se.glory.zombieworld.utilities.Constants;
import se.glory.zombieworld.utilities.Score;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class HighscoreScreen implements Screen {
	private Stage stage;
	private SpriteBatch batch;
	private Image highscoreLabel, backButton;
	
	private Texture backgroundTexture;
	
	// TODO Move the font to a separate class
	BitmapFont font = new BitmapFont(Gdx.files.internal("font/scoreFont.fnt"),
			Gdx.files.internal("font/scoreFont_0.png"), false);

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(255, 255, 255, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		
		batch.begin();
		batch.draw(backgroundTexture, 0, 0);
		font.draw(batch, "1.", Constants.VIEWPORT_WIDTH/3, 350);
		font.draw(batch, "2.", Constants.VIEWPORT_WIDTH/3, 320);
		font.draw(batch, "3.", Constants.VIEWPORT_WIDTH/3, 290);
		font.draw(batch, "4.", Constants.VIEWPORT_WIDTH/3, 260);
		font.draw(batch, "5.", Constants.VIEWPORT_WIDTH/3, 230);
		batch.end();
	
		for (int i = 0; i < 5; i++) {
			batch.begin();
			font.draw(batch, Score.getHighscoreAtPosition(i), Constants.VIEWPORT_WIDTH/3 + 100, 350 - (30 * i));
			batch.end();
		}
		
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		// TODO resize as in game screen
	}

	@Override
	public void show() {
		backgroundTexture = new Texture(Gdx.files.internal("ui/highscoreBackground.png"));
		
		stage = new Stage();
		batch = new SpriteBatch();
		
		Gdx.input.setInputProcessor(stage);
		
		backButton = new Image(new Texture(Gdx.files.internal("ui/buttonBack.png")));
		backButton.setX(Constants.VIEWPORT_WIDTH/2 - backButton.getWidth()/2);
		backButton.setY(20);
		
		backButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu());
			}
		});

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
