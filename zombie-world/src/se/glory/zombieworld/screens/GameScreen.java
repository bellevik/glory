package se.glory.zombieworld.screens;

import java.util.ArrayList;

import se.glory.entities.Human;
import se.glory.entities.Zombie;
import se.glory.utilities.Constants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

public class GameScreen implements Screen {
	
	private Box2DDebugRenderer debugRenderer;
	private OrthographicCamera camera;
	
	private World world;
	
	private ArrayList<Human> humans = new ArrayList<Human>();
	private ArrayList<Zombie> zombies = new ArrayList<Zombie>();

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		
		Matrix4 cameraCopy = camera.combined.cpy();
		debugRenderer.render(world, cameraCopy.scl(Constants.BOX_TO_WORLD));
		
		for (Zombie z : zombies) {
			z.autoUpdateMovement(humans);
		}
		
		for (Human h : humans) {
			h.autoUpdateMovement(zombies);
		}
		
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
		debugRenderer = new Box2DDebugRenderer();
		
		humans.add(new Human(world, 250, 200));
		
		zombies.add(new Zombie(world, 200, 50));
		zombies.add(new Zombie(world, 310, 280));
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
