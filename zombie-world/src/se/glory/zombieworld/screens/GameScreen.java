package se.glory.zombieworld.screens;

import java.util.ArrayList;
import java.util.Iterator;

import se.glory.entities.Creature;
import se.glory.entities.Human;
import se.glory.entities.Player;
import se.glory.entities.Zombie;
import se.glory.utilities.Constants;
import se.glory.utilities.Identity;
import se.glory.utilities.Joystick;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
	
	private OrthographicCamera camera;
	private World world;
	private Player player;
	private SpriteBatch batch;
	private Texture bkg;
	private Joystick moveStick;
	private Joystick fireStick;
	
	private Box2DDebugRenderer debugRenderer;
	
	//Consider change of variable name. This array will contain
	//all the bodies in the world. And is used to draw them.
	private Array<Body> drawableBodies = new Array<Body>();
	
	private ArrayList<Zombie> zombies = new ArrayList<Zombie>();
	private ArrayList<Creature> humans = new ArrayList<Creature>();
	
	private Stage stage;
	
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		if(Constants.DEBUG_MODE){
			useDebugRenderer();
		}
		
		camera.position.set(player.getPlayerBody().getPosition().x * Constants.BOX_TO_WORLD, player.getPlayerBody().getPosition().y * Constants.BOX_TO_WORLD, 0);
		camera.update();
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(bkg, 0, 0);
		batch.end();
		
		drawEntites();
		
		for (Zombie z: zombies) {
			z.autoUpdateMovement(humans, player);
		}
		
		for (Creature h: humans) {
			((Human) h).autoUpdateMovement(zombies);
		}
		
		player.getPlayerBody().setLinearVelocity(moveStick.getTouchpad().getKnobPercentX() * 2, moveStick.getTouchpad().getKnobPercentY() * 2);
		
		stage.act(delta);
		stage.draw();
		
		world.step(1/60f, 6, 2);
	}
	
	/*
	 * This method will get all the Box2d bodies from the world and iterate
	 * through all of them. The method will grab the texture form every one
	 * and draw them to the screen.
	 * We figured this is easier than letting all of the Classes have
	 * its own draw method.
	 */
	public void drawEntites() {
		world.getBodies(drawableBodies);
		
		for (Body body : drawableBodies) {
			//The != null test is for test purposes at the moment.
			if (((Identity)(body.getUserData())).getTexture() != null) {
				float width = ((Identity)(body.getUserData())).getWidth();
				float height = ((Identity)(body.getUserData())).getHeight();
				batch.begin();
				batch.draw(((Identity)(body.getUserData())).getTexture(), body.getPosition().x * Constants.BOX_TO_WORLD - width , body.getPosition().y * Constants.BOX_TO_WORLD - height);
				batch.end();
			}
		}
		drawableBodies.clear();
	}
	
	public void useDebugRenderer (){
		Matrix4 matrix4 = new Matrix4(camera.combined);
		matrix4.scale(100f, 100f, 1f);
		debugRenderer.render(world, matrix4);
	}

	@Override
	public void resize(int width, int height) {
		camera.viewportWidth = width;
        camera.viewportHeight = height;
	}

	@Override
	public void show() {
		camera = new OrthographicCamera();
		world = new World(new Vector2(0, 0), true);
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true, batch);
		player = new Player (world, 300, 400, 32, 32);
		batch = new SpriteBatch();
		bkg = new Texture(Gdx.files.internal("img/bkg.png"));
		
		debugRenderer = new Box2DDebugRenderer();
		
		moveStick = new Joystick(stage, 15, 15);
		fireStick = new Joystick(stage, Gdx.graphics.getWidth() - 15 - 128, 15);
		
		for (int i = 1; i < 6; i++) {
			for (int j = 1; j < 4; j++) {
				zombies.add(new Zombie(world, i*200, j*200));
				System.out.println(i*200 + ":" + j*200);
			}
		}
		
		for (int i = 1; i < 6; i+=2) {
			for (int j = 1; j < 6; j+=2) {
				humans.add(new Human(world, i*150, j*150));
			}
		}
		
		Gdx.input.setInputProcessor(stage);
		attachContactListener();
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
		world.dispose();
		batch.dispose();
		stage.dispose();
		debugRenderer.dispose();
	}
	
	public void attachContactListener(){
		world.setContactListener(new ContactListener(){

			@Override
			public void beginContact(Contact contact) {
				Body a = contact.getFixtureA().getBody();
				Body b = contact.getFixtureB().getBody();
				String typeA = ((Identity)(a.getUserData())).getType();
				String typeB = ((Identity)(b.getUserData())).getType();
			}

			@Override
			public void endContact(Contact contact) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
				// TODO Auto-generated method stub
				
			}
		});
	}

}
