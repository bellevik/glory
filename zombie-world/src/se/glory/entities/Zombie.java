package se.glory.entities;

import se.glory.utilities.Constants;
import se.glory.utilities.TextureHandler;

public class Zombie extends MoveableBody{
	
	public Zombie(int x, int y) {
		super(x, y, 15, 15, TextureHandler.zombieTexture, Constants.MoveableBodyShape.CIRCLE , Constants.MoveableBodyType.ZOMBIE);
	}
}
