package com.fr.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class Police {
	public final float THIEF_RADIUS = 0.5f;
	public final float THIEF_DENSITY = 10f;
	private boolean isCaught = false;
	int numberSwitch = 0;
	private THIEF_JUMP currentJump = THIEF_JUMP.NO_JUMP;
	private final float FORCE_MAGNITUDE = 39.755f;

	Body thiefBody;
	Vector2 originalLocation;

	private boolean isShielded;
	private int numberShields;

	public enum THIEF_JUMP {
		NO_JUMP, SHORT_JUMP, LONG_JUMP
	}

	private boolean inAir;

	public Police(World world, Vector2 location) {
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
	}

	/*
	 * Check if the Yeti is alive or not Used while checking for GameOver
	 */
	public boolean isCaught() {
		return isCaught;
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
		if (!this.isCaught()) {
			switch (currentJump) {
			case NO_JUMP:
				break;
			case SHORT_JUMP:
				if (!inAir) {
					thiefBody.applyLinearImpulse(FORCE_MAGNITUDE * 2,
							FORCE_MAGNITUDE / 2, 0, 0, true);
					inAir = true;
				}
			case LONG_JUMP:
				if (!inAir) {
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

	public void killVelocites() {
		thiefBody.setLinearVelocity(Vector2.Zero);
		thiefBody.setAngularVelocity(0);
		currentJump = THIEF_JUMP.NO_JUMP;
		inAir = false;
	}

	public void reset() {

	}

	public void gotCaught() {
		isCaught = true;
	}

	public THIEF_JUMP getCurrentStatus() {
		return currentJump;
	}

	public void pickUpShield() {
		this.numberShields = Math.min(2, numberShields + 1);
	}

	public Vector2 getLinearVeclocity() {
		return thiefBody.getLinearVelocity();
	}

	public void setPosition(float x, float y) {
		thiefBody.setTransform(x, y, 0);
	}
	
	public boolean isInAir(){
		return inAir;
	}
}
