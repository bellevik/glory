package se.glory.zombieworld.view;

import se.glory.zombieworld.model.WorldModel;
import se.glory.zombieworld.model.entities.Creature;
import se.glory.zombieworld.model.entities.items.WeaponLoot;
import se.glory.zombieworld.utilities.Animator;
import se.glory.zombieworld.utilities.Constants;
import se.glory.zombieworld.utilities.Constants.MoveableBodyType;
import se.glory.zombieworld.utilities.Identity;
import se.glory.zombieworld.utilities.Score;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
	
	private Animator animator;
	private String isOpen = "closed";
	
	// TODO: Change font
	/*BitmapFont font = new BitmapFont(Gdx.files.internal("font/scoreFOnt.fnt"),
			Gdx.files.internal("font/scoreFont_0.png"), false);*/
	
	BitmapFont font = new BitmapFont();
	
	private float angle;
	
	public GameView(SpriteBatch batch) {
		this.batch = batch;
		debugRenderer = new Box2DDebugRenderer();
		camera = new OrthographicCamera();
		
		map = new TmxMapLoader().load("img/tilemap/theWorld.tmx");
		// map = new TmxMapLoader().load("img/tilemap/debug_16/map.tmx");

		mapRenderer = new OrthogonalTiledMapRenderer(map);
		
		animator = new Animator();
	}
	
	/*public TiledMapTileLayer getMapLayer(int i) {
		return (TiledMapTileLayer) map.getLayers().get(i);
	}*/
	
	public TiledMapTileLayer getMapLayer(String name) {
		return (TiledMapTileLayer) map.getLayers().get(name);
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
		Gdx.gl.glClearColor(0, 0, 0, 1);
		
		mapRenderer.setView(camera);
		mapRenderer.render();
		
		camera.position.set(WorldModel.player.getBody().getPosition().x * Constants.BOX_TO_WORLD, WorldModel.player.getBody().getPosition().y * Constants.BOX_TO_WORLD, 0);
		camera.update();
		
		if (WorldModel.player.getEquippedWeapon() != null) {
			batch.begin();
			font.draw(batch, "Ammo: " + WorldModel.player.getEquippedWeapon().getClips() + " " + WorldModel.player.getEquippedWeapon().getCurrentClipSize(), 200, 100);
			batch.end();
		}
		
		batch.setProjectionMatrix(camera.combined);
		
		if (Constants.DEBUG_MODE){
			useDebugRenderer();
		}
		
		camera.position.set(WorldModel.player.getBody().getPosition().x * Constants.BOX_TO_WORLD, WorldModel.player.getBody().getPosition().y * Constants.BOX_TO_WORLD, 0);
		camera.update();
		
		batch.setProjectionMatrix(camera.combined);
		
		// Debug: Always draw textures
		drawEntites();
		
		// Draw certain map layers on top of player
		mapRenderer.getSpriteBatch().begin();
		if (!getMapLayer("roof").isVisible()) {
			mapRenderer.renderTileLayer(getMapLayer("overlay"));
		} else {
			mapRenderer.renderTileLayer(getMapLayer("roof"));
		}
		mapRenderer.getSpriteBatch().end();
		
		animateDoor();

		//Draw the ammo for the equipped weapon on the screen
		if (WorldModel.player.getEquippedWeapon() != null) {
			drawLabelOnScreen("Ammo: " + WorldModel.player.getEquippedWeapon().getClips() + " " + WorldModel.player.getEquippedWeapon().getCurrentClipSize(), WorldModel.player.getBody().getPosition().x * Constants.BOX_TO_WORLD - 20, WorldModel.player.getBody().getPosition().y * Constants.BOX_TO_WORLD + Constants.VIEWPORT_HEIGHT / 2 - 30);
		}
		//Draw the score on the screen
		drawLabelOnScreen("Score : " + Score.currentScore, WorldModel.player.getBody().getPosition().x * Constants.BOX_TO_WORLD - 20, WorldModel.player.getBody().getPosition().y * Constants.BOX_TO_WORLD + Constants.VIEWPORT_HEIGHT / 2 - 10);
	}
	
	/*
	 * This methods draws labels on the screen
	 */
	public void drawLabelOnScreen(String label, float x, float y) {
		batch.begin();
		font.draw(batch, label, x, y);
		batch.end();
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
					
					//Check if the body is a creature
					if(identity.getObj() instanceof Creature){
						/*
						 * Calculates the angle each body is facing to display the
						 * correct animation.
						 */
						float T = body.getAngle();
						
						if (T > Math.PI)
							T = (float) (-2 * Math.PI + T);
						if (T < -Math.PI)
							T = (float) (2 * Math.PI + T);
						
						angle = T * MathUtils.radiansToDegrees;
						
						//Gets what type of creature the object is
						MoveableBodyType name = identity.getType();
						
						Animation ani = null;
						
						/*
						* Check to see in what direction the body is facing, and gets the 
						* appropriate animation.
						*/
						if(angle > -22  && angle <= 22){
							//Facing east
							ani = animator.getAnimation(name, 2);
						}else if(angle > 22 && angle <= 67){
							//Facing northeast
							ani = animator.getAnimation(name, 3);
						}else if(angle > 67 && angle <= 112){
							//Facing north
							ani = animator.getAnimation(name, 4);
						}else if(angle > 112 && angle <= 157){
							//Facing northwest
							ani = animator.getAnimation(name, 5);
						}else if(angle > 157 || angle <= -157){
							//Facing west
							ani = animator.getAnimation(name, 6);
						}else if(angle > -157 && angle <= -112){
							//Facing southwest
							ani = animator.getAnimation(name, 7);
						}else if(angle > -112 && angle <= -67){
							//Facing south
							ani = animator.getAnimation(name, 0);
						}else if(angle > -67 && angle <= -22){
							//Facing southeast
							ani = animator.getAnimation(name, 1);
						}
						
						if (ani != null) {
							animator.drawAnimation(batch, body.getPosition().x, body.getPosition().y, ani, ((Creature)identity.getObj()).isMoving());
						}
					} else if (identity.getObj() instanceof WeaponLoot) {
						animator.drawAnimation(batch, body.getPosition().x, body.getPosition().y, animator.getLootAnimation(), true);
					}
			}
		}
		
		WorldModel.drawableBodies.clear();
	}
	
	public void animateDoor(){
		Animation animation = null;
		Animation closedDoor = null;
		
		animation = animator.getDoorAnimation(0);
		closedDoor = animator.getClosedDoor();
		
		if (animation != null){
			if(isOpen.equals("opening")){
				animator.drawAnimation(batch, 7.35f, 5.7799997f, animation, true);
			} else if(isOpen.equals("open")){
				animator.drawAnimation(batch, 7.35f, 5.7799997f, animation, false);
			} else if(isOpen.equals("closed")) {
				animator.drawAnimation(batch,  7.35f, 5.7799997f, closedDoor, true);
			}
		}
	}
	
	public Animator getAnimator(){
		return this.animator;
	}
	
	public SpriteBatch getSpriteBatch(){
		return this.batch;
	}
	
	public String getOpen(){
		return this.isOpen;
	}
	
	public void setOpen(String isOpen){
		this.isOpen = isOpen;
	}
}