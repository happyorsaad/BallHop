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

public class StoreScreen extends ScreenAdapter {
	BallHop gameRef;
	SpriteBatch batch;
	Vector3 touchPoint;
	OrthographicCamera camera;

	private Rectangle buyCoins;
	private Rectangle upgradeShield;
	private Rectangle backButton;

	public Screen onDoneScreen;

	public StoreScreen(BallHop gameRef) {
		this.gameRef = gameRef;
		this.batch = gameRef.batch;
		this.touchPoint = new Vector3();
		this.camera = new OrthographicCamera();
		this.camera.setToOrtho(false, 800, 480);
		this.buyCoins = new Rectangle(200, 280, 400, 80);
		this.upgradeShield = new Rectangle(200, 180, 400, 80);
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
		redBack.setBounds(upgradeShield.x, upgradeShield.y,
				upgradeShield.width, upgradeShield.height);
		redBack.draw(batch);

		BitmapFont font = Assets.font25W;
		String text = "UPGRADE SHIELD";
		TextBounds bound = font.getBounds(text);

		font.draw(batch, text, upgradeShield.x + upgradeShield.width / 2
				- bound.width / 2, upgradeShield.y + upgradeShield.height / 2
				+ bound.height / 2);

		Sprite greenBack = Assets.greenPlatform;
		greenBack.setBounds(buyCoins.x, buyCoins.y, buyCoins.width,
				buyCoins.height);
		greenBack.draw(batch);

		text = "GET COINS";
		bound = font.getBounds(text);

		font.draw(batch, text, buyCoins.x + buyCoins.width / 2 - bound.width
				/ 2, buyCoins.y + buyCoins.height / 2 + bound.height / 2);

		Sprite blueBack = Assets.bluePlatform;
		blueBack.setBounds(backButton.x, backButton.y, backButton.width,
				backButton.height);
		blueBack.draw(batch);

		text = "BACK";
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
				gameRef.setScreen(onDoneScreen);
			} else if (buyCoins.contains(touchPoint.x, touchPoint.y)) {
				Assets.playSound(Assets.clickSound);
				gameRef.setScreen(BallHop.buyCoins);
			} else if (upgradeShield.contains(touchPoint.x, touchPoint.y)) {
				Assets.playSound(Assets.clickSound);
				gameRef.setScreen(BallHop.upgradeScreen);
			}
		}
	}
}
