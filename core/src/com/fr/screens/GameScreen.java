package com.fr.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.fr.entities.GameRenderer;
import com.fr.entities.GameWorld;
import com.fr.entities.GameWorld.WorldState;
import com.fr.game.BallHop;
import com.fr.utils.Assets;
import com.fr.utils.Session;
import com.fr.utils.Settings;

public class GameScreen extends ScreenAdapter {

	GameWorld world;
	GameRenderer renderer;
	OrthographicCamera hudCam;
	BallHop gameRef;
	SpriteBatch batch;

	ShapeRenderer shapeRenderer;

	Color redTint;

	private Rectangle saveMeForMoney;
	private Rectangle saveMeForFree;
	private Rectangle closeButton;
	private Rectangle timerButton;

	private Rectangle storeButton;
	private Rectangle leaderboardButton;
	private Rectangle shareButton;
	private Rectangle reviewButton;
	private Rectangle resumeButton;

	private Rectangle soundButton;
	private Rectangle pauseButton;

	private Rectangle restartButton;
	private Rectangle pausedBackButton;
	private Rectangle continueButton;

	private Rectangle tapRectangle;

	private Vector3 touchPoint;

	private int numResume;

	public enum GameScreenState {
		WAITING, RUNNING, PAUSED, SAVE_ME, GAMEOVER
	}

	private final float SAVE_ME_TIMER = 3.0f;
	private float runningTime = 0;

	public GameScreenState currentState;

	private String[] countDown = { "1", "2", "3" };

	private int countDownValue;
	private String scoreMsg = "SCORE";

	public GameScreen(final BallHop gameRef) {
		this.gameRef = gameRef;
		this.world = new GameWorld(gameRef);
		this.renderer = new GameRenderer(world);
		this.hudCam = new OrthographicCamera();
		this.hudCam.setToOrtho(false, 800, 480);

		changeState(GameScreenState.WAITING);

		this.batch = gameRef.batch;
		this.shapeRenderer = new ShapeRenderer();
		this.runningTime = 0;
		this.touchPoint = new Vector3();

		this.saveMeForFree = new Rectangle(200, 200, 400, 80);
		this.saveMeForMoney = new Rectangle(200, 290, 400, 80);
		this.closeButton = new Rectangle(160, 80, 80, 80);
		this.timerButton = new Rectangle(640 - 100, 80, 80, 80);

		this.pausedBackButton = new Rectangle(250, 290, 300, 80);
		this.restartButton = new Rectangle(250, 200, 300, 80);
		this.continueButton = new Rectangle(250, 110, 300, 80);

		this.resumeButton = new Rectangle(650 - 340 / 2 - 35, 75, 340 - 50, 110);
		this.storeButton = new Rectangle(650 - 170 - 35, 75 + 110, 170 - 25,
				110);
		this.reviewButton = new Rectangle(650 - 60, 75 + 110, 170 - 25, 110);
		this.leaderboardButton = new Rectangle(650 - 170 - 35, 75 + 220,
				170 - 25, 110);
		this.shareButton = new Rectangle(650 - 60, 75 + 220, 170 - 25, 110);

		this.soundButton = new Rectangle(800 - 10 - 130, 430, 30, 30);
		this.pauseButton = new Rectangle(800 - 10 - 60, 430, 30, 30);

		this.tapRectangle = new Rectangle(360, 20, 80, 80);
		this.redTint = new Color(231 / 255f, 78 / 255f, 89 / 255f, 1f);

	}

	private int numRetries;

	public void render(float delta) {
		GL20 gl = Gdx.gl;
		gl.glClearColor(1, 1, 1, 1);
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		updateScreen(delta);
		renderScreen(delta);
	}

	private void renderScreen(float delta) {
		switch (currentState) {
		case WAITING:
			renderWaiting(delta);
			break;
		case RUNNING:
			renderRunning(delta);
			break;
		case PAUSED:
			renderPaused(delta);
			break;
		case SAVE_ME:
			renderSaveMe(delta);
			break;
		case GAMEOVER:
			renderGameover(delta);
			break;
		}
	}

