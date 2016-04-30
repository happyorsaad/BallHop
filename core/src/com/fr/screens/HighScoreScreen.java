package com.fr.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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
import com.fr.utils.Assets;
import com.fr.utils.Session;
import com.fr.utils.Settings;

public class HighScoreScreen extends ScreenAdapter {
	BallHop gameRef;
	SpriteBatch batch;
	Vector3 touchPoint;
	OrthographicCamera camera;

	private Rectangle backButton;
	private Rectangle getLeaderboard;

	public Screen onDoneScreen;

	public HighScoreScreen(BallHop gameRef) {
		this.gameRef = gameRef;
		this.batch = gameRef.batch;
		this.touchPoint = new Vector3();
		this.camera = new OrthographicCamera();
		this.camera.setToOrtho(false, 800, 480);
		this.backButton = new Rectangle(0, 20, 800, 60);
		this.getLeaderboard = new Rectangle(600 - 150, 175, 300, 60);
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		updateScreen(delta);
		renderScreen(delta);
	}

	String highScoreMsg = "Highscores";
	String leaderboardMsg = "Leaderboard";

	String signInMsg[] = new String[] { "Click To Sign In",
			"and View Leaderboard" };
	
	private boolean getLeaderBoard;

	private void renderScreen(float delta) {
		Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.setProjectionMatrix(camera.combined);
		batch.begin();

		Sprite base = Assets.buttonBase;
		base.setBounds(backButton.x, backButton.y, backButton.width,
				backButton.height);
		base.draw(batch);

		BitmapFont backF = Assets.font25W;
		String backT = "BACK";
		TextBounds backB = backF.getBounds(backT);
		backF.draw(batch, backT, backButton.x + backButton.width / 2
				- backB.width / 2, backButton.y + backButton.height / 2
				+ backB.height / 2);

		Sprite leader = Assets.greenPlatform;
		leader.setBounds(getLeaderboard.x, getLeaderboard.y,
				getLeaderboard.width, getLeaderboard.height);
		leader.draw(batch);

		backF = Assets.font25W;
		backT = "LEADERBOARD";
		backB = backF.getBounds(backT);
		backF.draw(batch, backT, getLeaderboard.x + getLeaderboard.width / 2
				- backB.width / 2, getLeaderboard.y + getLeaderboard.height / 2
				+ backB.height / 2);

		BitmapFont font55 = Assets.font45;
		TextBounds bound = font55.getBounds(highScoreMsg);
		font55.draw(batch, highScoreMsg, 200 - bound.width / 2,
				410 + bound.height / 2);

		bound = font55.getBounds(leaderboardMsg);
		font55.draw(batch, leaderboardMsg, 600 - bound.width / 2,
				410 + bound.height / 2);

		BitmapFont font25 = Assets.font25;
		for (int i = 0; i < Settings.highscores.length; i += 1) {
			bound = font25.getBounds(Settings.highscores[i] + "");
			font25.draw(batch, Settings.highscores[i] + "",
					300 - bound.width / 2, 350 - bound.height / 2 - i * 50);
			bound = font25.getBounds((i + 1) + ".");
			font25.draw(batch, (i + 1) + ".", 100 - bound.width / 2, 350
					- bound.height / 2 - i * 50);
		}

		for (int i = 0; i < signInMsg.length; i += 1) {
			bound = font25.getBounds(signInMsg[i]);
			font25.draw(batch, signInMsg[i], 600 - bound.width / 2, 350
					- bound.height / 2 - i * 50);
		}
		batch.end();
	}

	private void updateScreen(float delta) {
		if (Gdx.input.justTouched()) {
			this.camera.unproject(touchPoint.set(Gdx.input.getX(),
					Gdx.input.getY(), 0));
			if (backButton.contains(touchPoint.x, touchPoint.y)) {
				Assets.playSound(Assets.clickSound);
				gameRef.setScreen(onDoneScreen);
			}
		}

		if (gameRef.actionResolver.isConnecting()) {
			return;
		}

		if (gameRef.actionResolver.getSignedInGPGS() && getLeaderBoard) {
			getLeaderBoard = false;
			gameRef.actionResolver.submitScoreGPGS(Session.maxScore);
			gameRef.actionResolver.getLeaderboardGPGS();
		}

		if (Gdx.input.justTouched()) {
			this.camera.unproject(touchPoint.set(Gdx.input.getX(),
					Gdx.input.getY(), 0));
			if (backButton.contains(touchPoint.x, touchPoint.y)) {
				Assets.playSound(Assets.clickSound);
				gameRef.setScreen(onDoneScreen);
			} else if (getLeaderboard.contains(touchPoint.x, touchPoint.y)) {
				Assets.playSound(Assets.clickSound);
				if (!gameRef.actionResolver.getSignedInGPGS()) {
					getLeaderBoard = true;
					gameRef.actionResolver.getLeaderboardGPGS();
				} else {
					gameRef.actionResolver.getLeaderboardGPGS();
				}
			}
		}

	}
}
