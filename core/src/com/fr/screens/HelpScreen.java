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

public class HelpScreen extends ScreenAdapter {
	private BallHop gameRef;
	private SpriteBatch batch;
	private Vector3 touchPoint;
	private OrthographicCamera camera;

	private Rectangle coin;
	private Rectangle coinP1;
	private Rectangle coinP2;

	private Rectangle backButton;

	private Rectangle safe;
	private Rectangle spike;
	private Rectangle glue;
	private Rectangle quicksand;

	private Rectangle white1;
	private Rectangle white2;
	private Rectangle white3;

	public Screen onDoneScreen;

	public HelpScreen(BallHop gameRef) {
		this.gameRef = gameRef;
		this.batch = gameRef.batch;
		this.touchPoint = new Vector3();
		this.camera = new OrthographicCamera();
		this.camera.setToOrtho(false, 800, 480);
		this.coin = new Rectangle(580, 260 + 50, 40, 40);
		this.safe = new Rectangle(50, 278 - 10 + 50, 100, 25);
		this.spike = new Rectangle(50, 278 - 50 - 10 + 50, 100, 25);
		this.quicksand = new Rectangle(50, 278 - 100 - 10 + 50, 100, 25);
		this.glue = new Rectangle(50, 278 - 150 - 10 + 50, 100, 25);
		this.coinP2 = new Rectangle(550, 150 + 50, 100, 25);
		this.coinP1 = new Rectangle(550, 120 + 45, 100, 25);
		this.backButton = new Rectangle(0, 20, 800, 60);

		this.white1 = new Rectangle(25, 380, 750, 80);

		this.white2 = new Rectangle(25, 135, 400, 230);
		this.white3 = new Rectangle(25 + 400 + 10, 135, 340, 230);
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		updateScreen(delta);
		renderScreen(delta);
	}

	String helpMsg = "HELP";
	String leftMsg = "Touch Left For Short Jump";
	String rightMsg = "Touch Right For Long Jump";
	String safeMsg = "Safe Platform";
	String spikeMsg = "Has Spikes";
	String quickSand = "Sucks You In";
	String glueMsg = "Makes You Stick";
	String protects = "protects you from";
	String gotIt = "GOT IT !";

	public int numTouched;

	private void renderScreen(float delta) {
		Gdx.gl.glClearColor(0.35f, 0.35f, 0.35f, 0.35f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.setProjectionMatrix(camera.combined);
		batch.begin();

		Sprite whiteBase = Assets.whiteBack;

		whiteBase.setBounds(white1.x, white1.y - 270, white1.width,
				white1.height);
		whiteBase.draw(batch);

		whiteBase.setBounds(white2.x, white2.y + 80, white2.width,
				white2.height);
		whiteBase.draw(batch);

		whiteBase.setBounds(white3.x, white3.y + 80, white3.width,
				white3.height);
		whiteBase.draw(batch);

		BitmapFont font55 = Assets.font45;
		TextBounds bound = font55.getBounds(helpMsg);

		BitmapFont font25 = Assets.font25;
		bound = font25.getBounds(leftMsg);
		font25.draw(batch, leftMsg, 200 - bound.width / 2,
				420 + bound.height / 2 - 270);

		bound = font25.getBounds(rightMsg);
		font25.draw(batch, rightMsg, 600 - bound.width / 2,
				420 + bound.height / 2 - 270);

		bound = font25.getBounds(safeMsg);
		font25.draw(batch, safeMsg, 200, 300 - 10 + 50 + 80);

		bound = font25.getBounds(spikeMsg);
		font25.draw(batch, spikeMsg, 200, 250 - 10 + 50 + 80);

		bound = font25.getBounds(quickSand);
		font25.draw(batch, quickSand, 200, 200 - 10 + 50 + 80);

		bound = font25.getBounds(glueMsg);
		font25.draw(batch, glueMsg, 200, 150 - 10 + 50 + 80);

		bound = font25.getBounds(protects);
		font25.draw(batch, protects, 600 - bound.width / 2, 240 - bound.height
				/ 2 + 50 + 80);

		Sprite redPlatform = Assets.spikePlatform;
		redPlatform.setBounds(spike.x, spike.y + 80, spike.width, spike.height);
		redPlatform.draw(batch);

		Sprite bluePlatform = Assets.bluePlatform;
		bluePlatform.setBounds(glue.x, glue.y + 80, glue.width, glue.height);
		bluePlatform.draw(batch);

		Sprite blackPlatform = Assets.blackPlatform;
		blackPlatform.setBounds(safe.x, safe.y + 80, safe.width, safe.height);
		blackPlatform.draw(batch);

		Sprite greenPlatform = Assets.greenPlatform;
		greenPlatform.setBounds(quicksand.x, quicksand.y + 80, quicksand.width,
				quicksand.height);
		greenPlatform.draw(batch);

		Sprite shield = Assets.shieldSprite;
		shield.setBounds(coin.x, coin.y + 80, coin.width, coin.height);
		shield.draw(batch);

		redPlatform.setBounds(coinP1.x, coinP1.y + 80, coinP1.width,
				coinP1.height);
		redPlatform.draw(batch);

		bluePlatform.setBounds(coinP2.x, coinP2.y + 80, coinP2.width,
				coinP2.height);
		bluePlatform.draw(batch);

		Sprite base = Assets.buttonBase;
		base.setBounds(backButton.x, backButton.y, backButton.width,
				backButton.height);
		base.draw(batch);

		BitmapFont backF = Assets.font25W;
		
		TextBounds backB = backF.getBounds(gotIt);
		backF.draw(batch, gotIt, backButton.x + backButton.width / 2
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
				gameRef.setScreen(onDoneScreen);
				numTouched += 1;
			}
		}
	}
}
