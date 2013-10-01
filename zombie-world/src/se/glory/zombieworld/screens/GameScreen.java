package se.glory.zombieworld.screens;

import se.glory.entities.Player;
import se.glory.entities.obstacles.House;
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
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

public class GameScreen implements Screen {
	
	private TiledMapTileLayer collide;

	private House topWall;
	private House sideWall;
	
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
	
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		mapRenderer.setView(camera);
		mapRenderer.render();
		
		if(Constants.DEBUG_MODE){
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
		debugRenderer = new Box2DDebugRenderer();

		map = new TmxMapLoader().load("img/firstwall.tmx");
		//This line will import all the images that will be used multiple times
		TextureHandler.createTextures();

		collide =(TiledMapTileLayer) map.getLayers().get(0);

		MapProperties prop = map.getProperties();
		String MapWidth = prop.get("Width", String.class);
		String MapHeight = prop.get("Height", String.class);

		mapRenderer = new OrthogonalTiledMapRenderer(map);
		
		camera = new OrthographicCamera();
		WorldHandler.createWorld();
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true, batch);
		player = new Player (300, 400, 32, 32);
		batch = new SpriteBatch();
		
		createHouse(MapWidth,MapHeight);

		debugRenderer = new Box2DDebugRenderer();
		
		moveStick = new Joystick(stage, 15, 15, 128, 128, Constants.TouchpadType.MOVEMENT);
		fireStick = new Joystick(stage, Gdx.graphics.getWidth() - 15 - 128, 15, 128, 128, Constants.TouchpadType.FIRE);
		
		Gdx.input.setInputProcessor(stage);
		attachContactListener();
	}
	
	public void createHouse(String MapWidth,String MapHeight){
		float Width;
		float Height;
		for (int i=0;i<Integer.parseInt(MapWidth);i++){
			for (int j=0;j<Integer.parseInt(MapHeight);j++){
				if (collide.getCell(i, j).getTile().getProperties().containsKey("Blocked")){
					Width = Integer.parseInt(collide.getCell(i,j).getTile().getProperties().get("Width").toString());
					Height = Integer.parseInt(collide.getCell(i,j).getTile().getProperties().get("Height").toString());
					topWall = new House (i*16,(j+1)*16,Width*8,8,"img/Topwall2.png");
					sideWall = new House (i*16,(j+1)*16,8,Height*8,"img/Sidewall2.png");

				}
			}
		}
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
