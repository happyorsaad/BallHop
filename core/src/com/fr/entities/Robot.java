package com.fr.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Robot {

	public static final float ROBOT_RADIUS = 1;
	public static final float ROBOT_DENSITY = 1;

	enum Direction {
		LEFT, UP, DOWN, RIGHT
	}

	enum Speed {
		SLOW, FAST
	}

	Vector2 location;
	Direction currentDirection;
	Speed currentSpeed;
	Body robotBody;

	public Robot(World world, Direction direction, Speed speed, Vector2 location) {
		this.currentDirection = direction;
		this.currentSpeed = speed;
		this.location = location;
		createBody(world, location);
	}

	private void createBody(World world, Vector2 location) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(location);
		robotBody = world.createBody(bodyDef);
		robotBody.setUserData(this);
		CircleShape circle = new CircleShape();
		circle.setRadius(ROBOT_RADIUS);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circle;
		fixtureDef.density = ROBOT_DENSITY;
		fixtureDef.friction = 1f;
		fixtureDef.restitution = 0f;
		Fixture fixture = robotBody.createFixture(fixtureDef);
		circle.dispose();
	}

	public void setDirection(Direction direction) {
		this.currentDirection = direction;
	}

	public void setSpeed(Speed speed) {
		this.currentSpeed = speed;
	}

	public void setLocation(float x, float y) {
		this.robotBody.setTransform(x, y, 0);
	}

	public void setLocation(Vector2 location) {
		setLocation(location.x, location.y);
	}

	public Vector2 getLocation() {
		return robotBody.getPosition();
	}

}
