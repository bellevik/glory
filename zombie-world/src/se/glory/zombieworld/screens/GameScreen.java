package se.glory.zombieworld.screens;

import se.glory.entities.Player;
import se.glory.entities.items.ItemContainer;
import se.glory.entities.items.ItemSelection;
import se.glory.entities.items.WeaponLoot;
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
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.utils.Array;

public class GameScreen implements Screen {
	
	private TiledMapTileLayer collide;

	private House topWall;
	private House sideWall;
	
	private OrthogonalTiledMapRenderer mapRenderer;
	private TiledMap map;
	
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Joystick moveStick;
	private Joystick fireStick;
	
	private Joystick selectionStick;
	private float selectionX;
	private float selectionY;
	private ItemContainer[] itemContainers;
	private ItemSelection itemSelection;
	
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
		
		camera.position.set(WorldHandler.player.getBody().getPosition().x * Constants.BOX_TO_WORLD, WorldHandler.player.getBody().getPosition().y * Constants.BOX_TO_WORLD, 0);
		camera.update();
		
		batch.setProjectionMatrix(camera.combined);
		
		WorldHandler.player.getBody().setLinearVelocity(moveStick.getTouchpad().getKnobPercentX() * 2, moveStick.getTouchpad().getKnobPercentY() * 2);
		
		//-------------REFACTOR THIS METHOD!-------------
		applyRotationToPlayer(delta);
		selectWeapon();
		
		stage.act(delta);
		stage.draw();
		
