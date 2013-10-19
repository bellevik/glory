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

public class TutorialScreen implements Screen {
	
	private Stage stage;
	
	private SpriteBatch batch;

	private Texture firstTutorial, secondTutorial, thirdTutorial, nextTexture, currentTexture;
	private Image nextButton;
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(255, 255, 255, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		batch.draw(currentTexture, 0, 0);
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
		
		firstTutorial = new Texture(Gdx.files.internal("ui/tutorial1.png"));
		secondTutorial = new Texture(Gdx.files.internal("ui/tutorial2.png"));
		thirdTutorial = new Texture(Gdx.files.internal("ui/tutorial3.png"));
		nextTexture = new Texture(Gdx.files.internal("ui/nextLabel.png"));
		
		Gdx.input.setInputProcessor(stage);
		
		currentTexture = firstTutorial;
		
		nextButton = new Image(nextTexture);
		nextButton.setX(Constants.VIEWPORT_WIDTH/6);
		nextButton.setY((float) (Constants.VIEWPORT_HEIGHT - nextTexture.getHeight()*1.2));
		
		nextButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (currentTexture == firstTutorial){
					currentTexture = secondTutorial;
				} else if ( currentTexture == secondTutorial){
					currentTexture = thirdTutorial;
				} else if ( currentTexture == thirdTutorial){
					((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu());
				}
			}
		});
		
		stage.addActor(nextButton);
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
