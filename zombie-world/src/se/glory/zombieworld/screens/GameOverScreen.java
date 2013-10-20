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
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

public class GameOverScreen implements Screen {
	private Stage stage;
	private SpriteBatch batch;
	private Image highscoreLabel, backButton;
	
	private TextField textfield;
	
	private Texture backgroundTexture;
	
	// TODO Move the font to a separate class
	BitmapFont font = new BitmapFont(Gdx.files.internal("font/scoreFont.fnt"),
			Gdx.files.internal("font/scoreFont_0.png"), false);
	
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
		
		stage = new Stage();
		batch = new SpriteBatch();
		
		highscoreLabel = new Image(new Texture(Gdx.files.internal("ui/gameOverLabel.png")));
		highscoreLabel.setX(Constants.VIEWPORT_WIDTH/2 - highscoreLabel.getWidth()/2);
		highscoreLabel.setY(295);
		
		backButton = new Image(new Texture(Gdx.files.internal("ui/buttonBack.png")));
		backButton.setX(Constants.VIEWPORT_WIDTH/2 - backButton.getWidth()/2);
		backButton.setY(20);
		
		Texture texture = new Texture(Gdx.files.internal("ui/textfieldBackground.png"));
		Sprite sprite = new Sprite(texture);
		SpriteDrawable spDraw = new SpriteDrawable(sprite);
		
		TextFieldStyle textfieldStyle = new TextFieldStyle();
		textfieldStyle.background = spDraw;
		textfieldStyle.fontColor = Color.BLACK;
		textfieldStyle.font = font;
		
		textfield = new TextField("Type your name", textfieldStyle);
		textfield.setSize(200, 30);
		textfield.setPosition(Constants.VIEWPORT_WIDTH/2 - textfield.getWidth()/2, 150);
		
		backButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu());
			}
		});

		stage.addActor(highscoreLabel);
		stage.addActor(backButton);
		stage.addActor(textfield);
		
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
