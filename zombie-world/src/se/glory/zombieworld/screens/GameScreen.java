package se.glory.zombieworld.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class GameScreen implements Screen {
	
	private OrthographicCamera camera;
	private World world;

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		
		world.step(1/60f, 6, 2);
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void show() {
		camera = new OrthographicCamera();
		camera.setToOrtho(false);
		world = new World(new Vector2(0, 0), true);
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
