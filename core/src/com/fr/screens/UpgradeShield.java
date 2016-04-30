package com.fr.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.fr.game.BallHop;
import com.fr.utils.Assets;
import com.fr.utils.Settings;

public class UpgradeShield extends ScreenAdapter {
	BallHop gameRef;
	SpriteBatch batch;
	Vector3 touchPoint;
	OrthographicCamera camera;

	private Rectangle backButton;
	private Rectangle shieldRect;
	private Rectangle upgradeButton;

	public UpgradeShield(BallHop gameRef) {
		this.gameRef = gameRef;
		this.batch = gameRef.batch;
		this.touchPoint = new Vector3();
		this.camera = new OrthographicCamera();
		this.camera.setToOrtho(false, 800, 480);
		this.backButton = new Rectangle(0, 20, 800, 60);
		this.shieldRect = new Rectangle(175 - 40, 240, 80, 80);
		this.upgradeButton = new Rectangle(500 - 150, 150, 300, 50);
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

		Sprite back = Assets.bluePlatform;
		back.setBounds(backButton.x, backButton.y, backButton.width,
				backButton.height);
		back.draw(batch);

		Sprite shield = Assets.shieldSprite;
		shield.setBounds(shieldRect.x, shieldRect.y, shieldRect.width,
				shieldRect.height);
		shield.draw(batch);

		int cost = (int) (Math.pow(2, Settings.shieldLevel) * 100);
		Sprite red = Assets.redPlatform;
		if (Settings.numCoins < cost) {
			red.setAlpha(0.5f);
		}
		red.setBounds(upgradeButton.x, upgradeButton.y, upgradeButton.width,
				upgradeButton.height);
		red.draw(batch);
		red.setAlpha(1);

		BitmapFont font = Assets.font25W;
		String text = "UPGRADE";
		TextBounds bound = font.getBounds(text);
		font.draw(batch, text, upgradeButton.x + upgradeButton.width / 2
				- bound.width / 2, upgradeButton.y + upgradeButton.height / 2
				+ bound.height / 2);

		font = Assets.font25W;
		text = "DONE";
		bound = font.getBounds(text);
		font.draw(batch, text, backButton.x + backButton.width / 2
				- bound.width / 2, backButton.y + backButton.height / 2
				+ bound.height / 2);

		String currentLevel = "Lv " + Settings.shieldLevel;
		String nextLevel = "LEVEL " + (Settings.shieldLevel + 1);
		String levelCost = String.valueOf((int) (Math.pow(2,
				Settings.shieldLevel) * 100));
		String levelSubMsg = "Lasts for "
				+ (Settings.shieldMultiplier * Math
						.pow(2, Settings.shieldLevel)) + " seconds";

		BitmapFont font35 = Assets.font35;

		TextBounds bound35 = font35.getBounds(currentLevel);
		font35.draw(batch, currentLevel, 175 - bound35.width / 2,
				200 - bound35.height / 2);

		BitmapFont font45 = Assets.font45;
		TextBounds bound45 = font45.getBounds(nextLevel);
		font45.draw(batch, nextLevel, 500 - bound45.width / 2,
				400 - bound45.height / 2);

		BitmapFont font25 = Assets.font25;
		TextBounds bound25 = font25.getBounds(levelSubMsg);
		font25.draw(batch, levelSubMsg, 500 - bound25.width / 2,
				340 - bound25.height / 2);

		bound35 = font35.getBounds(levelCost);
		font35.draw(batch, levelCost, 500 - bound35.width / 2 - 15,
				280 - bound35.height / 2);

		Sprite coin = Assets.currencySprite;
		coin.setBounds(500 + bound35.width / 2, 280 - bound35.height
				- bound35.height / 2f, bound35.height, bound35.height);
		coin.draw(batch);

		bound = Assets.font35.getBounds(Settings.numCoins + "");
		Assets.font35.draw(batch, "x " + Settings.numCoins, 30 + bound.height,
				480 - bound.height / 2 - 10);

		coin.setBounds(20, 468 - bound.height - 10, bound.height, bound.height);
		coin.draw(batch);

		batch.end();
	}

	private void updateScreen(float delta) {
		if (Gdx.input.justTouched()) {
			camera.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(),
					0));
			if (backButton.contains(touchPoint.x, touchPoint.y)) {
				Assets.playSound(Assets.clickSound);
				gameRef.setScreen(BallHop.storeScreen);
			} else if (upgradeButton.contains(touchPoint.x, touchPoint.y)) {
				Assets.playSound(Assets.clickSound);
				int cost = (int) (Math.pow(2, Settings.shieldLevel) * 100);
				if (Settings.numCoins >= cost) {
					Settings.numCoins -= cost;
					Settings.shieldLevel += 1;
					Settings.save(gameRef);
					gameRef.actionResolver.sendTracker("Upgrade Shield", "Level "+Settings.shieldLevel, 1);
				}
			}
		}
	}
}
