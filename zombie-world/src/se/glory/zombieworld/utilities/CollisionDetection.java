package se.glory.zombieworld.utilities;

import java.util.Random;

import se.glory.zombieworld.model.WorldModel;
import se.glory.zombieworld.model.entities.Human;
import se.glory.zombieworld.model.entities.items.Item;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

/*
 * This class will handle all of the Collisions in our Box2D world.
 */
public class CollisionDetection implements ContactListener {
	
	@Override
	public void beginContact(Contact contact) {
		Body a = contact.getFixtureA().getBody();
		Body b = contact.getFixtureB().getBody();
		
		Identity i1 = (Identity) a.getUserData();
		Identity i2 = (Identity) b.getUserData();
		
		if (i1 == null || i2 == null) {
			return;
		}
		
		// If two humans collide, fix this. TODO: Make smarter!
		
		// TODO, NOTE: Collision may cause body to get behind a wall, this will cause the 
		// body to get stuck trying to go to the next keypoint at the other side of the wall
		if (i1.getType() == Constants.MoveableBodyType.HUMAN && i2.getType() == Constants.MoveableBodyType.HUMAN) {
			
			// Solve by some random movement, not good, make better!
			((Human) i1.getObj()).getBody().setLinearVelocity(3 * new Random().nextFloat() + 1, 3 * new Random().nextFloat());
			((Human) i2.getObj()).getBody().setLinearVelocity(3 * new Random().nextFloat(), 3 * new Random().nextFloat() - 1);
		}
		
		
		
		//Checks if the first collision body is of type Item and the other is of type Player
		// OR the first is Player and the second is Item
		if (i1.getType() == Constants.MoveableBodyType.ITEM && i2.getType() == Constants.MoveableBodyType.PLAYER || i1.getType() == Constants.MoveableBodyType.PLAYER && i2.getType() == Constants.MoveableBodyType.ITEM) {
			//These 2 ifs checks wheater its the first or second body that is the Item
			//In the if we set the items dead boolean to true, for later removal of the item
			if (i1.getType() == Constants.MoveableBodyType.ITEM){
				i1.setDead(true);
				WorldModel.player.addItemToQuickSwap((Item)i1.getObj());
			} else if (i2.getType() == Constants.MoveableBodyType.ITEM) {
				i2.setDead(true);
				WorldModel.player.addItemToQuickSwap((Item)i2.getObj());
			}
			
		}
	}

	@Override
	public void endContact(Contact contact) {
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		
	}

}