	private void renderGameover(float delta) {
		batch.setProjectionMatrix(hudCam.combined);
		batch.begin();
		Sprite resume = Assets.resumeButton;
		resume.setBounds(resumeButton.x, resumeButton.y, resumeButton.width,
				resumeButton.height);
		resume.draw(batch);

		Sprite share = Assets.shareButton;
		share.setBounds(shareButton.x, shareButton.y, shareButton.width,
				shareButton.height);
		share.draw(batch);

		Sprite leader = Assets.leaderboardButton;
		leader.setBounds(leaderboardButton.x, leaderboardButton.y,
				leaderboardButton.width, leaderboardButton.height);
		leader.draw(batch);

		Sprite store = Assets.storeButton;
		store.setBounds(storeButton.x, storeButton.y, storeButton.width,
				storeButton.height);
		store.draw(batch);

		Sprite rate = Assets.reviewButton;
		rate.setBounds(reviewButton.x, reviewButton.y, reviewButton.width,
				reviewButton.height);
		rate.draw(batch);

		BitmapFont font = Assets.font65;
		TextBounds bound = font.getBounds(scoreMsg);
		font.draw(batch, scoreMsg, 200 - bound.width / 2, 400);

		font = Assets.font55;
		String scoreString = world.getScore();
		bound = font.getBounds(scoreString);
		font.draw(batch, scoreString, 200 - bound.width / 2, 320);

		font = Assets.font35;
		String bestMsg = "BEST : " + Settings.highscores[0];
		bound = font.getBounds(bestMsg);
		font.draw(batch, bestMsg, 200 - bound.width / 2, 120);

		font = Assets.font45;
		String numCoin = " x " + Settings.numCoins;
		bound = font.getBounds(numCoin);
		font.draw(batch, numCoin, 200 - bound.width / 2 + 10, 215);

		Sprite coin = Assets.currencySprite;
		coin.setBounds(200 - bound.width / 2 - bound.height,
				215 - bound.height, bound.height, bound.height);
		coin.draw(batch);

		batch.end();

	}

	private void renderSaveMe(float delta) {
		batch.setProjectionMatrix(hudCam.combined);
		batch.begin();
		Sprite blue = Assets.bluePlatform;
		blue.setAlpha(gameRef.actionResolver.isVideoAdAvailable() ? 1 : 0.5f);
		blue.setBounds(saveMeForFree.x, saveMeForFree.y, saveMeForFree.width,
				saveMeForFree.height);
		blue.draw(batch);
		blue.setAlpha(1);

		Sprite green = Assets.blackPlatform;

		long coinsReq = (long) (Math.pow(2, numRetries) * 10);
		if (Settings.numCoins < coinsReq) {
			green.setAlpha(0.5f);
		}

		green.setBounds(saveMeForMoney.x, saveMeForMoney.y,
				saveMeForMoney.width, saveMeForMoney.height);
		green.draw(batch);
		green.setAlpha(1);

		BitmapFont font = Assets.font25W;
		String text = "SAVE ME FOR " + coinsReq;
		TextBounds bound = font.getBounds(text);
		font.draw(batch, text, saveMeForMoney.x + saveMeForMoney.width / 2
				- bound.width / 2 - 30, saveMeForMoney.y
				+ saveMeForMoney.height / 2 + bound.height / 2);

		Sprite cam = Assets.currencySprite;
		cam.setBounds(saveMeForMoney.x + saveMeForMoney.width / 2 + bound.width
				/ 2 - 10, saveMeForMoney.y + saveMeForMoney.height / 2 - 15,
				30, 30);
		cam.draw(batch);

		font = Assets.font25W;
		text = "SAVE ME FOR FREE";
		bound = font.getBounds(text);
		font.draw(batch, text, saveMeForFree.x + saveMeForFree.width / 2
				- bound.width / 2 - 30, saveMeForFree.y + saveMeForFree.height
				/ 2 + bound.height / 2);

		cam = Assets.cameraIcon;
		cam.setBounds(saveMeForFree.x + saveMeForFree.width - 90,
				saveMeForFree.y + saveMeForFree.height / 2 - 15, 40, 30);
		cam.draw(batch);

		Sprite close = Assets.closeButton;
		close.setBounds(closeButton.x, closeButton.y, closeButton.width,
				closeButton.height);
		close.draw(batch);

		batch.end();

		shapeRenderer.setProjectionMatrix(hudCam.combined);
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(redTint);
		shapeRenderer.rect(timerButton.x, timerButton.y, timerButton.width,
				timerButton.height);

		float angle = Math.min(360f, runningTime / SAVE_ME_TIMER * 360f);
		shapeRenderer.setColor(Color.WHITE);
		shapeRenderer.arc(timerButton.x + timerButton.width / 2, timerButton.y
				+ timerButton.height / 2, timerButton.width / 2 - 20, 90,
				360 - angle);

		shapeRenderer.end();
	}

