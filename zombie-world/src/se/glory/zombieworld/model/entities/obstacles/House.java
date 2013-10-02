package se.glory.zombieworld.model.entities.obstacles;

import se.glory.zombieworld.utilities.Constants;
import se.glory.zombieworld.utilities.Identity;
import se.glory.zombieworld.utilities.WorldHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class House {
	
	private Body houseBody;
	private BodyDef houseDef;
	private Texture texture;
	private float width;
	private float height;
	PolygonShape houseShape;
	

	public House (float x, float y, float width, float height, String texture){
		this.width=width;
		this.height=height;
		
		houseDef = new BodyDef();
		houseDef.type=BodyType.StaticBody;
		houseDef.position.set(new Vector2((x+width)*Constants.WORLD_TO_BOX,(y-height)*Constants.WORLD_TO_BOX));
		//houseDef.position.set(x, y);
		houseBody= WorldHandler.world.createBody(houseDef);
		
		houseShape= new PolygonShape();
		houseShape.setAsBox(width*Constants.WORLD_TO_BOX, height*Constants.WORLD_TO_BOX);
		//System.out.println("Skapats" + x + " , " + y);
		
		FixtureDef fixtureDef= new FixtureDef();
		fixtureDef.shape=houseShape;
		houseBody.createFixture(fixtureDef);
		
		
		Identity identity = new Identity();
		identity.setWidth(width);
		identity.setHeight(height);
		identity.setType(Constants.MoveableBodyType.HOUSE);
		
		houseBody.setUserData(identity);
		
	}
	
	public Body getHouseBody(){
		return houseBody;
	}
	
	
	
	public float getWidth(){
		return width;
	}
	
	public float getHeight(){
		return height;
	}
	
}
