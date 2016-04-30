package com.fr.utils;

import com.fr.game.BallHop;

public class Settings {
	public static long numCoins = 0;
	public static long shieldLevel = 1;
	public static long shieldMultiplier = 5;

	private static String hsOne = "hsOne", hsTwo = "hsTwo",
			hsThree = "hsThree", soundStatus = "soundStatus",
			numCoin = "numCoins", shield = "shieldLevel",
			hsTsOne = "TSOne",
			hsTsTwo = "TSTwo",
			hsTsThree = "TSThree";

	public static long highscores[] = new long[] { 0, 0, 0 };
	public static long highscoresTS[] = new long[] { 0, 0, 0 };
	
	public static boolean soundOn = true;

	public static void load(BallHop gameRef) {
		String _1 = gameRef.actionResolver.getSecureValue(hsOne);
		String _2 = gameRef.actionResolver.getSecureValue(hsTwo);
		String _3 = gameRef.actionResolver.getSecureValue(hsThree);
		String _sound = gameRef.actionResolver.getSecureValue(soundStatus);
		String _coin = gameRef.actionResolver.getSecureValue(numCoin);
		String _shield = gameRef.actionResolver.getSecureValue(shield);
		String _ts1 = gameRef.actionResolver.getSecureValue(hsTsOne);
		String _ts2 = gameRef.actionResolver.getSecureValue(hsTsTwo);
		String _ts3 = gameRef.actionResolver.getSecureValue(hsTsThree);

		highscores[0] = _1 == null ? 0 : Long.parseLong(_1);
		highscores[1] = _2 == null ? 0 : Long.parseLong(_2);
		highscores[2] = _3 == null ? 0 : Long.parseLong(_3);
		
		highscoresTS[0] = _ts1 == null ? 0 : Long.parseLong(_ts1);
		highscoresTS[1] = _ts2 == null ? 0 : Long.parseLong(_ts2);
		highscoresTS[2] = _ts3 == null ? 0 : Long.parseLong(_ts3);
		
		soundOn = _sound == null ? true : Boolean.parseBoolean(_sound);
		numCoins = _coin == null ? 0 : Long.parseLong(_coin);
		shieldLevel = _shield == null ? 1 : Long.parseLong(_shield);
	}

	public static void save(BallHop gameRef) {
		gameRef.actionResolver.putSecureValue(hsOne,
				String.valueOf(highscores[0]));
		gameRef.actionResolver.putSecureValue(hsTwo,
				String.valueOf(highscores[1]));
		gameRef.actionResolver.putSecureValue(hsThree,
				String.valueOf(highscores[2]));
		
		gameRef.actionResolver.putSecureValue(soundStatus,
				String.valueOf(soundOn));
		
		gameRef.actionResolver
				.putSecureValue(numCoin, String.valueOf(numCoins));
		
		gameRef.actionResolver.putSecureValue(shield,
				String.valueOf(shieldLevel));
		
		gameRef.actionResolver.putSecureValue(hsTsOne,
				String.valueOf(highscoresTS[0]));
		gameRef.actionResolver.putSecureValue(hsTsTwo,
				String.valueOf(highscoresTS[1]));
		gameRef.actionResolver.putSecureValue(hsTsThree,
				String.valueOf(highscoresTS[2]));
	}

	public static void addScore(long score) {
		for (int i = 0; i < highscores.length; i++) {
			if (highscores[i] < score) {
				for (int j = highscores.length - 1; j > i; j--)
					highscores[j] = highscores[j - 1];
				highscores[i] = score;
				highscoresTS[i] = System.currentTimeMillis();
				break;
			}
		}
	}

}
