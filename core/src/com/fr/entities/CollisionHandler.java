package com.fr.entities;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.fr.utils.Assets;
import com.fr.utils.Settings;

public class CollisionHandler implements ContactListener {

	@Override
	public void beginContact(Contact contact) {
		Fixture fixtureA = contact.getFixtureA();
		Fixture fixtureB = contact.getFixtureB();

		Body bodyA = fixtureA.getBody();
		Body bodyB = fixtureB.getBody();

		Object entityA = bodyA.getUserData();
		Object entityB = bodyB.getUserData();

		if (entityA instanceof Platform && entityB instanceof Dude) {
			Dude thief = (Dude) entityB;
			Platform platform = (Platform) entityA;
			thief.killVelocites();

			platform.isTaken = true;

			if (platform.hasShield) {
				Assets.playSound(Assets.powerUp);
				thief.shieldUp();
			}

			if (platform.hasCoin) {
				Assets.playSound(Assets.scoreUp);
				Settings.numCoins += 1;
			}

			switch (platform.platformType) {
			case NORMAL:
				break;
			case GLUE:
				if (!thief.isShielded) {
					Assets.playSound(Assets.lockedUp);
					thief.isGlued = true;
				}
				break;
			case COLLAPSE_AFTER_TIMEOUT:
				platform.isActivated = true;
				break;
			case COLLAPSE_ON_TOUCH:
				Assets.playSound(Assets.suckedIn);
				thief.isStuck = true;
				break;
			case SPIKES:
				if (platform.hasSpikes && !thief.isShielded) {
					Assets.playSound(Assets.hitSound);
					thief.isCaught = true;
				}
				break;
			}

		}

		if (entityA instanceof Dude && entityB instanceof Platform) {
			Dude thief = (Dude) entityA;
			Platform platform = (Platform) entityB;
			thief.killVelocites();

			platform.isTaken = true;

			if (platform.hasShield) {
				Assets.playSound(Assets.powerUp);
				thief.shieldUp();
			}

			if (platform.hasCoin) {
				Settings.numCoins += 1;
			}

			switch (platform.platformType) {
			case NORMAL:
				break;
			case GLUE:
				if (!thief.isShielded) {
					Assets.playSound(Assets.lockedUp);
					thief.isGlued = true;
				}
				break;
			case COLLAPSE_AFTER_TIMEOUT:
				platform.isActivated = true;
				break;
			case COLLAPSE_ON_TOUCH:
				Assets.playSound(Assets.suckedIn);
				thief.isStuck = true;
				break;
			case SPIKES:
				if (platform.hasSpikes && !thief.isShielded) {
					Assets.playSound(Assets.hitSound);
					thief.isCaught = true;
				}
				break;
			}
		}

		if (entityA instanceof Platform && entityB instanceof Police) {
			Police thief = (Police) entityB;
			thief.killVelocites();
		}

		if (entityA instanceof Police && entityB instanceof Platform) {
			Police thief = (Police) entityA;
			thief.killVelocites();
		}

		if (entityA instanceof Police && entityB instanceof Dude) {
			Police police = (Police) entityA;
			Dude thief = (Dude) entityB;
			Assets.playSound(Assets.hitSound);
			thief.isCaught = true;
		}
		if (entityA instanceof Dude && entityB instanceof Police) {
			Police police = (Police) entityB;
			Dude thief = (Dude) entityA;
			Assets.playSound(Assets.hitSound);
			thief.isCaught = true;
		}
	}

	@Override
	public void endContact(Contact contact) {

	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		Fixture fixtureA = contact.getFixtureA();
		Fixture fixtureB = contact.getFixtureB();

		Body bodyA = fixtureA.getBody();
		Body bodyB = fixtureB.getBody();

		Object entityA = bodyA.getUserData();
		Object entityB = bodyB.getUserData();

		if (entityA instanceof Platform && entityB instanceof Dude) {
			Dude thief = (Dude) entityB;
			Platform platform = (Platform) entityA;
			switch (platform.platformType) {
			case NORMAL:
				break;
			case GLUE:
				break;
			case COLLAPSE_AFTER_TIMEOUT:
				break;
			case COLLAPSE_ON_TOUCH:
				contact.setEnabled(false);
				break;
			case SPIKES:
				break;
			}
		}

		if (entityA instanceof Dude && entityB instanceof Platform) {
			Dude thief = (Dude) entityA;
			Platform platform = (Platform) entityB;
			switch (platform.platformType) {
			case NORMAL:
				break;
			case GLUE:
				break;
			case COLLAPSE_AFTER_TIMEOUT:
				break;
			case COLLAPSE_ON_TOUCH:
				contact.setEnabled(false);
				break;
			case SPIKES:
				break;
			}
		}

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {

	}

}
