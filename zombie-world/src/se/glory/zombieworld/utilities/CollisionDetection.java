package se.glory.zombieworld.utilities;

import se.glory.zombieworld.model.WorldModel;
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
		
		Identity i1 = (Identity)a.getUserData();
		Identity i2 = (Identity)b.getUserData();
		
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
