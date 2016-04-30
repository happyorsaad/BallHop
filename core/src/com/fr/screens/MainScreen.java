package com.fr.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.fr.game.BallHop;
import com.fr.screens.GameScreen.GameScreenState;
import com.fr.utils.Assets;

public class MainScreen extends ScreenAdapter {
	private BallHop gameRef;
	private SpriteBatch batch;
	private OrthographicCamera hudCam;

	private Rectangle playButton;
	private Rectangle storeButton;
	private Rectangle leaderboardButton;
	private Rectangle settingButton;
	private Rectangle helpButton;
	private Rectangle iconBounds;

	private Vector3 touchPoint;

	private String appName = "BALL HOP";

	private int numShown;

	public MainScreen(BallHop gameRef) {
		this.gameRef = gameRef;
		this.batch = gameRef.batch;
		this.hudCam = new OrthographicCamera();
		this.hudCam.setToOrtho(false, 800, 480);
		this.playButton = new Rectangle(650 - 340 / 2 - 55, 75, 340 - 50, 110);
		this.storeButton = new Rectangle(650 - 170 - 55, 75 + 110, 170 - 25,
				110);
		this.helpButton = new Rectangle(650 - 55 - 25, 75 + 110, 170 - 25, 110);
		this.leaderboardButton = new Rectangle(650 - 170 - 55, 75 + 220,
				170 - 25, 110);
		this.settingButton = new Rectangle(650 - 55 - 25, 75 + 220, 170 - 25,
				110);
		this.iconBounds = new Rectangle(210 - 250 / 2, 155, 250, 250);
		this.touchPoint = new Vector3();
		this.numShown = 0;
	}

	public void render(float delta) {
		super.render(delta);
		updateScreen(delta);
		renderScreen(delta);
	}

	private void renderScreen(float delta) {
		GL20 gl = Gdx.gl;
		gl.glClearColor(1, 1, 1, 1);
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		BitmapFont nameFont = Assets.font45;
		TextBounds bounds = nameFont.getBounds(appName);

		batch.setProjectionMatrix(hudCam.combined);
		batch.begin();
		Assets.font45.draw(batch, appName, 210 - bounds.width / 2f, 120);

		Sprite playSprite = Assets.playButton;
		playSprite.setBounds(playButton.x, playButton.y, playButton.width,
				playButton.height);
		playSprite.draw(batch);

		Sprite iconSprite = Assets.iconSprite;
		iconSprite.setBounds(iconBounds.x, iconBounds.y, iconBounds.width,
				iconBounds.height);
		iconSprite.draw(batch);

		Sprite settingSprite = Assets.settingsButton;
		settingSprite.setBounds(settingButton.x, settingButton.y,
				settingButton.width, settingButton.height);
		settingSprite.draw(batch);

		Sprite helpSprite = Assets.helpButton;
		helpSprite.setBounds(helpButton.x, helpButton.y, helpButton.width,
				helpButton.height);
		helpSprite.draw(batch);

		Sprite leaderboardSprite = Assets.leaderboardButton;
		leaderboardSprite.setBounds(leaderboardButton.x, leaderboardButton.y,
				leaderboardButton.width, leaderboardButton.height);
		leaderboardSprite.draw(batch);

		Sprite storeSprite = Assets.storeButton;
		storeSprite.setBounds(storeButton.x, storeButton.y, storeButton.width,
				storeButton.height);
		storeSprite.draw(batch);

		batch.end();

	}

	private void updateScreen(float delta) {

		if (Gdx.input.isKeyJustPressed(Keys.BACK)) {
			Gdx.app.exit();
		}

		if (Gdx.input.justTouched()) {
			hudCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(),
					0));
			if (playButton.contains(touchPoint.x, touchPoint.y)) {
				if (gameRef.actionResolver.isFirstTimeUser()
						&& BallHop.helpScreen.numTouched == 0) {
					BallHop.helpScreen.onDoneScreen = BallHop.gameScreen;
					gameRef.setScreen(BallHop.helpScreen);
				} else {
					BallHop.gameScreen.world.reset();
					BallHop.gameScreen.changeState(GameScreenState.WAITING);
					gameRef.setScreen(BallHop.gameScreen);
				}
				Assets.playSound(Assets.clickSound);
			} else if (settingButton.contains(touchPoint.x, touchPoint.y)) {
				gameRef.setScreen(BallHop.settingsScreen);
				Assets.playSound(Assets.clickSound);
			} else if (helpButton.contains(touchPoint.x, touchPoint.y)) {
				BallHop.helpScreen.onDoneScreen = BallHop.mainScreen;
				gameRef.setScreen(BallHop.helpScreen);
				Assets.playSound(Assets.clickSound);
			} else if (storeButton.contains(touchPoint.x, touchPoint.y)) {
				BallHop.storeScreen.onDoneScreen = BallHop.mainScreen;
				gameRef.setScreen(BallHop.storeScreen);
				Assets.playSound(Assets.clickSound);
			} else if (leaderboardButton.contains(touchPoint.x, touchPoint.y)) {
				if (gameRef.actionResolver.getSignedInGPGS()) {
					gameRef.actionResolver.getLeaderboardGPGS();
				} else {
					BallHop.highscoresScreen.onDoneScreen = BallHop.mainScreen;
					gameRef.setScreen(BallHop.highscoresScreen);
				}
				Assets.playSound(Assets.clickSound);
			}
		}
	}

	@Override
	public void show() {
		super.show();
		this.numShown += 1;
		if (numShown == 3) {
			gameRef.actionResolver.showOrLoadInterstital();
			numShown = 0;
		}
	}
}
