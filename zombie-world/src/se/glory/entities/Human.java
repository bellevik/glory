package se.glory.entities;

import se.glory.utilities.Constants;
import se.glory.utilities.Identity;
import se.glory.utilities.WorldHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class Human extends MoveableBody {
	
	
	public Human(int x, int y) {
		super(x, y, 15, 15, null, Constants.MoveableBodyShape.CIRCLE , Constants.MoveableBodyType.HUMAN);
	}
}
