package se.glory.zombieworld.model.entities.obstacles;

import se.glory.zombieworld.model.entities.Identity;
import se.glory.zombieworld.utilities.Constants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class StreetObject extends CustomObstacle{
	private Texture image;

	public StreetObject(String name, float x, float y){
		super();
		
		if (name.equals("LAMP")){
			image =new Texture(Gdx.files.internal("img/streettextures/streetlight.png"));
			super.setCircle(x, y, 5f);
		} else if (name.equals("PARKBENCH")){
			image =new Texture(Gdx.files.internal("img/streettextures/parkBench.png"));
			super.setBox(x, y, image.getWidth(), image.getHeight());
		}
			
		Identity identity = new Identity();
		identity.setType(Constants.MoveableBodyType.STREETOBJECT);
		identity.setTexture(image);
		identity.setObj(this);
		
		super.getBody().setUserData(identity);
	}
		
}
