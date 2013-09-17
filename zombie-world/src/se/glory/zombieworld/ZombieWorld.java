package se.glory.zombieworld;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class ZombieWorld implements ApplicationListener {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture texture;
	private Rectangle ball;
	
	@Override
	public void create() {		
		
		
		camera = new OrthographicCamera();
	    camera.setToOrtho(false, 800, 480);
	    
	    batch = new SpriteBatch();
		texture = new Texture(Gdx.files.internal("img/Zombie.png"));
		
		
		ball = new Rectangle();
	    ball.x = 800 / 2 - 64 / 2;
	    ball.y = 20;
	    ball.width = 64;
	    ball.height = 64;
	}

	@Override
	public void dispose() {
		batch.dispose();
		texture.dispose();
	}

	@Override
	public void render() {		
		Gdx.gl.glClearColor(1, 0, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
	    camera.update();
	    
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(texture, ball.x, ball.y);
		batch.end();
		
		if(Gdx.input.isTouched()) {
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			ball.x = touchPos.x - 64 / 2;
		}
		
		if(Gdx.input.isKeyPressed(Keys.LEFT)) ball.x -= 200 * Gdx.graphics.getDeltaTime();
		if(Gdx.input.isKeyPressed(Keys.RIGHT)) ball.x += 200 * Gdx.graphics.getDeltaTime();
		
		if(ball.x < 0) ball.x = 0;
		if(ball.x > 800 - 64) ball.x = 800 - 64;
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
