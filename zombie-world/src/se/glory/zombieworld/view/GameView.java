package se.glory.zombieworld.view;

import se.glory.zombieworld.model.WorldModel;
import se.glory.zombieworld.model.entities.Creature;
import se.glory.zombieworld.utilities.Animator;
import se.glory.zombieworld.utilities.Constants;
import se.glory.zombieworld.utilities.Identity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

public class GameView {
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private OrthogonalTiledMapRenderer mapRenderer;
	private Box2DDebugRenderer debugRenderer;
	private TiledMap map;
	
	private float angle, prevAngle;
	
	public GameView(SpriteBatch batch) {
		this.batch = batch;
		debugRenderer = new Box2DDebugRenderer();
		camera = new OrthographicCamera();
		
		map = new TmxMapLoader().load("img/tilemap/map.tmx");
		mapRenderer = new OrthogonalTiledMapRenderer(map);
	}
	
	public TiledMapTileLayer getMapLayer(int i) {
		return (TiledMapTileLayer) map.getLayers().get(i);
	}
	
	public void useDebugRenderer() {
		Matrix4 matrix4 = new Matrix4(camera.combined);
		matrix4.scale(100f, 100f, 1f);
		
		debugRenderer.render(WorldModel.world, matrix4);
	}
	
	public void dispose() {
		batch.dispose();
		debugRenderer.dispose();
	}
	
	public Camera getCamera() {
		return camera;
	}
	
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(0.5f, 1, 1, 1);
		
		mapRenderer.setView(camera);
		mapRenderer.render();
		
		camera.position.set(WorldModel.player.getBody().getPosition().x * Constants.BOX_TO_WORLD, WorldModel.player.getBody().getPosition().y * Constants.BOX_TO_WORLD, 0);
		camera.update();
		
		batch.setProjectionMatrix(camera.combined);
		
		if (Constants.DEBUG_MODE){
			useDebugRenderer();
		} else {
			drawEntites();
		}
		
		// Debug: Always draw textures
		drawEntites();
	}
	
	/*
	 * This method will get all the Box2d bodies from the world and iterate
	 * through all of them. The method will grab the texture form every one
	 * and draw them to the screen.
	 * We figured this is easier than letting all of the Classes have
	 * its own draw method.
	 */
	public void drawEntites() {
		WorldModel.world.getBodies(WorldModel.drawableBodies);
		
		for (Body body : WorldModel.drawableBodies) {
			if (body.getUserData() != null && body.getUserData().getClass().equals(Identity.class)) {
				Identity identity = (Identity) body.getUserData();
				
				if (identity.getTexture() != null) {
					float width = identity.getWidth();
					float height = identity.getHeight();
					

					//Check if the body is a creature
					if(identity.getObj() instanceof Creature){
						angle = body.getAngle()*MathUtils.radiansToDegrees;

						/*
						* Check to see in what direction the body is facing, and create an animation if 
						* it's different from the previous direction. 
						*/
						
						if(angle > -22  && angle <= 22 && (prevAngle <-22 || prevAngle > 22)){
							Animator.createAnimation("SpriteSheetMain.png", 2);
						}else if(angle > 22 && angle <= 67 && (prevAngle < 22 || prevAngle > 67)){
							Animator.createAnimation("SpriteSheetMain.png", 3);
						}else if(angle > 67 && angle <= 112 && (prevAngle < 67 || prevAngle > 112)){
							Animator.createAnimation("SpriteSheetMain.png", 4);
						}else if(angle > 112 && angle <= 157 && (prevAngle < 112 || prevAngle > 157)){
							Animator.createAnimation("SpriteSheetMain.png", 5);
						}else if(angle > 157 && angle <= -157 && (prevAngle < 157 || prevAngle > -157)){
							Animator.createAnimation("SpriteSheetMain.png", 6);
						}else if(angle > -157 && angle <= -112 && (prevAngle < -157 || prevAngle > -112)){
							Animator.createAnimation("SpriteSheetMain.png", 7);
						}else if(angle > -112 && angle <= -67 && (prevAngle < -112 || prevAngle > -67)){
							Animator.createAnimation("SpriteSheetMain.png", 0);
						}else if(angle > -67 && angle <= -22 && (prevAngle < -67 || prevAngle > -22)){
							Animator.createAnimation("SpriteSheetMain.png", 1);
						}
						
						//Draw the animation if it exists
						if(Animator.getAnimation() != null)
							Animator.drawAnimation(batch, body.getPosition().x, body.getPosition().y);
						
						//Save the previous angle
						prevAngle = angle;
					}
					
					batch.begin();
					//batch.draw(identity.getTexture(), body.getPosition().x * Constants.BOX_TO_WORLD - width, body.getPosition().y * Constants.BOX_TO_WORLD - height);
					batch.end();
				}
			}
		}
		
		WorldModel.drawableBodies.clear();
	}
}