package se.glory.zombieworld.utilities;

import se.glory.zombieworld.model.WorldModel;
import se.glory.zombieworld.model.entities.Human;
import se.glory.zombieworld.model.entities.Player;
import se.glory.zombieworld.model.entities.Zombie;
import se.glory.zombieworld.model.entities.items.WeaponLoot;
import se.glory.zombieworld.model.entities.weapons.Bullet;

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
	UtilityTimer zombieCollisionTimer = null;

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

		if (i1.getType() == Constants.MoveableBodyType.HUMAN || i2.getType() == Constants.MoveableBodyType.HUMAN) {
			if (i1.getType() == Constants.MoveableBodyType.HUMAN) {
				Human h1 = (Human) i1.getObj();

				if (h1.getState() != Human.State.COLLIDING) {
					Vector2 direction = h1.getBody().getLinearVelocity().cpy().rotate(90);

					((Human) i1.getObj()).setState(Human.State.COLLIDING);
					h1.setDumbWalk(direction, 20);
				}
			}

			if (i2.getType() == Constants.MoveableBodyType.HUMAN) {
				Human h2 = (Human) i2.getObj();

				if (h2.getState() != Human.State.COLLIDING) {
					Vector2 direction = h2.getBody().getLinearVelocity().cpy().rotate(90);

					((Human) i2.getObj()).setState(Human.State.COLLIDING);
					h2.setDumbWalk(direction, 20);
				}
			}
		}

		//Only activated if zombie walks into player. not if player walks into human
		if ((i1.getType() == Constants.MoveableBodyType.PLAYER && i2.getType() == Constants.MoveableBodyType.ZOMBIE)
				|| (i1.getType() == Constants.MoveableBodyType.ZOMBIE && i2.getType() == Constants.MoveableBodyType.PLAYER)) {

			//Making a local object of the current object player depending on which object in the collision it is
			Player player = i1.getType() == Constants.MoveableBodyType.PLAYER ? (Player)i1.getObj() : (Player)i2.getObj();

			//Infecting the player
			if(player != null) {
				if (player.getInfectedHealthTimer() == null) {
					player.infect();
				}

				player.changeHealth(-Constants.ZOMBIE_DAMAGE);

				//Implemented for later use when needing a control of zombie attacking
				zombieCollisionTimer = new UtilityTimer(1000);
			}
		}

		//Only activated if human walks into player. not if player walks into human
		if ((i1.getType() == Constants.MoveableBodyType.PLAYER && i2.getType() == Constants.MoveableBodyType.HUMAN)
				|| (i1.getType() == Constants.MoveableBodyType.HUMAN && i2.getType() == Constants.MoveableBodyType.PLAYER)) {

			//Making a local object of the current object player depending on which object in the collision it is
			Player player = i1.getType() == Constants.MoveableBodyType.PLAYER ? (Player)i1.getObj() : (Player)i2.getObj();

			//infecting the player
			if(player != null) {
				player.changeHealth(1);
			}
		}

		//Only activated if zombie walks into player. not if player walks into human
		if ((i1.getType() == Constants.MoveableBodyType.HUMAN && i2.getType() == Constants.MoveableBodyType.ZOMBIE)
				|| (i1.getType() == Constants.MoveableBodyType.ZOMBIE && i2.getType() == Constants.MoveableBodyType.HUMAN)) {

			//Making a local object of the current object player depending on which object in the collision it is
			Human h = i1.getType() == Constants.MoveableBodyType.HUMAN ? (Human)i1.getObj() : (Human)i2.getObj();

			//infecting the player
			if(h != null) {
				if (h.getInfectedHealthTimer() == null) {
					h.infect();
				}

				h.changeHealth(-Constants.ZOMBIE_DAMAGE);
			}
		}

		//Checks if the first collision body is of type Item and the other is of type Player
		// OR the first is Player and the second is Item
		if (i1.getType() == Constants.MoveableBodyType.ITEM && i2.getType() == Constants.MoveableBodyType.PLAYER || i1.getType() == Constants.MoveableBodyType.PLAYER && i2.getType() == Constants.MoveableBodyType.ITEM) {
			//These 2 ifs checks wheater its the first or second body that is the Item
			//In the if we set the items dead boolean to true, for later removal of the item
			if (i1.getType() == Constants.MoveableBodyType.ITEM) {
				i1.setDead(true);
				WorldModel.player.addItemToQuickSwap(((WeaponLoot)i1.getObj()).getWeapon());
			} else if (i2.getType() == Constants.MoveableBodyType.ITEM) {
				i2.setDead(true);
				WorldModel.player.addItemToQuickSwap(((WeaponLoot)i2.getObj()).getWeapon());
			}

		}

		if (i1.getType() == Constants.MoveableBodyType.DOOR && i2.getType() == Constants.MoveableBodyType.PLAYER || i1.getType() == Constants.MoveableBodyType.PLAYER && i2.getType() == Constants.MoveableBodyType.DOOR) {
			//These 2 ifs checks wheater its the first or second body that is the door
			//In the if we set the doors open boolean to true, for later removal of the door
			if (i1.getType() == Constants.MoveableBodyType.DOOR){
				i1.setOpen(true);
			} else if (i2.getType() == Constants.MoveableBodyType.DOOR) {
				i2.setOpen(true);
			}
		}

		//This statement checks if a bullet collides with a zombie or a zombie with a bullet
		//then removes both of them from the world
		if (i1.getType() == Constants.MoveableBodyType.BULLET && i2.getType() == Constants.MoveableBodyType.ZOMBIE || i1.getType() == Constants.MoveableBodyType.ZOMBIE && i2.getType() == Constants.MoveableBodyType.BULLET) {
			Zombie z = i1.getType() == Constants.MoveableBodyType.ZOMBIE ? (Zombie)i1.getObj() : (Zombie)i2.getObj();
			Identity bulletIdentity = i1.getType() == Constants.MoveableBodyType.BULLET ? i1 : i2;

			//Saving bulletdamage in case the bullet is removed before the damage is dealt
			float bulletDamage = ((Bullet)bulletIdentity.getObj()).getDamage();
			bulletIdentity.setDead(true);

			if (z.getHealth() - bulletDamage <= 0) {
				Score.addScore(Constants.ScoreType.KILL_ZOMBIE);
			}

			z.changeHealth(-bulletDamage);
		}

		//This statement checks if a bullet collides with a human or a human with a bullet
		//then removes both of them from the world
		if (i1.getType() == Constants.MoveableBodyType.BULLET && i2.getType() == Constants.MoveableBodyType.HUMAN || i1.getType() == Constants.MoveableBodyType.HUMAN && i2.getType() == Constants.MoveableBodyType.BULLET) {
			Human h = i1.getType() == Constants.MoveableBodyType.HUMAN ? (Human)i1.getObj() : (Human)i2.getObj();
			Identity bulletIdentity = i1.getType() == Constants.MoveableBodyType.BULLET ? i1 : i2;

			//Saving bulletdamage in case the bullet is removed before the damage is dealt
			float bulletDamage = ((Bullet)bulletIdentity.getObj()).getDamage();
			bulletIdentity.setDead(true);

			if (h.getHealth() - bulletDamage <= 0) {
				Score.addScore(Constants.ScoreType.KILL_HUMAN);
			}

			h.changeHealth(-bulletDamage);
		}

		if (i1.getType() == Constants.MoveableBodyType.BULLET && i2.getType() == Constants.MoveableBodyType.PLAYER || i1.getType() == Constants.MoveableBodyType.PLAYER && i2.getType() == Constants.MoveableBodyType.BULLET) {
			Identity bulletIdentity = i1.getType() == Constants.MoveableBodyType.BULLET ? i1 : i2;

			//Saving bulletdamage in case the bullet is removed before the damage is dealt
			float bulletDamage = ((Bullet)bulletIdentity.getObj()).getDamage();
			bulletIdentity.setDead(true);

			WorldModel.player.changeHealth(-bulletDamage);
		}

		if (i1.getType() == Constants.MoveableBodyType.BULLET || i2.getType() == Constants.MoveableBodyType.BULLET) {
			Identity bulletIdentity = i1.getType() == Constants.MoveableBodyType.BULLET ? i1 : i2;
			bulletIdentity.setDead(true);
		}
	}

	@Override
	public void endContact(Contact contact) {
		zombieCollisionTimer = null;
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {

	}
}