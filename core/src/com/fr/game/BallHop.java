package com.fr.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.fr.entities.BackKeyHandler;
import com.fr.screens.BuyCoins;
import com.fr.screens.GameScreen;
import com.fr.screens.HelpScreen;
import com.fr.screens.HighScoreScreen;
import com.fr.screens.MainScreen;
import com.fr.screens.SettingScreen;
import com.fr.screens.SplashScreen;
import com.fr.screens.StoreScreen;
import com.fr.screens.UpgradeShield;

public class BallHop extends Game {

	public SpriteBatch batch;
	public ActionResolver actionResolver;
	boolean firstTimeCreate = true;

	public static GameScreen gameScreen;
	public static BuyCoins buyCoins;
	public static HelpScreen helpScreen;
	public static HighScoreScreen highscoresScreen;
	public static MainScreen mainScreen;
	public static SettingScreen settingsScreen;
	public static StoreScreen storeScreen;
	public static UpgradeShield upgradeScreen;

	public BallHop(ActionResolver actionResolver) {
		this.actionResolver = actionResolver;
	}

	@Override
	public void create() {
		batch = new SpriteBatch();

		BackKeyHandler handler = new BackKeyHandler(this);
		
		Gdx.input.setCatchBackKey(true);
		Gdx.input.setInputProcessor(handler);
		
		setScreen(new SplashScreen(this));
	}

	@Override
	public void render() {
		super.render();
	}

}