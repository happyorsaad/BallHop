package com.fr.entities;

import java.util.Random;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.fr.entities.Platform.PLATFORM_TYPE;
import com.fr.game.BallHop;

public class GameWorld {
	
	public static final float WORLD_WIDTH = 40f;
	public static final float WORLD_HEIGHT = 30f;

	private static final float PLATFORM_REFRESH = 0.75f;
	private int startIndex, endIndex, policeIndex, thiefIndex;
	private int NUM_PLATFORMS = 80;

	Array<Platform> platforms = new Array<Platform>();

	public enum WorldState {
		WAITING, RUNNING, GAME_OVER
	}

	class State {
		float x;
		float y;
	}

	public WorldState state;

	private final float TIME_STEP = 1 / 60f;
	private final int VELOCITY_ITERATIONS = 3;
	private final int POSITION_ITERATIONS = 2;

	public World world;
	public BallHop gameRef;
	public Dude thief;
	public Police police;
	public Platform platform;

	public float accumulator;

	private final Vector2 THIEF_LOCATION = new Vector2(10, 12);
	private final Vector2 POLICE_LOCATION = new Vector2(0, 12);

	private float refreshTime = 0;

	private final float PLATFORM_SINGLE_DELTA = 5.06175f;
	private final float PLATFORM_DOUBLE_DELTA = 10.29225f;

	private Random nextPlatform;
	public long gameScore;

	public State current, previous;
	public State render;

	private String gameStoreString;

	public GameWorld(BallHop gameRef) {
		this.gameRef = gameRef;
		this.world = new World(new Vector2(0, -9.8f), false);
		this.world.setContactListener(new CollisionHandler());
		this.thief = new Dude(world, THIEF_LOCATION);
		this.police = new Police(world, POLICE_LOCATION);
		this.accumulator = 0.0f;
		this.state = WorldState.RUNNING;
		this.gameScore = 0;
		this.gameStoreString = "0";
		this.current = new State();
		this.previous = new State();
		this.render = new State();
		this.nextPlatform = new Random();
		this.nextPlatform.setSeed(System.currentTimeMillis());
		startIndex = 0;
		endIndex = NUM_PLATFORMS - 1;
		thiefIndex = 10;
		policeIndex = thiefIndex - 6;
		time = 0;
		loadDefaultPlatforms();
	}

	private void loadDefaultPlatforms() {
		if (platforms.size == 0) {
			float x = 0;
			for (int i = 0; i < NUM_PLATFORMS; i += 1) {
				int next = nextPlatform.nextInt(2);
				Vector2 location = new Vector2(x, 10);
				Platform platform = new Platform(world, location);
				platform.platformType = getStationaryType();
				platforms.add(platform);
				x = next == 0 ? x + PLATFORM_SINGLE_DELTA : x
						+ PLATFORM_DOUBLE_DELTA;
			}

			setPlatformTypes(1);

		} else {
			float x = 0;
			for (int i = 0; i < NUM_PLATFORMS; i += 1) {
				int next = nextPlatform.nextInt(2);
				Platform platform = platforms.get(i);
				platform.platformType = getStationaryType();
				platform.reset(x);
				x = next == 0 ? x + PLATFORM_SINGLE_DELTA : x
						+ PLATFORM_DOUBLE_DELTA;
			}

			setPlatformTypes(1);
		}

		thief.setPosition(platforms.get(thiefIndex).getLocation().x, 12f);
		police.setPosition(platforms.get(policeIndex).getLocation().x, 12f);

		platforms.get(thiefIndex).platformType = PLATFORM_TYPE.NORMAL;
	}

	float time;

	int lastIndex = -1;