	private void renderPaused(float delta) {
		batch.setProjectionMatrix(hudCam.combined);
		batch.begin();

		Sprite red = Assets.redPlatform;
		red.setBounds(restartButton.x, restartButton.y, restartButton.width,
				restartButton.height);
		red.draw(batch);

		BitmapFont font = Assets.font25W;
		String text = "RESTART";
		TextBounds bound = font.getBounds(text);
		font.draw(batch, text, restartButton.x + restartButton.width / 2
				- bound.width / 2, restartButton.y + restartButton.height / 2
				+ bound.height / 2);

		Sprite blue = Assets.bluePlatform;
		blue.setBounds(pausedBackButton.x, pausedBackButton.y,
				pausedBackButton.width, pausedBackButton.height);
		blue.draw(batch);

		text = "BACK";
		bound = font.getBounds(text);
		font.draw(batch, text, pausedBackButton.x + pausedBackButton.width / 2
				- bound.width / 2, pausedBackButton.y + pausedBackButton.height
				/ 2 + bound.height / 2);

		Sprite green = Assets.greenPlatform;
		green.setBounds(continueButton.x, continueButton.y,
				continueButton.width, continueButton.height);
		green.draw(batch);

		text = "CONTINUE";
		bound = font.getBounds(text);
		font.draw(batch, text, continueButton.x + continueButton.width / 2
				- bound.width / 2, continueButton.y + continueButton.height / 2
				+ bound.height / 2);
		batch.end();
	}

	private int numTaps = 0;

	private void renderRunning(float delta) {
		renderer.render(delta);

		batch.setProjectionMatrix(hudCam.combined);
		batch.begin();

		if (world.thief.isGlued) {
			Sprite red = Assets.unlockButton;
			red.setAlpha(0.85f);
			red.setBounds(tapRectangle.x, tapRectangle.y, tapRectangle.width,
					tapRectangle.height);
			red.draw(batch);
			red.setAlpha(1);

			BitmapFont font = Assets.font25;
			String text = "< TAP";
			TextBounds bound = font.getBounds(text);
			font.draw(batch, text, tapRectangle.x + tapRectangle.width + 25,
					tapRectangle.y + tapRectangle.height / 2 + bound.height / 2);

			text = "TAP >";
			bound = font.getBounds(text);
			font.draw(batch, text, tapRectangle.x - 25 - bound.width,
					tapRectangle.y + tapRectangle.height / 2 + bound.height / 2);
		}

		TextBounds bound = Assets.font45.getBounds(world.getScore());
		Assets.font45.draw(batch, world.getScore(), 400 - bound.width / 2,
				420 - bound.height / 2);

		bound = Assets.font35.getBounds(Settings.numCoins + "");
		Assets.font35.draw(batch, "x " + Settings.numCoins, 30 + bound.height,
				480 - bound.height / 2 - 10);

		Sprite coin = Assets.currencySprite;
		coin.setBounds(20, 468 - bound.height - 10, bound.height, bound.height);
		coin.draw(batch);

		Sprite pause = Assets.pauseIcon;
		pause.setBounds(pauseButton.x, pauseButton.y, pauseButton.width,
				pauseButton.height);
		pause.draw(batch);

		Sprite soundOn = Settings.soundOn ? Assets.soundOffIcon
				: Assets.soundOnIcon;
		soundOn.setBounds(soundButton.x, soundButton.y, soundButton.width,
				soundButton.height);
		soundOn.draw(batch);

		batch.end();

	}

