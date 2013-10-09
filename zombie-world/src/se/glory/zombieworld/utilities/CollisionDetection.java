package se.glory.zombieworld.utilities;

import se.glory.zombieworld.model.WorldModel;
import se.glory.zombieworld.model.entities.Human;
import se.glory.zombieworld.model.entities.Zombie;
import se.glory.zombieworld.model.entities.items.Item;
import se.glory.zombieworld.model.entities.items.WeaponLoot;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

/*
 * This class will handle all of the Collisions in our Box2D world.
 */
public class CollisionDetection implements ContactListener {
	
	// TODO Refactor all these if statements into class methods!
	@Override
	public void beginContact(Contact contact) {
		Body a = contact.getFixtureA().getBody();
		Body b = contact.getFixtureB().getBody();
		
		Identity i1 = (Identity) a.getUserData();
		Identity i2 = (Identity) b.getUserData();
		
		if (i1 == null || i2 == null) {
			return;
		}
		
		// If two humans collide, fix this. TODO: Refactor, use same code for zombies.
		if (i1.getType() == Constants.MoveableBodyType.HUMAN && i2.getType() == Constants.MoveableBodyType.HUMAN) {
			// Make the first human turn to his right for 15 updates
			Human h1 = (Human) i1.getObj();
			Vector2 h1v = h1.getBody().getLinearVelocity().cpy().rotate(90);	
			
			h1.setState(Human.State.COLLIDING);
			h1.setCollidingInfo(h1v, 15);
			
			// Make the second human turn to his right for 15 updates
			Human h2 = (Human) i2.getObj();
			Vector2 h2v = h2.getBody().getLinearVelocity().cpy().rotate(90);
			
			h2.setState(Human.State.COLLIDING);
			h2.setCollidingInfo(h2v, 15);
		}
		
		// If two zombies collide, fix this. TODO: Refactor, use same code as for humans.
		if (i1.getType() == Constants.MoveableBodyType.ZOMBIE && i2.getType() == Constants.MoveableBodyType.ZOMBIE) {
			// Make the first human turn to his right for 15 updates
			Zombie z1 = (Zombie) i1.getObj();
			Vector2 z1v = z1.getBody().getLinearVelocity().cpy().rotate(90);	
			
			z1.setState(Zombie.State.COLLIDING);
			z1.setCollidingInfo(z1v, 15);
			
			// Make the second human turn to his right for 15 updates
			Zombie z2 = (Zombie) i2.getObj();
			Vector2 z2v = z2.getBody().getLinearVelocity().cpy().rotate(90);
			
			z2.setState(Zombie.State.COLLIDING);
			z2.setCollidingInfo(z2v, 15);
		}
		
		
		
		
		
		//Checks if the first collision body is of type Item and the other is of type Player
		// OR the first is Player and the second is Item
		if (i1.getType() == Constants.MoveableBodyType.ITEM && i2.getType() == Constants.MoveableBodyType.PLAYER || i1.getType() == Constants.MoveableBodyType.PLAYER && i2.getType() == Constants.MoveableBodyType.ITEM) {
			//These 2 ifs checks wheater its the first or second body that is the Item
			//In the if we set the items dead boolean to true, for later removal of the item
			if (i1.getType() == Constants.MoveableBodyType.ITEM){
				i1.setDead(true);
				WorldModel.player.addItemToQuickSwap(((WeaponLoot)i1.getObj()).getWeapon());
			} else if (i2.getType() == Constants.MoveableBodyType.ITEM) {
				i2.setDead(true);
				WorldModel.player.addItemToQuickSwap(((WeaponLoot)i2.getObj()).getWeapon());
			}
			
		}
		
		// TODO Test theese two ifs on a Windows computer / Android phone
		/*
		//This statement checks if a bullet collides with a zombie or a zombie with a bullet
		//then removes both of them from the world
		if (i1.getType() == Constants.MoveableBodyType.BULLET && i2.getType() == Constants.MoveableBodyType.ZOMBIE || i1.getType() == Constants.MoveableBodyType.ZOMBIE && i2.getType() == Constants.MoveableBodyType.BULLET) {
			i1.setDead(true);
			i2.setDead(true);
		}
		
		//This statement checks if a bullet collides with a human or a human with a bullet
		//then removes both of them from the world
		if (i1.getType() == Constants.MoveableBodyType.BULLET && i2.getType() == Constants.MoveableBodyType.HUMAN || i1.getType() == Constants.MoveableBodyType.HUMAN && i2.getType() == Constants.MoveableBodyType.BULLET) {
			i1.setDead(true);
			i2.setDead(true);
		}*/
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