	private void updatePolice() {
		if (policeIndex < 0)
			return;
		Platform current = platforms.get(policeIndex - startIndex);
		Platform next = policeIndex - startIndex + 1 < NUM_PLATFORMS ? platforms
				.get(policeIndex - startIndex + 1) : null;
		Platform nextToNext = policeIndex - startIndex + 2 < NUM_PLATFORMS ? platforms
				.get(policeIndex - startIndex + 2) : null;

		float deltaOne = next == null ? 0 : next.getLocation().x
				- current.getLocation().x;
		float deltaTwo = nextToNext == null ? 0 : nextToNext.getLocation().x
				- current.getLocation().x;

		float p = (float) Math.random();
		if (p < 0.8 && deltaTwo < 13) {
			police.longJump();
		} else {
			if (deltaOne > 7f) {
				police.longJump();
			} else {
				police.shortJump();
			}
		}
	}

	public void update(float delta) {
		if (state == WorldState.GAME_OVER)
			return;

		if (delta > 0.25f) {
			delta = TIME_STEP;
		}

		accumulator += delta;

		updatePolice();

		while (accumulator >= TIME_STEP) {
			world.clearForces();
			police.update(delta);
			thief.update(delta);
			previous.x = current.x;
			previous.y = current.y;
			world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
			accumulator -= TIME_STEP;
		}

		if (thief.getPosition().y < 0) {
			thief.isCaught = true;
		}

		if (police.getPosition().y < 0) {
			resetPoliceAfterFalling();
		}

		if (thief.isCaught) {
			state = WorldState.GAME_OVER;
		} else {
			int no = 0;

			while (platforms.get(thiefIndex - startIndex).getLocation().x < thief
					.getPosition().x - Platform.PLATFORM_WIDTH
					&& !thief.inAir) {
				thiefIndex += 1;
				gameScore += 1;
				gameStoreString = String.valueOf(gameScore);
				no += 1;
			}

			if (no > 1) {
				moveAfterDoubleJump();
			}

			float D = police.getPosition().x
					- platforms.get(policeIndex - startIndex).getLocation().x;

			while (D > 0.9f && !police.isInAir()) {
				policeIndex += 1;
				D = police.getPosition().x
						- platforms.get(policeIndex - startIndex).getLocation().x;
			}

		}

		for (Platform platform : platforms) {
			float distance = Math.abs(platform.getLocation().x
					- police.getPosition().x);
			if (distance <= GameWorld.WORLD_WIDTH / 2.5f && platform.hasSpikes) {
				platform.hasSpikes = false;
			}
			platform.update(delta);
		}
		updatePlatforms(delta);

	}

	private void updatePlatforms(float delta) {
		refreshTime += delta;
		if (refreshTime > PLATFORM_REFRESH) {
			refreshTime -= PLATFORM_REFRESH;
			for (int num = 0; num < 3; num += 1) {
				if (startIndex < policeIndex - 5 && startIndex < thiefIndex - 5) {
					Platform lastPlatform = platforms
							.get(endIndex - startIndex);
					Platform platform = platforms.removeIndex(0);
					int next = nextPlatform.nextInt(2);
					platform.reset(next == 0 ? lastPlatform.getLocation().x
							+ PLATFORM_SINGLE_DELTA : lastPlatform
							.getLocation().x + PLATFORM_DOUBLE_DELTA);
					platform.platformType = getStationaryType();
					platforms.add(platform);
					startIndex += 1;
					endIndex += 1;
				}
			}

			int last = 0;
			for (last = NUM_PLATFORMS - 1; last >= 0; last--) {
				Platform p = platforms.get(last);
				if (p.platformType == PLATFORM_TYPE.GLUE
						|| p.platformType == PLATFORM_TYPE.COLLAPSE_ON_TOUCH) {
					break;
				}
			}

			for (int i = last + 1; i < NUM_PLATFORMS; i += 1) {
				platforms.get(i).platformType = getStationaryType();
			}

			setPlatformTypes(last + 1);

		}
	}

	private void moveAfterDoubleJump() {
		for (int i = thiefIndex - startIndex + 5; i < platforms.size; i += 1) {
			platforms.get(i).moveLeft(platforms.get(i).getLocation().x + 0.17f);
		}
	}

