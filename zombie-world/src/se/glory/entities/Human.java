package se.glory.entities;

import se.glory.utilities.Constants;

public class Human extends MoveableBody {
	
	public Human(int x, int y) {
		super(x, y, 15, 15, null, Constants.MoveableBodyShape.CIRCLE , Constants.MoveableBodyType.HUMAN);
	}
}
