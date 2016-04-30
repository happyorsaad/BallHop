package com.fr.screens;

import com.badlogic.gdx.Gdx;
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
import com.fr.utils.Settings;

public class SettingScreen extends ScreenAdapter {

	BallHop gameRef;
	SpriteBatch batch;
	Vector3 touchPoint;
	OrthographicCamera camera;

	private Rectangle soundButton;
	private Rectangle googleSignIn;
	private Rectangle backButton;

	public SettingScreen(BallHop gameRef) {
		this.gameRef = gameRef;
		this.batch = gameRef.batch;
		this.touchPoint = new Vector3();
		this.camera = new OrthographicCamera();
		this.camera.setToOrtho(false, 800, 480);

		this.soundButton = new Rectangle(200, 280, 400, 80);
		this.googleSignIn = new Rectangle(200, 180, 400, 80);
		this.backButton = new Rectangle(0, 20, 800, 60);
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		updateScreen(delta);
		renderScreen(delta);
	}

	private void renderScreen(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.setProjectionMatrix(camera.combined);
		batch.begin();

		Sprite redBack = Assets.redPlatform;
		redBack.setBounds(googleSignIn.x, googleSignIn.y, googleSignIn.width,
				googleSignIn.height);
		redBack.draw(batch);

		BitmapFont font = Assets.font25W;
		String text = "GOOGLE SIGN "
				+ (gameRef.actionResolver.getSignedInGPGS() ? "OUT " : "IN");
		TextBounds bound = font.getBounds(text);

		font.draw(batch, text, googleSignIn.x + googleSignIn.width / 2
				- bound.width / 2, googleSignIn.y + googleSignIn.height / 2
				+ bound.height / 2);

		Sprite greenBack = Assets.greenPlatform;
		greenBack.setBounds(soundButton.x, soundButton.y, soundButton.width,
				soundButton.height);
		greenBack.draw(batch);

		text = "SOUND " + (Settings.soundOn ? "OFF " : "ON");
		bound = font.getBounds(text);

		font.draw(batch, text, soundButton.x + soundButton.width / 2
				- bound.width / 2, soundButton.y + soundButton.height / 2
				+ bound.height / 2);

		Sprite blueBack = Assets.bluePlatform;
		blueBack.setBounds(backButton.x, backButton.y, backButton.width,
				backButton.height);
		blueBack.draw(batch);

		font = Assets.font25W;
		text = "DONE";
		bound = font.getBounds(text);
		font.draw(batch, text, backButton.x + backButton.width / 2
				- bound.width / 2, backButton.y + backButton.height / 2
				+ bound.height / 2);

		batch.end();
	}

	private void updateScreen(float delta) {
		if (Gdx.input.justTouched()) {
			camera.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(),
					0));
			if (backButton.contains(touchPoint.x, touchPoint.y)) {
				Assets.playSound(Assets.clickSound);
				gameRef.setScreen(new MainScreen(gameRef));
			} else if (soundButton.contains(touchPoint.x, touchPoint.y)) {
				Assets.playSound(Assets.clickSound);
				Settings.soundOn = !Settings.soundOn;
				if (!Settings.soundOn) {
					Assets.pauseMusic();
				} else {
					Assets.playMusic();
				}
			} else if (googleSignIn.contains(touchPoint.x, touchPoint.y)) {
				Assets.playSound(Assets.clickSound);
				if (gameRef.actionResolver.getSignedInGPGS()) {
					gameRef.actionResolver.signOutGPGS();
				} else {
					gameRef.actionResolver.loginGPGS();
				}
			}
		}
	}
}
