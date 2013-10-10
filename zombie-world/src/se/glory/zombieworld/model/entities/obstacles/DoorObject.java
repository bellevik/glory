package se.glory.zombieworld.model.entities.obstacles;

import se.glory.zombieworld.utilities.Constants;
import se.glory.zombieworld.utilities.Identity;

public class DoorObject extends CustomObstacle {
	
	public DoorObject(float x, float y, float width, float height) {
		super(x, y, width, height);
		
		Identity identity = new Identity();
		identity.setWidth(width);
		identity.setHeight(height);
		identity.setType(Constants.MoveableBodyType.DOOR);
		identity.setObj(this);
		
		body.setUserData(identity);
	}

}