		WorldHandler.world.step(1/60f, 6, 2);
		sweepDeadBodies();
	}
	
	/*
	 * This method will show or hide the quickselection as well as 
	 * handle which selections are made.
	 */
	public void selectWeapon() {
		if(selectionStick.getTouchpad().isTouched()) {
			if(!itemContainers[0].isActorVisible()) {
				for(int i = 0; i < itemContainers.length; i++) {
					itemContainers[i].show();
				}
			}
		} else {
			if(itemContainers[0].isActorVisible()) {
				for(int i = 0; i < itemContainers.length; i++) {
					itemContainers[i].hide();
				}
			}
		}
		
		if(selectionStick.getTouchpad().getKnobPercentX() != 0 || selectionStick.getTouchpad().getKnobPercentY() != 0) {
			float knobX = selectionStick.getTouchpad().getKnobPercentX();
			float knobY = selectionStick.getTouchpad().getKnobPercentY();
			
			float knobDegree;
			int selection = 5;
			
			if (knobY >= 0) {
				knobDegree = -(int) (Math.acos(knobX) * MathUtils.radiansToDegrees);
			} else {
				knobDegree = (int) (Math.acos(knobX) * MathUtils.radiansToDegrees);
			}
			
			if((knobDegree <= 0 && knobDegree > -30) || (knobDegree <= -150 && knobDegree > -180)) {
				knobDegree = Math.abs(knobDegree);
			}
			
			if (knobDegree >= 0 && knobDegree < 180) {
				selection = (int) (knobDegree / 36);
				
				itemSelection.setActorPosition(itemContainers[selection].getActorX(), itemContainers[selection].getActorY());
				
				if(!itemSelection.isActorVisible()) {
					itemSelection.show();
				}
			} else {
				selection = 5;
				if(itemSelection.isActorVisible()) {
					itemSelection.hide();
				}
			}
		} else {
			if(itemSelection.isActorVisible()) {
				itemSelection.hide();
			}
		}
	}
	
	/*
	 * This method will rotate the player according to what angle the touchpads got
	 */
	public void applyRotationToPlayer(float delta) {
		timeStamp += delta;
		
		//-------RECHECK THIS CONDITION. IT WILL STOP AT x=0 and y=0. i.e MOVE
		//-------STRAIGHT TO THE RIGHT
		if (fireStick.getTouchpad().getKnobPercentX() != 0 && fireStick.getTouchpad().getKnobPercentY() != 0) {
			
			float knobX = fireStick.getTouchpad().getKnobPercentX();
			float knobY = fireStick.getTouchpad().getKnobPercentY();
			
			float playerDegree = (int) (WorldHandler.player.getBody().getTransform().getRotation() * MathUtils.radiansToDegrees);
			float knobDegree, totalRotation;
			
			if (knobY >= 0) {
				knobDegree = (int) (Math.acos(knobX) * MathUtils.radiansToDegrees);
			} else {
				knobDegree = -(int) (Math.acos(knobX) * MathUtils.radiansToDegrees);
			}
			totalRotation = knobDegree - playerDegree;
			
			WorldHandler.player.getBody().setTransform(WorldHandler.player.getBody().getPosition(), knobDegree * MathUtils.degreesToRadians);
			WorldHandler.player.getBody().getJointList().get(0).joint.getBodyB().setTransform(WorldHandler.player.getBody().getJointList().get(0).joint.getBodyB().getPosition(), knobDegree * MathUtils.degreesToRadians);
			WorldHandler.player.getBody().getJointList().get(0).joint.getBodyB().setAwake(true);
			
			if(timeStamp > 1) {
				WorldHandler.player.shoot();
				timeStamp = 0;
			}
		} else {
			if (moveStick.getTouchpad().getKnobPercentX() != 0 && moveStick.getTouchpad().getKnobPercentY() != 0) {
				float knobX = moveStick.getTouchpad().getKnobPercentX();
				float knobY = moveStick.getTouchpad().getKnobPercentY();
				
				float playerDegree = (int) (WorldHandler.player.getBody().getTransform().getRotation() * MathUtils.radiansToDegrees);
				float knobDegree, nextAngle, totalRotation;
				
				if (knobY >= 0) {
					knobDegree = (int) (Math.acos(knobX) * MathUtils.radiansToDegrees);
				} else {
					knobDegree = -(int) (Math.acos(knobX) * MathUtils.radiansToDegrees);
				}
				totalRotation = knobDegree - playerDegree;
				
				WorldHandler.player.getBody().setTransform(WorldHandler.player.getBody().getPosition(), knobDegree * MathUtils.degreesToRadians);
				WorldHandler.player.getBody().getJointList().get(0).joint.getBodyB().setTransform(WorldHandler.player.getBody().getJointList().get(0).joint.getBodyB().getPosition(), knobDegree * MathUtils.degreesToRadians);
				WorldHandler.player.getBody().getJointList().get(0).joint.getBodyB().setAwake(true);
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
	
	/*
	 * This method will loop through all the bodies in the world and check if the
	 * body got the isDead boolean as true. If it is true then remove the body from
	 * the world. This needs to be done separately and after the world.step
	 * method. Otherwise libgdx will crash. 
	 */
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
		batch = new SpriteBatch();
		
		createHouse(MapWidth,MapHeight);
		WeaponLoot loot = new WeaponLoot("Bazooka", 14, 15);

		debugRenderer = new Box2DDebugRenderer();
		
		moveStick = new Joystick(stage, 15, 15, 128, 128, Constants.TouchpadType.MOVEMENT);
		fireStick = new Joystick(stage, Gdx.graphics.getWidth() - 15 - 128, 15, 128, 128, Constants.TouchpadType.FIRE);
		
		selectionX = (Gdx.graphics.getWidth() - 15 - 196);
		selectionY = (Gdx.graphics.getHeight() - 15 - 64);
		selectionStick = new Joystick(stage, selectionX, selectionY, 64, 64, Constants.TouchpadType.ITEM_SELECTION);
		
		itemContainers = new ItemContainer[5];
		itemContainers[4] = new ItemContainer(stage, selectionX - 16 - 64, selectionY);
		itemContainers[3] = new ItemContainer(stage, selectionX - 16 - 64, selectionY - 16 - 64);
		itemContainers[2] = new ItemContainer(stage, selectionX, selectionY - 16 - 64);
		itemContainers[1] = new ItemContainer(stage, selectionX + 16 + 64, selectionY - 16 - 64);
		itemContainers[0] = new ItemContainer(stage, selectionX + 16 + 64, selectionY);
		
		itemSelection = new ItemSelection(stage, selectionStick.getTouchpad().getX(), selectionStick.getTouchpad().getY());
		
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
