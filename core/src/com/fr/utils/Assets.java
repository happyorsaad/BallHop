package com.fr.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class Assets {

	public static volatile boolean isLoading;

	public static BitmapFont font25;
	public static BitmapFont font35;
	public static BitmapFont font45;
	public static BitmapFont font55;
	public static BitmapFont font65;

	public static BitmapFont font25W;
	public static BitmapFont font35W;
	public static BitmapFont font45W;
	public static BitmapFont font55W;
	public static BitmapFont font65W;

	public static TextureAtlas ballHopAtlas;

	public static Sprite iconSprite;
	public static Sprite playButton;
	public static Sprite storeButton;
	public static Sprite leaderboardButton;
	public static Sprite shareButton;
	public static Sprite helpButton;
	public static Sprite settingsButton;
	public static Sprite whiteBack;
	public static Sprite reviewButton;
	public static Sprite aura;

	public static Sprite redPlatform;
	public static Sprite spikePlatform;

	public static Sprite bluePlatform;
	public static Sprite blackPlatform;
	public static Sprite greenPlatform;

	public static Sprite currencySprite;
	public static Sprite shieldSprite;

	public static Sprite soundOnIcon;
	public static Sprite soundOffIcon;
	public static Sprite pauseIcon;

	public static Sprite buttonBase;

	public static Sprite mainBall;
	public static Sprite enemyBall;

	public static Sprite closeButton;

	public static Sprite cameraIcon;
	public static Sprite resumeButton;
	public static Sprite cage;
	public static Sprite unlockButton;

	public static Sound clickSound;
	public static Sound hitSound;
	public static Sound scoreUp;
	public static Sound powerUp;
	public static Sound suckedIn;
	public static Sound lockedUp;
	public static Sound jump;

	public static Music gameMusic;

	public static void load() {
		isLoading = true;
		ballHopAtlas = new TextureAtlas("graphics/spritesheets/ballHop.atlas");

		iconSprite = ballHopAtlas.createSprite("iconFinal");
		playButton = ballHopAtlas.createSprite("playButton");
		storeButton = ballHopAtlas.createSprite("storeButton");
		leaderboardButton = ballHopAtlas.createSprite("leaderboardButton");
		shareButton = ballHopAtlas.createSprite("shareButton");
		helpButton = ballHopAtlas.createSprite("helpButton");
		settingsButton = ballHopAtlas.createSprite("settingsButton");
		reviewButton = ballHopAtlas.createSprite("rateButton");
		cage = ballHopAtlas.createSprite("cage");
		unlockButton = ballHopAtlas.createSprite("unlockButton");

		redPlatform = ballHopAtlas.createSprite("redPlatform");
		greenPlatform = ballHopAtlas.createSprite("greenPlatform");
		blackPlatform = ballHopAtlas.createSprite("blackPlatform");
		bluePlatform = ballHopAtlas.createSprite("bluePlatform");
		spikePlatform = ballHopAtlas.createSprite("redPlatformSpikes");
		aura = ballHopAtlas.createSprite("shieldAura");

		currencySprite = ballHopAtlas.createSprite("currency");
		shieldSprite = ballHopAtlas.createSprite("safetyGem");

		buttonBase = ballHopAtlas.createSprite("blueButtonBase");
		whiteBack = ballHopAtlas.createSprite("whiteBack");

		soundOnIcon = ballHopAtlas.createSprite("soundOn");
		soundOffIcon = ballHopAtlas.createSprite("soundOff");
		pauseIcon = ballHopAtlas.createSprite("pauseIcon");

		mainBall = ballHopAtlas.createSprite("mainBall");
		enemyBall = ballHopAtlas.createSprite("enemyBall");

		closeButton = ballHopAtlas.createSprite("crossButton");
		cameraIcon = ballHopAtlas.createSprite("videoAdIcon");

		resumeButton = ballHopAtlas.createSprite("resumeButton");

		loadFonts();

		try {
			gameMusic = Gdx.audio.newMusic(Gdx.files
					.internal("sounds/gameMusic.ogg"));
		} catch (Exception e) {

		}

		try {
			clickSound = Gdx.audio.newSound(Gdx.files
					.internal("sounds/buttonSoft.wav"));
		} catch (Exception e) {

		}

		try {
			hitSound = Gdx.audio.newSound(Gdx.files
					.internal("sounds/ballonBurst.ogg"));
		} catch (Exception e) {

		}
		try {
			scoreUp = Gdx.audio.newSound(Gdx.files
					.internal("sounds/pickUpSoft.wav"));
		} catch (Exception e) {

		}
		try {
			powerUp = Gdx.audio.newSound(Gdx.files
					.internal("sounds/powerUp.wav"));
		} catch (Exception e) {

		}
		try {
			suckedIn = Gdx.audio.newSound(Gdx.files
					.internal("sounds/suckedIn.ogg"));
		} catch (Exception e) {

		}
		try {
			lockedUp = Gdx.audio.newSound(Gdx.files
					.internal("sounds/lockedUp.ogg"));
		} catch (Exception e) {

		}
		try {
			jump = Gdx.audio.newSound(Gdx.files.internal("sounds/jump.ogg"));
		} catch (Exception e) {

		}
		isLoading = false;
	}

	public static void loadFonts() {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(
				Gdx.files.internal("fonts/arial.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 45;
		parameter.color = Color.BLACK;
		parameter.magFilter = TextureFilter.Linear;
		parameter.minFilter = TextureFilter.Linear;
		font45 = generator.generateFont(parameter);

		parameter.color = Color.WHITE;
		font45W = generator.generateFont(parameter);

		parameter.size = 55;
		parameter.color = Color.BLACK;
		font55 = generator.generateFont(parameter);

		parameter.color = Color.WHITE;
		font55W = generator.generateFont(parameter);

		parameter.size = 65;
		parameter.color = Color.BLACK;
		font65 = generator.generateFont(parameter);

		parameter.color = Color.WHITE;
		font65W = generator.generateFont(parameter);

		parameter.size = 35;
		parameter.color = Color.BLACK;
		font35 = generator.generateFont(parameter);

		parameter.color = Color.WHITE;
		font35W = generator.generateFont(parameter);

		parameter.size = 25;
		parameter.color = Color.BLACK;
		font25 = generator.generateFont(parameter);

		parameter.color = Color.WHITE;
		font25W = generator.generateFont(parameter);

		generator.dispose();
	}

	public static void playSound(Sound sound) {
		if (sound != null && Settings.soundOn) {
			sound.play(0.75f);
		}
	}

	public static void playMusic() {
		if (gameMusic != null && !gameMusic.isPlaying()) {
			gameMusic.setLooping(true);
			gameMusic.setVolume(1f);
			gameMusic.play();
		}
	}

	public static void pauseMusic() {
		if (gameMusic != null && gameMusic.isPlaying()) {
			gameMusic.pause();
		}
	}

}