	private void resetPoliceAfterFalling() {
		policeIndex = thiefIndex - 3;
		police.setPosition(platforms.get(Math.max(0, policeIndex - startIndex))
				.getLocation().x, 12f);
	}

	boolean requiresDoubleJump[] = new boolean[NUM_PLATFORMS];

	private void setPlatformTypes(int startIndex) {
		for (int i = startIndex; i < NUM_PLATFORMS; i += 1) {
			Platform current = platforms.get(i);
			Platform prev = platforms.get(i - 1);
			float D = current.getLocation().x - prev.getLocation().x;
			if (D > 8f) {
				requiresDoubleJump[i] = true;
			} else {
				requiresDoubleJump[i] = false;
			}
		}

		for (int i = startIndex; i < NUM_PLATFORMS - 3;) {
			if (!requiresDoubleJump[i] && !requiresDoubleJump[i + 1]
					&& !requiresDoubleJump[i + 2] && !requiresDoubleJump[i + 3]) {
				float p = (float) Math.random();
				if (p < 0.5f) {
					platforms.get(i + 1).platformType = getJumpOverType();
					i += 2;
				} else {
					platforms.get(i + 2).platformType = getJumpOverType();
					i += 3;
				}
			} else if (!requiresDoubleJump[i] && !requiresDoubleJump[i + 1]
					&& !requiresDoubleJump[i + 2] && requiresDoubleJump[i + 3]) {
				platforms.get(i + 1).platformType = getJumpOverType();
				i += 2;
			} else if (requiresDoubleJump[i] && !requiresDoubleJump[i + 1]
					&& !requiresDoubleJump[i + 2] && !requiresDoubleJump[i + 3]) {
				platforms.get(i + 2).platformType = getJumpOverType();
				i += 3;
			} else if (requiresDoubleJump[i] && !requiresDoubleJump[i + 1]
					&& !requiresDoubleJump[i + 2] && requiresDoubleJump[i + 3]) {
				platforms.get(i + 1).platformType = getJumpOverType();
				i += 2;
			} else {
				i += 1;
			}
		}
	}

	private PLATFORM_TYPE getJumpOverType() {
		float p = (float) Math.random();
		if (p > 0f && p < 0.45f) {
			return PLATFORM_TYPE.COLLAPSE_ON_TOUCH;
		} else if (p >= 0.45f && p < 0.90f) {
			return PLATFORM_TYPE.GLUE;
		}
		return PLATFORM_TYPE.GLUE;
	}

	private PLATFORM_TYPE getStationaryType() {
		float p = (float) Math.random();
		if (p > 0f && p < 0.10f) {
			return PLATFORM_TYPE.SPIKES;
		} else if (p > 0.10f && p < 0.15f) {
			return PLATFORM_TYPE.SPIKES;
		}
		return PLATFORM_TYPE.NORMAL;
	}

	public void reset() {
		state = WorldState.RUNNING;
		startIndex = 0;
		endIndex = NUM_PLATFORMS - 1;
		thiefIndex = 10;
		policeIndex = thiefIndex - 2;
		time = 0;
		loadDefaultPlatforms();
		thief.reset();
		police.reset();
		gameScore = 0;
		gameStoreString = "0";
	}

	public void resume() {
		state = WorldState.RUNNING;
		thief.reset();
		police.reset();
		thief.setPosition(
				platforms.get(thiefIndex - startIndex).getLocation().x, 12f);
		police.setPosition(
				platforms.get(
						Math.max(
								0,
								Math.min(policeIndex, thiefIndex - startIndex
										- 6))).getLocation().x, 12f);
		platforms.get(thiefIndex - startIndex).platformType = PLATFORM_TYPE.NORMAL;
		platforms.get(thiefIndex - startIndex).setLocation(
				platforms.get(thiefIndex - startIndex).getLocation().x, 10);
	}

	public String getScore() {
		return gameStoreString;
	}

	public void unGlue() {
		thief.isGlued = false;
		platforms.get(thiefIndex - startIndex).platformType = PLATFORM_TYPE.NORMAL;
		thief.killVelocites();
	}
}
