package se.glory.zombieworld.view;

import java.util.ArrayList;

import se.glory.zombieworld.model.StageModel;
import se.glory.zombieworld.model.WorldModel;
import se.glory.zombieworld.model.entities.Creature;
import se.glory.zombieworld.model.entities.items.WeaponLoot;
import se.glory.zombieworld.model.entities.obstacles.CustomObstacle;
import se.glory.zombieworld.utilities.Animator;
import se.glory.zombieworld.utilities.Constants;
import se.glory.zombieworld.utilities.Constants.MoveableBodyType;
import se.glory.zombieworld.utilities.Identity;
import se.glory.zombieworld.utilities.Point;
import se.glory.zombieworld.utilities.Score;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
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
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class GameView {
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private OrthogonalTiledMapRenderer mapRenderer;
	private Box2DDebugRenderer debugRenderer;
	private TiledMap map;

	private Animator animator;
	private String isOpen = "closed";

	private ArrayList<Point> doors = new ArrayList<Point>();

	// TODO Move the font to a separate class
	BitmapFont font = new BitmapFont(Gdx.files.internal("font/scoreFont.fnt"),
			Gdx.files.internal("font/scoreFont_0.png"), false);

	private float angle;

	public GameView(SpriteBatch batch) {
		this.batch = batch;
		debugRenderer = new Box2DDebugRenderer();
		camera = new OrthographicCamera();

		map = new TmxMapLoader().load("img/tilemap/theWorld.tmx");
		mapRenderer = new OrthogonalTiledMapRenderer(map);

		animator = new Animator();

		doors.add(new Point(46, 36));
	}

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

		// TODO: Test enable blending
		batch.enableBlending();

		mapRenderer.setView(camera);
		mapRenderer.render();

		camera.position.set(WorldModel.player.getBody().getPosition().x * Constants.BOX_TO_WORLD, WorldModel.player.getBody().getPosition().y * Constants.BOX_TO_WORLD, 0);
		camera.update();

		batch.setProjectionMatrix(camera.combined);

		if (Constants.DEBUG_MODE){
			useDebugRenderer();
		}

		camera.position.set(WorldModel.player.getBody().getPosition().x * Constants.BOX_TO_WORLD, WorldModel.player.getBody().getPosition().y * Constants.BOX_TO_WORLD, 0);
		camera.update();

		batch.setProjectionMatrix(camera.combined);

		// Draw textures
		drawEntites();

		//drawTextures();

		// Draw certain map layers on top of player
		mapRenderer.getSpriteBatch().begin();
		if (!getMapLayer("roof").isVisible()) {
			mapRenderer.renderTileLayer(getMapLayer("overlay"));
		} else {
			mapRenderer.renderTileLayer(getMapLayer("roof"));
		}
		mapRenderer.getSpriteBatch().end();

		animate();

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
		font.setColor(Color.RED);
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
			//if (body.getUserData() != null && body.getUserData().getClass().equals(Identity.class)) {
				Identity identity = (Identity) body.getUserData();
					//System.out.println(identity.getType() == Constants.MoveableBodyType.STREETOBJECT);
					if(identity.getType() == MoveableBodyType.STREETOBJECT ){
						Texture texture = identity.getTexture();
						//System.out.println(body.getPosition().x + " : " + body.getPosition().y);
						batch.begin();
						batch.draw(texture, body.getPosition().x*Constants.BOX_TO_WORLD - texture.getWidth()/2, body.getPosition().y*Constants.BOX_TO_WORLD - texture.getHeight()/15);
						batch.end();
					}
					float width = identity.getWidth();
					float height = identity.getHeight();

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
						animator.drawAnimation(batch, body.getPosition().x, body.getPosition().y, animator.getAnimation(MoveableBodyType.WEAPON, 0), true);
					}
			}

			WorldModel.drawableBodies.clear();
		
	}

	/*
	 * Draws the animation of an door opening when the player is 
	 * entering or exiting a house
	 */
	public void animate() {
		for (Point p: doors) {
			float realX = p.getX() * 16 / Constants.BOX_TO_WORLD;
			float realY = p.getY() * 16 / Constants.BOX_TO_WORLD;

			Animation animation = null;
			Texture closedDoor = null;

			// Gets the animation for an opening door
			animation = animator.getAnimation(MoveableBodyType.DOOR, 2);
			// Gets the texture for a closed door
			closedDoor = animator.getClosedDoor();

			if (animation != null){
				if(isOpen.equals("opening")){
					// Draw an animation of an opening door
					animator.drawAnimation(batch, realX, realY, animation, true);
				} else if(isOpen.equals("open")){
					// Draw an animation of a completely open door
					animator.drawAnimation(batch, realX, realY, animation, false);
				} else if(isOpen.equals("closed")) {
					// Draw a texture of a closed door
					batch.begin();
					batch.draw(closedDoor, realX*Constants.BOX_TO_WORLD - closedDoor.getWidth()/2, realY*Constants.BOX_TO_WORLD - closedDoor.getHeight()/2 );
					batch.end();
				}
			}
		}
		
		//Bad fix to make it change position depending on players position since it is not
		//an actor and has to be placed as part of the world
		if(WorldModel.player.getInfectedHealthTimer() != null) {
			Animation infectedAnimation = animator.getAnimation(MoveableBodyType.INFECTEDBAR, 0);
			animator.drawAnimation(batch, (float)(WorldModel.player.getTileX()*16+Constants.VIEWPORT_WIDTH*0.03)*Constants.WORLD_TO_BOX, (float)(WorldModel.player.getTileY()*16-Constants.VIEWPORT_HEIGHT*0.34)*Constants.WORLD_TO_BOX, infectedAnimation, true);
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