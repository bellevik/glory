package se.glory.zombieworld.model.entities.obstacles;

import se.glory.zombieworld.utilities.Identity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class StreetObject extends CustomObstacle{
	Texture image;
	
	public StreetObject(String name, float x, float y){
		super(x,y,5f);
		Identity identity = new Identity();
		
		if (name.equals("LAMP")){
			image =new Texture(Gdx.files.internal("img/streettextures/streetlight.png"));		
		}
		/*switch (type){
		case LAMP:
			image =new Texture(Gdx.files.internal("img/streettextures/streetlight.png"));
			break;
		case TREE:
			break;
		case PARKBENCH:			
						
			break;*/
			
		identity.setTexture(image);
	}
		
}