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

public class BuyCoins extends ScreenAdapter {
	BallHop gameRef;
	SpriteBatch batch;
	Vector3 touchPoint;
	OrthographicCamera camera;

	private Rectangle buy1000;
	private Rectangle buy3000;
	private Rectangle buy8000;
	private Rectangle buy100Free;
	private Rectangle backButton;
	private Rectangle coinRect;

	private Rectangle get100Coins;

	public BuyCoins(BallHop gameRef) {
		this.gameRef = gameRef;
		this.batch = gameRef.batch;
		this.touchPoint = new Vector3();
		this.camera = new OrthographicCamera();
		this.camera.setToOrtho(false, 800, 480);

		this.backButton = new Rectangle(0, 20, 800, 60);

		this.buy1000 = new Rectangle(500 - 250, 280, 250, 100);
		this.buy3000 = new Rectangle(500, 280, 250, 100);
		this.buy8000 = new Rectangle(500 - 250, 180, 250, 100);
		this.buy100Free = new Rectangle(500, 180, 250, 100);

		this.coinRect = new Rectangle(125 - 40, 300 - 40, 80, 80);

		this.get100Coins = new Rectangle(250, 250, 500, 80);
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

		String coinNumber = String.valueOf(Settings.numCoins);
		BitmapFont font35 = Assets.font35;
		TextBounds bound35 = font35.getBounds(coinNumber);
		font35.draw(batch, coinNumber, 125 - bound35.width / 2,
				240 - bound35.height / 2);

		Sprite currency = Assets.currencySprite;
		currency.setBounds(coinRect.x, coinRect.y, coinRect.width,
				coinRect.height);
		currency.draw(batch);

		Sprite red = Assets.redPlatform;
		red.setAlpha(gameRef.actionResolver.isVideoAdAvailable() ? 1 : 0.5f);
		red.setBounds(get100Coins.x, get100Coins.y, get100Coins.width,
				get100Coins.height);
		red.draw(batch);
		red.setAlpha(1);

		font35 = Assets.font25W;
		String text = "GET 100 COINS";
		bound35 = font35.getBounds(text);
		font35.draw(batch, text, get100Coins.x + get100Coins.width / 2
				- bound35.width / 2 - 30, get100Coins.y + get100Coins.height
				/ 2 + bound35.height / 2);

		Sprite video = Assets.cameraIcon;
		video.setBounds(get100Coins.x + get100Coins.width / 2 + bound35.width
				/ 2, get100Coins.y + get100Coins.height / 2 - 15, 40, 30);

		video.draw(batch);
		
		Sprite blue = Assets.bluePlatform;
		blue.setBounds(backButton.x, backButton.y, backButton.width,
				backButton.height);
		blue.draw(batch);

		BitmapFont backF = Assets.font25W;
		String backT = "DONE";
		TextBounds backB = backF.getBounds(backT);
		backF.draw(batch, backT, backButton.x + backButton.width / 2
				- backB.width / 2, backButton.y + backButton.height / 2
				+ backB.height / 2);

		batch.end();
	}

	private void updateScreen(float delta) {
		if (Gdx.input.justTouched()) {
			camera.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(),
					0));
			if (backButton.contains(touchPoint.x, touchPoint.y)) {
				Assets.playSound(Assets.clickSound);
				gameRef.setScreen(BallHop.storeScreen);
			} else if (get100Coins.contains(touchPoint.x, touchPoint.y)
					&& gameRef.actionResolver.isVideoAdAvailable()) {
				Assets.playSound(Assets.clickSound);
				gameRef.actionResolver.playVideoAd();
				Settings.numCoins += 100;
				Settings.save(gameRef);
				gameRef.actionResolver.sendTracker("Bought 100 Coins", "Video", 1);
			}
		}
	}
}
