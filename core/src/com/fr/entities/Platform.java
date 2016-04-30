package com.fr.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Platform {
	public final static float PLATFORM_WIDTH = 1.5f;
	public Body platformBody;
	public boolean hasCoin;
	public boolean hasShield;
	public boolean isTaken;

	public enum PLATFORM_TYPE {
		NORMAL, SPIKES, COLLAPSE_ON_TOUCH, COLLAPSE_AFTER_TIMEOUT, GLUE
	}

	public PLATFORM_TYPE platformType;

	public Platform(World world, Vector2 center) {
		// First we create a body definition
		BodyDef bodyDef = new BodyDef();
		// We set our body to dynamic
		bodyDef.type = BodyType.KinematicBody;
		// Set our body's starting position in the world
		bodyDef.position.set(center);
		// Create our body in the world using our body definition
		platformBody = world.createBody(bodyDef);
		// reset(center.x);
		// set User Pointer to the Current Object for Rendering Purposes
		platformBody.setUserData(this);
		// Create a circle shape and set its radius to 6
		PolygonShape box = new PolygonShape();
		box.setAsBox(PLATFORM_WIDTH / 2, 0.5f);
		// Create a fixture definition to apply our shape to
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = box;
		fixtureDef.density = 0.0f;
		fixtureDef.friction = 0f;
		fixtureDef.restitution = 0f; // Make it bounce a little bit
		// Create our fixture and attach it to the body
		Fixture fixture = platformBody.createFixture(fixtureDef);
		// Remember to dispose of any shapes after you're done with them!
		// BodyDef and FixtureDef don't need disposing, but shapes do.
		box.dispose();
		// platformType = PLATFORM_TYPE.NORMAL;
		hasSpikes = isActivated = false;
		time = (float) Math.random();
		collapseTime = 0;

		setCoinAndShieldStatus();
	}

	public boolean isActivated;
	public boolean hasSpikes;

	private float time = 0, collapseTime = 0;
	private final float SPIKE_SWITCH_FREQUENCY = 2.0f;

	public void update(float delta) {
		switch (platformType) {
		case NORMAL:
			updateNormal(delta);
			break;
		case SPIKES:
			updateSpikes(delta);
			break;
		case COLLAPSE_ON_TOUCH:
			updateCollapseOnTouch(delta);
			break;
		case COLLAPSE_AFTER_TIMEOUT:
			updateCollapseAfterTimeout(delta);
			break;
		case GLUE:
			updateGlue(delta);
			break;
		}
	}

	private void updateGlue(float delta) {

	}

	private void updateCollapseAfterTimeout(float delta) {
		if (isActivated) {
			collapseTime += delta;
			if (collapseTime >= 1.5f) {
				// platformBody.setTransform(platformBody.getPosition().x, -10,
				// 0);
				// platformBody.applyLinearImpulse(0, -0.0001f, 0, 0, true);
				// PolygonShape box = (PolygonShape) (platformBody
				// .getFixtureList().size > 0 ? platformBody
				// .getFixtureList().get(0).getShape() : null);
				// if (box != null)
				// box.setAsBox(PLATFORM_WIDTH / 2, 0.5f);
				collapseTime -= 1.5f;
			}
		}
	}

	private void updateCollapseOnTouch(float delta) {

	}

	private void updateSpikes(float delta) {
		time += delta;
		if (time >= SPIKE_SWITCH_FREQUENCY) {
			hasSpikes = !hasSpikes;
			time -= SPIKE_SWITCH_FREQUENCY;
		}
	}

	private void updateNormal(float delta) {

	}

	public void reset(float X) {
		platformBody.setTransform(X, 10, 0);
		platformBody.applyLinearImpulse(0, -0.0001f, 0, 0, true);
		PolygonShape box = (PolygonShape) (platformBody.getFixtureList().size > 0 ? platformBody
				.getFixtureList().get(0).getShape()
				: null);
		if (box != null)
			box.setAsBox(PLATFORM_WIDTH / 2, 0.5f);

		hasSpikes = isActivated = false;
		time = (float) Math.random();
		collapseTime = 0;
		setCoinAndShieldStatus();
	}

	public void moveLeft(float X) {
		platformBody.setTransform(X, 10, 0);
		platformBody.applyLinearImpulse(0, -0.0001f, 0, 0, true);
		PolygonShape box = (PolygonShape) (platformBody.getFixtureList().size > 0 ? platformBody
				.getFixtureList().get(0).getShape()
				: null);
		if (box != null)
			box.setAsBox(PLATFORM_WIDTH / 2, 0.5f);
	}

	public void setLocation(float x, float y) {
		platformBody.setTransform(x, y, (float) (Math.toRadians(0)));
	}

	public Vector2 getLocation() {
		return platformBody.getPosition();
	}

	private void setCoinAndShieldStatus() {
		double p = Math.random();
		if (p >= 0 && p <= 0.25f) {
			hasCoin = true;
			hasShield = false;
		} else if (p >= 0.60 && p <= 0.62) {
			hasShield = true;
			hasCoin = false;
		}
		isTaken = false;
	}
}
