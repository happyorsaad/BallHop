package com.fr.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.fr.utils.Assets;
import com.fr.utils.Settings;

public class Dude {
	public static final float THIEF_RADIUS = 0.5f;
	public final float THIEF_DENSITY = 10f;
	int numberSwitch = 0;
	private THIEF_JUMP currentJump = THIEF_JUMP.NO_JUMP;
	private final float FORCE_MAGNITUDE = 39.755f;

	Body thiefBody;
	Vector2 originalLocation;

	public boolean isShielded;
	public boolean inAir;
	public boolean isGlued;
	public boolean isCaught;
	public boolean isStuck;

	public enum THIEF_JUMP {
		NO_JUMP, SHORT_JUMP, LONG_JUMP
	}

	public Dude(World world, Vector2 location) {
		// First we create a body definition
		BodyDef bodyDef = new BodyDef();
		// We set our body to dynamic
		bodyDef.type = BodyType.DynamicBody;
		// Set our body's starting position in the world
		bodyDef.position.set(location);
		// Create our body in the world using our body definition
		thiefBody = world.createBody(bodyDef);
		// set User Pointer to the Current Object for Rendering Purposes
		thiefBody.setUserData(this);
		// Create a circle shape and set its radius to 6
		CircleShape circle = new CircleShape();
		circle.setRadius(THIEF_RADIUS);
		// Create a fixture definition to apply our shape to
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circle;
		fixtureDef.density = THIEF_DENSITY;
		fixtureDef.friction = 1f;
		fixtureDef.restitution = 0f; // Make it bounce a little bit
		// Create our fixture and attach it to the body
		Fixture fixture = thiefBody.createFixture(fixtureDef);
		// Remember to dispose of any shapes after you're done with them!
		// BodyDef and FixtureDef don't need disposing, but shapes do.
		circle.dispose();
		thiefBody.setTransform(thiefBody.getPosition().x,
				thiefBody.getPosition().y, (float) (Math.toRadians(-15)));
		this.originalLocation = location;
		this.inAir = true;
		this.isGlued = false;
	}

	public void shortJump() {
		if (currentJump == THIEF_JUMP.NO_JUMP)
			currentJump = THIEF_JUMP.SHORT_JUMP;
	}

	public void longJump() {
		if (currentJump == THIEF_JUMP.NO_JUMP)
			currentJump = THIEF_JUMP.LONG_JUMP;
	}

	public void update(float delta) {
		if (isShielded) {
			shieldTime += delta;
			if (shieldTime >= shieldLimit) {
				isShielded = false;
				shieldTime = 0;
			}
		}
		if (!isCaught && !isGlued && !isStuck) {
			switch (currentJump) {
			case NO_JUMP:
				break;
			case SHORT_JUMP:
				if (!inAir) {
					Assets.playSound(Assets.jump);
					thiefBody.applyLinearImpulse(FORCE_MAGNITUDE * 2,
							FORCE_MAGNITUDE / 2, 0, 0, true);
					inAir = true;
				}
			case LONG_JUMP:
				if (!inAir) {
					Assets.playSound(Assets.jump);
					thiefBody.applyLinearImpulse(FORCE_MAGNITUDE * 2,
							FORCE_MAGNITUDE, 0, 0, true);
					inAir = true;
				}
			}
		}
	}

	public Vector2 getPosition() {
		return thiefBody.getPosition();
	}

	public float getAngle() {
		return (float) Math.toDegrees(thiefBody.getAngle());
	}

	public long numJumps;

	public void killVelocites() {
		thiefBody.setLinearVelocity(Vector2.Zero);
		thiefBody.setAngularVelocity(0);
		currentJump = THIEF_JUMP.NO_JUMP;
		inAir = false;
		numJumps += 1;
	}

	public void reset() {
		thiefBody.setLinearVelocity(Vector2.Zero);
		thiefBody.setAngularVelocity(0);
		currentJump = THIEF_JUMP.NO_JUMP;
		inAir = true;
		isCaught = false;
		isGlued = false;
		isStuck = false;
		isShielded = false;
	}

	public THIEF_JUMP getCurrentStatus() {
		return currentJump;
	}

	public Vector2 getLinearVeclocity() {
		return thiefBody.getLinearVelocity();
	}

	public void setPosition(float x, float y) {
		thiefBody.setTransform(x, y, 0);
	}

	private float shieldTime = 0;
	private float shieldLimit = 10;

	public void shieldUp() {
		shieldTime = 0;
		isShielded = true;
		shieldLimit = (float) (Math.pow(2, Settings.shieldLevel - 1) * Settings.shieldMultiplier);
	}

	public float getShieldPercentage() {
		return Math.min(1, shieldTime / shieldLimit);
	}

}
