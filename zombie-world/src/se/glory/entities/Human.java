package se.glory.entities;

import se.glory.utilities.Constants;
import se.glory.utilities.TextureHandler;

public class Human extends MoveableBody {
	
	public Human(int x, int y) {
		super(x, y, 15, 15, TextureHandler.humanTexture, Constants.MoveableBodyShape.CIRCLE , Constants.MoveableBodyType.HUMAN);
	}
}
