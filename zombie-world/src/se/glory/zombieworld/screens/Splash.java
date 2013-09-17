package se.glory.zombieworld.screens;

import se.glory.zombieworld.tween.SpriteAccessor;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Splash implements Screen {

	
	private Sprite splash;
	private SpriteBatch batch;
	private TweenManager tweenManager;
	
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(66, 139, 17, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		tweenManager.update(delta);
		
		batch.begin();
		splash.draw(batch);
		batch.end();
		
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void show() {
		batch = new SpriteBatch();
		tweenManager = new TweenManager();
		Tween.registerAccessor(Sprite.class, new SpriteAccessor());
		
		Texture spriteTexture = new Texture(Gdx.files.internal("data/splashScreen.png"));
		splash = new Sprite(spriteTexture);
		splash.setSize(Gdx.graphics.getWidth() , Gdx.graphics.getHeight());
		
		Tween.set(splash, SpriteAccessor.ALPHA).target(0).start(tweenManager);
		Tween.to(splash, SpriteAccessor.ALPHA, 1).target(1).repeatYoyo(1, 1).setCallback(new TweenCallback(){
			@Override
			public void onEvent(int type, BaseTween<?> source){
				((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu());
			}
		}).start(tweenManager);
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
		batch.dispose();
		splash.getTexture().dispose();
	}

}