	private void renderWaiting(float delta) {
		renderRunning(delta);
		TextBounds bound = Assets.font55
				.getBounds(countDown[countDownValue - 1]);
		batch.setProjectionMatrix(hudCam.combined);
		batch.begin();
		Assets.font55.draw(batch, countDown[countDownValue - 1],
				400 - bound.width / 2, 240 + bound.height / 2);
		batch.end();
	}

	private void updateScreen(float delta) {
		switch (currentState) {
		case WAITING:
			updateWaiting(delta);
			break;
		case RUNNING:
			updateRunning(delta);
			break;
		case PAUSED:
			updatePaused(delta);
			break;
		case SAVE_ME:
			updateSaveMe(delta);
			break;
		case GAMEOVER:
			updateGameover(delta);
			break;
		}
	}

	private void updateGameover(float delta) {
		if (Gdx.input.justTouched()) {
			hudCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(),
					0));
			if (resumeButton.contains(touchPoint.x, touchPoint.y)) {
				Assets.playSound(Assets.clickSound);
				numResume += 1;
				if (numResume == 4) {
					gameRef.actionResolver.showOrLoadInterstital();
					numResume = 0;
				}
				world.reset();
				changeState(GameScreenState.RUNNING);
			} else if (leaderboardButton.contains(touchPoint.x, touchPoint.y)) {
				Assets.playSound(Assets.clickSound);
				if (gameRef.actionResolver.getSignedInGPGS()) {
					gameRef.actionResolver.getLeaderboardGPGS();
				} else {
					BallHop.highscoresScreen.onDoneScreen = BallHop.gameScreen;
					gameRef.setScreen(BallHop.highscoresScreen);
				}
			} else if (shareButton.contains(touchPoint.x, touchPoint.y)) {
				Assets.playSound(Assets.clickSound);
				gameRef.actionResolver.shareAppLink(world.gameScore);
			} else if (reviewButton.contains(touchPoint.x, touchPoint.y)) {
				Assets.playSound(Assets.clickSound);
				gameRef.actionResolver.rateApp();
			} else if (storeButton.contains(touchPoint.x, touchPoint.y)) {
				Assets.playSound(Assets.clickSound);
				BallHop.storeScreen.onDoneScreen = BallHop.gameScreen;
				gameRef.setScreen(BallHop.storeScreen);
			}
		}
	}

	private void onGameOver() {
		Session.maxScore = Math.max(Session.maxScore, world.gameScore);
		Settings.addScore(Session.maxScore);
		Settings.save(gameRef);
		if (gameRef.actionResolver.getSignedInGPGS()) {
			gameRef.actionResolver.submitScoreGPGS(Session.maxScore);
		}
		changeState(GameScreenState.GAMEOVER);
	}

	private void updateSaveMe(float delta) {
		runningTime += delta;

		if (runningTime >= SAVE_ME_TIMER) {
			onGameOver();
			return;
		}

		if (Gdx.input.justTouched()) {
			hudCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(),
					0));
			if (saveMeForFree.contains(touchPoint.x, touchPoint.y)
					&& gameRef.actionResolver.isVideoAdAvailable()) {
				Assets.playSound(Assets.clickSound);
				gameRef.actionResolver.playVideoAd();
				world.resume();
				changeState(GameScreenState.WAITING);
				gameRef.actionResolver.sendTracker("Used Save Me", "Free", 1);
			} else if (saveMeForMoney.contains(touchPoint.x, touchPoint.y)) {
				Assets.playSound(Assets.clickSound);
				numResume += 1;
				if (numResume == 4) {
					gameRef.actionResolver.showOrLoadInterstital();
					numResume = 0;
				}

				long coinsReq = (long) (Math.pow(2, numRetries) * 10);
				if (Settings.numCoins >= coinsReq) {
					Settings.numCoins -= coinsReq;
					numRetries += 1;
					world.resume();
					changeState(GameScreenState.WAITING);
					gameRef.actionResolver.sendTracker("Used Save Me", "Coins",
							numRetries);
				}

			} else if (closeButton.contains(touchPoint.x, touchPoint.y)) {
				Assets.playSound(Assets.clickSound);
				onGameOver();
			}
		}
	}

	public void changeState(GameScreenState state) {
		runningTime = 0;
		if (state == GameScreenState.WAITING) {
			countDownValue = 2;
		}
		if (state == GameScreenState.GAMEOVER) {
			numRetries = 0;
		}
		currentState = state;
	}

	private void updatePaused(float delta) {
		if (Gdx.input.justTouched()) {
			hudCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(),
					0));
			if (restartButton.contains(touchPoint.x, touchPoint.y)) {
				Assets.playSound(Assets.clickSound);
				world.reset();
				changeState(GameScreenState.RUNNING);
			} else if (pausedBackButton.contains(touchPoint.x, touchPoint.y)) {
				Assets.playSound(Assets.clickSound);
				gameRef.setScreen(new MainScreen(gameRef));
			} else if (continueButton.contains(touchPoint.x, touchPoint.y)) {
				Assets.playSound(Assets.clickSound);
				changeState(GameScreenState.RUNNING);
			}
		}
	}

	private void updateRunning(float delta) {

		if (world.state == WorldState.GAME_OVER) {
			long coinsReq = (long) (Math.pow(2, numRetries) * 10);
			if (Settings.numCoins < coinsReq
					&& !gameRef.actionResolver.isVideoAdAvailable()) {
				changeState(GameScreenState.GAMEOVER);
			} else {
				changeState(GameScreenState.SAVE_ME);
			}
			return;
		}

		if (Gdx.input.justTouched()) {
			hudCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(),
					0));
			if (tapRectangle.contains(touchPoint.x, touchPoint.y)) {
				Assets.playSound(Assets.clickSound);
				if (world.thief.isGlued) {
					numTaps = numTaps + 1;
					if (numTaps == 5) {
						numTaps = 0;
						world.unGlue();
					}
				}
			} else if (pauseButton.contains(touchPoint.x, touchPoint.y)) {
				Assets.playSound(Assets.clickSound);
				changeState(GameScreenState.PAUSED);
			} else if (soundButton.contains(touchPoint.x, touchPoint.y)) {
				Assets.playSound(Assets.clickSound);
				Settings.soundOn = !Settings.soundOn;
				if (!Settings.soundOn) {
					Assets.pauseMusic();
				} else {
					Assets.playMusic();
				}
			}
		}

		if (Gdx.input.isTouched()) {
			hudCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(),
					0));
			if (touchPoint.x <= 300 && touchPoint.y <= 380) {
				world.thief.shortJump();
			} else if (touchPoint.x >= 500 && touchPoint.y <= 380) {
				world.thief.longJump();
			}
		}

		world.update(delta);

	}

	private void updateWaiting(float delta) {
		runningTime += delta;
		if (runningTime >= 1.0f) {
			countDownValue = Math.max(0, countDownValue - 1);
			runningTime = 0;
			if (countDownValue == 0) {
				changeState(GameScreenState.RUNNING);
			}
		}
	};

	@Override
	public void show() {
		super.show();
	}

	@Override
	public void hide() {
		super.hide();
	}
}
