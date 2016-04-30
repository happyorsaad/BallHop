package com.fr.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.fr.game.BallHop;
import com.fr.utils.Assets;
import com.fr.utils.Session;
import com.fr.utils.Settings;

public class SplashScreen extends ScreenAdapter {

	private BallHop gameRef;
	private float time;
	private final float SPLASH_TIME = 3.0f;

	private OrthographicCamera cam;

	SpriteBatch batcher;

	private Texture logo;
	private Sprite logoSprite;

	public SplashScreen(BallHop gameRef) {
		this.gameRef = gameRef;
		this.time = 0;
		this.cam = new OrthographicCamera();
		this.cam.setToOrtho(false, 800, 480);
		this.batcher = gameRef.batch;
		this.logo = new Texture(
				Gdx.files.internal("graphics/images/frIcon.png"));
		this.logoSprite = new Sprite(logo);
	}

	private volatile boolean first = true;

	@Override
	public void render(float delta) {
		if (first) {
			Settings.load(gameRef);
			Assets.load();
			if (Settings.soundOn) {
				Assets.playMusic();
			}
			first = false;

			long lastTimestamp = Settings.highscoresTS[0];
			long currentTimestamp = System.currentTimeMillis();

			String _updatedOn = gameRef.actionResolver
					.getSecureValue("UPLOADTIME");
			long updatedTime = Long.parseLong(_updatedOn == null ? "0"
					: _updatedOn);

			if (lastTimestamp == 0
					|| lastTimestamp >= (currentTimestamp - (24 * 60 * 60 * 1000))
					|| updatedTime < (currentTimestamp - (24 * 60 * 60 * 1000))
					|| updatedTime == 0)
				Session.maxScore = Settings.highscores[0];
		}
		
		updateScreen(delta);
		renderScreen(delta);
	}

	private void updateScreen(float delta) {
		time += delta;
		if (time >= SPLASH_TIME && !Assets.isLoading) {
			BallHop.gameScreen = new GameScreen(gameRef);
			BallHop.mainScreen = new MainScreen(gameRef);
			BallHop.helpScreen = new HelpScreen(gameRef);
			BallHop.settingsScreen = new SettingScreen(gameRef);
			BallHop.highscoresScreen = new HighScoreScreen(gameRef);
			BallHop.buyCoins = new BuyCoins(gameRef);
			BallHop.storeScreen = new StoreScreen(gameRef);
			BallHop.upgradeScreen = new UpgradeShield(gameRef);
			gameRef.setScreen(BallHop.mainScreen);
		}
	}

	private void renderScreen(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batcher.begin();
		batcher.setProjectionMatrix(cam.combined);
		logoSprite.setBounds(400 - 40, 240 - 25, 80, 50);
		logoSprite.draw(batcher);
		batcher.end();
	}

}
