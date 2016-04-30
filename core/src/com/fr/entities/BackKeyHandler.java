package com.fr.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.fr.game.BallHop;
import com.fr.screens.BuyCoins;
import com.fr.screens.GameScreen;
import com.fr.screens.GameScreen.GameScreenState;
import com.fr.screens.HelpScreen;
import com.fr.screens.HighScoreScreen;
import com.fr.screens.MainScreen;
import com.fr.screens.SettingScreen;
import com.fr.screens.StoreScreen;
import com.fr.screens.UpgradeShield;

public class BackKeyHandler extends InputAdapter {

	BallHop gameRef;

	public BackKeyHandler(BallHop gameref) {
		this.gameRef = gameref;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Keys.BACK) {
			Screen currentScreen = gameRef.getScreen();

			if (currentScreen == null) {
				return false;
			}

			if (currentScreen instanceof MainScreen) {
				Gdx.app.exit();
			} else if (currentScreen instanceof StoreScreen) {
				StoreScreen storeScreen = (StoreScreen) currentScreen;
				gameRef.setScreen(storeScreen.onDoneScreen);
			} else if (currentScreen instanceof HighScoreScreen) {
				HighScoreScreen hsScreen = (HighScoreScreen) currentScreen;
				gameRef.setScreen(hsScreen.onDoneScreen);
			} else if (currentScreen instanceof SettingScreen) {
				gameRef.setScreen(BallHop.mainScreen);
			} else if (currentScreen instanceof UpgradeShield) {
				gameRef.setScreen(BallHop.storeScreen);
			} else if (currentScreen instanceof BuyCoins) {
				gameRef.setScreen(BallHop.storeScreen);
			} else if (currentScreen instanceof HelpScreen) {
				gameRef.setScreen(BallHop.mainScreen);
			} else if (currentScreen instanceof GameScreen) {
				GameScreen gameScreen = (GameScreen) currentScreen;
				switch (gameScreen.currentState) {
				case PAUSED:
					gameRef.setScreen(BallHop.mainScreen);
					break;
				case RUNNING:
					gameScreen.changeState(GameScreenState.PAUSED);
					break;
				case GAMEOVER:
					gameRef.setScreen(BallHop.mainScreen);
					break;
				case WAITING:
					break;
				case SAVE_ME:
					gameScreen.changeState(GameScreenState.GAMEOVER);
					break;
				default:
					break;
				}
			}

		}
		return super.keyUp(keycode);
	}
}
