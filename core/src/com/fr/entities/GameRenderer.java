package com.fr.entities;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Array;
import com.fr.entities.Platform.PLATFORM_TYPE;
import com.fr.utils.Assets;

public class GameRenderer {
	private GameWorld world;
	private Box2DDebugRenderer renderer;
	private OrthographicCamera camera;
	float treeRefreshTime = 0;
	float propY = 50;
	int propIndex = 0;
	float locationSampleTime = 0;
	private int NUM_PROPS = 40;
	private SpriteBatch batch;

	ShapeRenderer ren;

	private Rectangle shieldRect;
	private Rectangle shieldBar;

	enum PropType {
		TREE_ONE(2), TREE_TWO(0.5f), TREE_THREE(2f), TREE_FOUR(0.5f), TREE_FIVE(
				0.5f), TREE_SIX(0.5f), ROCK_ONE(1f), SHRUB_ONE(0.8f), SNOW_MAN(
				0f), LOG(1.5f), PILLAR(1f);
		private final float radius;

		private PropType(float radius) {
			this.radius = radius;
		}

		public float getRadius() {
			return radius;
		}

	}

	private PropType getType() {
		float p = (float) Math.random();
		if (p > 0.0f && p <= 0.45f) {
			return PropType.TREE_ONE;
		} else if (p > 0.45f && p <= 0.65f) {
			return PropType.ROCK_ONE;
		} else if (p > 0.65f && p < 0.95) {
			return PropType.SHRUB_ONE;
		} else if (p > 0.95f) {
			return PropType.LOG;
		}
		return PropType.TREE_ONE;
	}

	class Prop {
		public PropType type;
		public Vector2 location;
	}

	ArrayList<Prop> propLocations;
	Array<Vector2> yetiLocations;

	Random randomGenarator;
	private OrthographicCamera hudCam;

	public GameRenderer(GameWorld world) {
		this.world = world;
		this.renderer = new Box2DDebugRenderer(true, false, false, false, true,
				true);
		this.camera = new OrthographicCamera(GameWorld.WORLD_WIDTH,
				GameWorld.WORLD_WIDTH * Gdx.graphics.getHeight()
						/ Gdx.graphics.getWidth());
		this.camera.position.set(camera.viewportWidth / 2,
				camera.viewportHeight / 2, 0);
		this.camera.update();
		this.propLocations = new ArrayList<Prop>(NUM_PROPS);
		this.randomGenarator = new Random();
		this.yetiLocations = new Array<Vector2>();
		for (int i = 0; i < 10; i += 1) {
			yetiLocations.add(new Vector2(-1, 0));
		}

		this.hudCam = new OrthographicCamera(800, 480);
		this.hudCam.position.set(this.hudCam.viewportWidth / 2f,
				this.hudCam.viewportHeight / 2f, 0);
		this.hudCam.update();
		this.ren = new ShapeRenderer();

		this.shieldRect = new Rectangle(20, 20, 30, 30);
		this.shieldBar = new Rectangle(60, 20, 100, 30);
		this.batch = world.gameRef.batch;

	}

	public void render(float delta) {
		GL20 gl = Gdx.gl;
		gl.glClearColor(1, 1, 1, 1);
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.position.set(world.thief.getPosition().x + 8,
				camera.viewportHeight / 2f, 0);
		camera.update();

		batch.setProjectionMatrix(camera.combined);
		batch.begin();

		if (world.thief.isShielded) {
			float width = 1.65f * Dude.THIEF_RADIUS;
			Sprite aura = Assets.aura;
			aura.setAlpha(0.5f);
			aura.setBounds(world.thief.getPosition().x - width,
					world.thief.getPosition().y - width, 2 * width, 2 * width);
			aura.draw(batch);
			aura.setAlpha(1);
		}

		Sprite ball = Assets.mainBall;
		ball.setBounds(world.thief.getPosition().x - Dude.THIEF_RADIUS,
				world.thief.getPosition().y - Dude.THIEF_RADIUS,
				2 * Dude.THIEF_RADIUS, 2 * Dude.THIEF_RADIUS);
		ball.draw(batch);

		Sprite enemy = Assets.enemyBall;
		float radius = Dude.THIEF_RADIUS * 1.5f;
		enemy.setBounds(world.police.getPosition().x - radius,
				world.police.getPosition().y - radius, 2 * radius, 2 * radius);
		enemy.draw(batch);

		Sprite sprite = Assets.blackPlatform;
		sprite.setAlpha(1);

		for (Platform platform : world.platforms) {
			switch (platform.platformType) {
			case SPIKES:
				sprite = platform.hasSpikes ? Assets.spikePlatform
						: Assets.redPlatform;
				break;
			case COLLAPSE_ON_TOUCH:
				sprite = Assets.greenPlatform;
				break;
			case COLLAPSE_AFTER_TIMEOUT:
				sprite = Assets.blackPlatform;
				break;
			case GLUE:
				sprite = Assets.bluePlatform;
				break;
			case NORMAL:
				sprite = Assets.blackPlatform;
				break;
			}

			if (platform.hasSpikes
					&& platform.platformType == PLATFORM_TYPE.SPIKES) {
				sprite.setBounds(platform.getLocation().x
						- Platform.PLATFORM_WIDTH / 2,
						platform.getLocation().y - 0.5f,
						Platform.PLATFORM_WIDTH, 1.25f);
			} else {
				sprite.setBounds(platform.getLocation().x
						- Platform.PLATFORM_WIDTH / 2,
						platform.getLocation().y - 0.5f,
						Platform.PLATFORM_WIDTH, 1);
			}
			sprite.draw(batch);

			if (!platform.isTaken) {
				if (platform.hasCoin) {
					Sprite coin = Assets.currencySprite;
					coin.setBounds(platform.getLocation().x - 0.5f,
							platform.getLocation().y + 0.6f, 1f, 1f);
					coin.draw(batch);
				}

				if (platform.hasShield) {
					Sprite coin = Assets.shieldSprite;
					coin.setBounds(platform.getLocation().x - 0.5f,
							platform.getLocation().y + 0.6f, 1f, 1f);
					coin.draw(batch);
				}
			}
		}

		if (world.thief.isGlued) {
			Sprite cage = Assets.cage;
			cage.setBounds(world.thief.getPosition().x
					- Platform.PLATFORM_WIDTH / 2, world.thief.getPosition().y
					- Dude.THIEF_RADIUS, Platform.PLATFORM_WIDTH, 2.5f);
			cage.draw(batch);
		}

		if (world.thief.isShielded) {
			batch.setProjectionMatrix(hudCam.combined);

			Sprite shield = Assets.shieldSprite;
			shield.setBounds(shieldRect.x, shieldRect.y, shieldRect.width,
					shieldRect.height);
			shield.draw(batch);

			Sprite bar = Assets.greenPlatform;
			bar.setBounds(shieldBar.x, shieldBar.y, shieldBar.width
					* (1 - world.thief.getShieldPercentage()), shieldBar.height);
			bar.draw(batch);
		}

		batch.end();

	}
}
