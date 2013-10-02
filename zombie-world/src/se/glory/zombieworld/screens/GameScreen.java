package se.glory.zombieworld.screens;

import java.awt.Point;
import java.util.ArrayList;

import se.glory.entities.Player;
import se.glory.entities.ai.AIController;
import se.glory.utilities.CollisionDetection;
import se.glory.utilities.Constants;
import se.glory.utilities.Identity;
import se.glory.utilities.Joystick;
import se.glory.utilities.TextureHandler;
import se.glory.utilities.WorldHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class GameScreen implements Screen {
	private TiledMapTileLayer collideLayer;
	
	private OrthogonalTiledMapRenderer mapRenderer;
	private TiledMap map;
	
	private OrthographicCamera camera;
	private Player player;
	private SpriteBatch batch;
	private Joystick moveStick;
	private Joystick fireStick;
	
	private Box2DDebugRenderer debugRenderer;
	
	private Stage stage;
	private float timeStamp = 0;
	
	private AIController aic = new AIController();
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		mapRenderer.setView(camera);
		mapRenderer.render();
		
		if (Constants.DEBUG_MODE){
			useDebugRenderer();
		} else {
			drawEntites();
		}
		
		
		
		camera.position.set(player.getBody().getPosition().x * Constants.BOX_TO_WORLD, player.getBody().getPosition().y * Constants.BOX_TO_WORLD, 0);
		camera.update();
		
		batch.setProjectionMatrix(camera.combined);
		
		player.getBody().setLinearVelocity(moveStick.getTouchpad().getKnobPercentX() * 2, moveStick.getTouchpad().getKnobPercentY() * 2);
		
		//-------------REFACTOR THIS METHOD!-------------
		applyRotationToPlayer(delta);
		
		stage.act(delta);
		stage.draw();
		
		WorldHandler.world.step(1/60f, 6, 2);
		sweepDeadBodies();
		
		aic.update();
	}
	
	public void applyRotationToPlayer(float delta) {
		timeStamp += delta;
		
		//-------RECHECK THIS CONDITION. IT WILL STOP AT x=0 and y=0. i.e MOVE
		//-------STRAIGHT TO THE RIGHT
		if (fireStick.getTouchpad().getKnobPercentX() != 0 && fireStick.getTouchpad().getKnobPercentY() != 0) {
			
			float knobX = fireStick.getTouchpad().getKnobPercentX();
			float knobY = fireStick.getTouchpad().getKnobPercentY();
			
			float playerDegree = (int) (player.getBody().getTransform().getRotation() * MathUtils.radiansToDegrees);
			float knobDegree, totalRotation;
			
			if (knobY >= 0) {
				knobDegree = (int) (Math.acos(knobX) * MathUtils.radiansToDegrees);
			} else {
				knobDegree = -(int) (Math.acos(knobX) * MathUtils.radiansToDegrees);
			}
			totalRotation = knobDegree - playerDegree;
			
			player.getBody().setTransform(player.getBody().getPosition(), knobDegree * MathUtils.degreesToRadians);
			player.getBody().getJointList().get(0).joint.getBodyB().setTransform(player.getBody().getJointList().get(0).joint.getBodyB().getPosition(), knobDegree * MathUtils.degreesToRadians);
			player.getBody().getJointList().get(0).joint.getBodyB().setAwake(true);
			
			if(timeStamp > 1) {
				player.shoot();
				timeStamp = 0;
			}
		} else {
			if (moveStick.getTouchpad().getKnobPercentX() != 0 && moveStick.getTouchpad().getKnobPercentY() != 0) {
				float knobX = moveStick.getTouchpad().getKnobPercentX();
				float knobY = moveStick.getTouchpad().getKnobPercentY();
				
				float playerDegree = (int) (player.getBody().getTransform().getRotation() * MathUtils.radiansToDegrees);
				float knobDegree, nextAngle, totalRotation;
				
				if (knobY >= 0) {
					knobDegree = (int) (Math.acos(knobX) * MathUtils.radiansToDegrees);
				} else {
					knobDegree = -(int) (Math.acos(knobX) * MathUtils.radiansToDegrees);
				}
				totalRotation = knobDegree - playerDegree;
				
				player.getBody().setTransform(player.getBody().getPosition(), knobDegree * MathUtils.degreesToRadians);
				player.getBody().getJointList().get(0).joint.getBodyB().setTransform(player.getBody().getJointList().get(0).joint.getBodyB().getPosition(), knobDegree * MathUtils.degreesToRadians);
				player.getBody().getJointList().get(0).joint.getBodyB().setAwake(true);
			}
		}
	}
	
	/*
	 * This method will get all the Box2d bodies from the world and iterate
	 * through all of them. The method will grab the texture form every one
	 * and draw them to the screen.
	 * We figured this is easier than letting all of the Classes have
	 * its own draw method.
	 */
	public void drawEntites() {
		WorldHandler.world.getBodies(WorldHandler.drawableBodies);
		
		for (Body body : WorldHandler.drawableBodies) {
			if ( body.getUserData().getClass().equals(Identity.class) ) {
				float width = ((Identity)(body.getUserData())).getWidth();
				float height = ((Identity)(body.getUserData())).getHeight();
				batch.begin();
				batch.draw(((Identity)(body.getUserData())).getTexture(), body.getPosition().x * Constants.BOX_TO_WORLD - width , body.getPosition().y * Constants.BOX_TO_WORLD - height);
				batch.end();
			}
		}
		WorldHandler.drawableBodies.clear();
	}
	
	public void sweepDeadBodies() {
		WorldHandler.world.getBodies(WorldHandler.removeableBodies);
		
		for (Body body : WorldHandler.removeableBodies) {
			if ( body.getUserData().getClass().equals(Identity.class) ) {
				if(body!=null) {
					if (((Identity)(body.getUserData())).isDead()) {
						if (!WorldHandler.world.isLocked()) {
							WorldHandler.world.destroyBody(body);
						}
					}
				}
			}
		}
		WorldHandler.removeableBodies.clear();
	}
	
	public void useDebugRenderer (){
		Matrix4 matrix4 = new Matrix4(camera.combined);
		matrix4.scale(100f, 100f, 1f);
		debugRenderer.render(WorldHandler.world, matrix4);
	}

	@Override
	public void resize(int width, int height) {
		camera.viewportWidth = width;
        camera.viewportHeight = height;
	}

	@Override
	public void show() {
		//This line will import all the images that will be used multiple times
		TextureHandler.createTextures();

		// ###
		map = new TmxMapLoader().load("img/tileset/AITestMap/AITestMap.tmx");
		mapRenderer = new OrthogonalTiledMapRenderer(map);
		
		collideLayer = (TiledMapTileLayer) map.getLayers().get("collision");
		// ###
		
		WorldHandler.createWorld();
		camera = new OrthographicCamera();
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true, batch);
		player = new Player (300, 400, 16, 16);
		batch = new SpriteBatch();

		debugRenderer = new Box2DDebugRenderer();
		
		moveStick = new Joystick(stage, 15, 15);
		fireStick = new Joystick(stage, Gdx.graphics.getWidth() - 15 - 128, 15);
		
		Gdx.input.setInputProcessor(stage);
		attachContactListener();
		
		// TODO: Refactor?
		setupAIController();
		
		aic.addHuman(49, 81);
		aic.addHuman(177, 81);
		aic.addHuman(283, 209);
	}
	
	private void setupAIController() {
		ArrayList<Point> blockedTiles = new ArrayList<Point>();
		
		for (int x = 0; x < collideLayer.getWidth(); x++) {
			for (int y = 0; y < collideLayer.getHeight(); y++) {
				Cell c = collideLayer.getCell(x, y);
				if (c != null)
					blockedTiles.add(new Point(x, y));
			}
		}
		
		aic.setBlockedTiles(blockedTiles);
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
		WorldHandler.world.dispose();
		batch.dispose();
		stage.dispose();
		debugRenderer.dispose();
	}
	
	public void attachContactListener(){
		WorldHandler.world.setContactListener(new CollisionDetection());
	}
	
}
