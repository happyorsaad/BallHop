package com.fr.game.ballhop;

import android.app.ActionBar.LayoutParams;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.fr.game.ActionResolver;
import com.fr.game.BallHop;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesStatusCodes;
import com.google.android.gms.games.leaderboard.LeaderboardVariant;
import com.google.android.gms.games.leaderboard.Leaderboards;
import com.google.example.games.basegameutils.GameHelper;
import com.google.example.games.basegameutils.GameHelper.GameHelperListener;
import com.vungle.publisher.VunglePub;

public class AndroidLauncher extends AndroidApplication implements
		GameHelperListener, ActionResolver {

	private GameHelper gameHelper;
	private SharedPreferences sharedPref;
	private boolean firstTimeUser;
	private long maxScore;
	private boolean hasResult;
	private boolean hasRated;

	public static GoogleAnalytics analytics;
	public static Tracker tracker;

	private static final String AD_UNIT_ID_INTERSTITIAL = "ca-app-pub-4709840871136981/5738117356";
	private static final String VUNGLE_APP_ID = "5597ec7f50ff69c0740000be";
	private static final String LEADERBOARD_ID = "CgkIvMmnosgXEAIQAQ";

	protected AdView adView;
	protected View gameView;

	final VunglePub vunglePub = VunglePub.getInstance();
	private InterstitialAd interstitialAd;

	private SecurePreferences securePrefs;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
		cfg.useAccelerometer = false;
		cfg.useCompass = false;

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

		RelativeLayout layout = new RelativeLayout(this);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);
		layout.setLayoutParams(params);

		View gameView = createGameView(cfg);
		layout.addView(gameView);

		interstitialAd = new InterstitialAd(this);
		interstitialAd.setAdUnitId(AD_UNIT_ID_INTERSTITIAL);
		interstitialAd.setAdListener(new AdListener() {
			@Override
			public void onAdLoaded() {
			}

			@Override
			public void onAdClosed() {
			}
		});

		setContentView(layout);
		if (gameHelper == null) {
			gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
			gameHelper.enableDebugLog(false);
		}
		sharedPref = this.getPreferences(Context.MODE_PRIVATE);

		boolean isSignedIn = sharedPref.getBoolean("SIGNED_IN", true);

		firstTimeUser = sharedPref.getBoolean("FIRST_TIME", true);
		if (firstTimeUser) {
			writeBooleanToPref("FIRST_TIME", false);
		}

		hasRated = sharedPref.getBoolean("RATED", false);
		gameHelper.setConnectOnStart(isSignedIn);
		gameHelper.setup(this);
		securePrefs = new SecurePreferences(getApplicationContext(),
				".settings", "BsNmkTpX8FqihTXp8DsB"
						+ getApplicationContext().getPackageName()
						+ "5O8WGsNNcZziynP4NVBt" + "XXePtrenfzbKvUBnjpRz"
						+ "slideO", true);

		vunglePub.init(this, VUNGLE_APP_ID);

		analytics = GoogleAnalytics.getInstance(this);
		analytics.setLocalDispatchPeriod(1800);

		tracker = analytics.newTracker("UA-64799535-1");
		tracker.enableAdvertisingIdCollection(true);
		tracker.enableAutoActivityTracking(true);

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (adView != null)
			adView.resume();
		vunglePub.onResume();
	}

	private void writeBooleanToPref(String key, boolean value) {
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	@Override
	public void onStart() {
		super.onStart();
		gameHelper.onStart(this);
	}

	@Override
	public void onStop() {
		super.onStop();
		gameHelper.onStop();
	}

	@Override
	public void onPause() {
		if (adView != null)
			adView.pause();
		vunglePub.onPause();
		super.onPause();
	}

	@Override
	public void onDestroy() {
		if (adView != null)
			adView.destroy();
		vunglePub.clearEventListeners();
		super.onDestroy();
	}

	@Override
	public void onActivityResult(int request, int response, Intent data) {
		super.onActivityResult(request, response, data);
		gameHelper.onActivityResult(request, response, data);
	}

	@Override
	public boolean getSignedInGPGS() {
		return gameHelper.isSignedIn();
	}

	@Override
	public void loginGPGS() {
		try {
			runOnUiThread(new Runnable() {
				public void run() {
					gameHelper.beginUserInitiatedSignIn();
				}
			});
		} catch (final Exception ex) {
		}
	}

	@Override
	public void submitScoreGPGS(long score) {
		putSecureValue("UPLOADTIME", System.currentTimeMillis() + "");
		Games.Leaderboards.submitScore(gameHelper.getApiClient(),
				LEADERBOARD_ID, score);
	}

	@Override
	public void unlockAchievementGPGS(String achievementId) {
		Games.Achievements.unlock(gameHelper.getApiClient(), achievementId);
	}

	@Override
	public void getLeaderboardGPGS() {
		if (gameHelper.isSignedIn()) {
			startActivityForResult(
					Games.Leaderboards.getLeaderboardIntent(
							gameHelper.getApiClient(), LEADERBOARD_ID), 100);
		} else if (!gameHelper.isConnecting()) {
			loginGPGS();
		}
	}

	@Override
	public void getAchievementsGPGS() {
		if (gameHelper.isSignedIn()) {
			startActivityForResult(
					Games.Achievements.getAchievementsIntent(gameHelper
							.getApiClient()), 101);
		} else if (!gameHelper.isConnecting()) {
			loginGPGS();
		}
	}

	@Override
	public void signOutGPGS() {
		if (gameHelper.isSignedIn()) {
			gameHelper.signOut();
			if (!gameHelper.isSignedIn()) {
				writeBooleanToPref("SIGNED_IN", false);
			}
		}
	}

	@Override
	public void onSignInFailed() {

	}

	@Override
	public void onSignInSucceeded() {
		writeBooleanToPref("SIGNED_IN", true);
	}

	@Override
	public boolean isFirstTimeUser() {
		return firstTimeUser;
	}

	@Override
	public boolean isConnecting() {
		return gameHelper.isConnecting();
	}

	public long getMaxValue() {
		maxScore = -1;
		hasResult = false;
		loadScoreOfLeaderBoard();
		int numTries = 3000;
		while (!hasResult && numTries-- >= 0) {
		}
		return maxScore;
	}

	public void loadScoreOfLeaderBoard() {
		Games.Leaderboards.loadCurrentPlayerLeaderboardScore(
				gameHelper.getApiClient(), LEADERBOARD_ID,
				LeaderboardVariant.TIME_SPAN_ALL_TIME,
				LeaderboardVariant.COLLECTION_PUBLIC).setResultCallback(
				new ResultCallback<Leaderboards.LoadPlayerScoreResult>() {
					public void onResult(
							final Leaderboards.LoadPlayerScoreResult scoreResult) {
						hasResult = true;
						if (isScoreResultValid(scoreResult)) {
							maxScore = scoreResult.getScore().getRawScore();
						}
					}
				});
	}

	private boolean isScoreResultValid(
			final Leaderboards.LoadPlayerScoreResult scoreResult) {
		return scoreResult != null
				&& GamesStatusCodes.STATUS_OK == scoreResult.getStatus()
						.getStatusCode() && scoreResult.getScore() != null;
	}

	private View createGameView(AndroidApplicationConfiguration cfg) {
		gameView = initializeForView(new BallHop(this), cfg);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		gameView.setLayoutParams(params);
		return gameView;
	}

	@Override
	public void showOrLoadInterstital() {
		try {
			runOnUiThread(new Runnable() {
				public void run() {
					if (interstitialAd.isLoaded()) {
						interstitialAd.show();
					} else {
						AdRequest interstitialRequest = new AdRequest.Builder()
								.addTestDevice(
										"9224E6EF1C8F6F8A00D635FD23793344")
								.build();
						interstitialAd.loadAd(interstitialRequest);
					}
				}
			});
		} catch (Exception e) {
		}
	}

	@Override
	public boolean hasRated() {
		return hasRated;
	}

	@Override
	public void setRated() {
		hasRated = true;
		writeBooleanToPref("RATED", true);
	}

	@Override
	public boolean isNetConnected() {
		ConnectivityManager cm = (ConnectivityManager) getApplicationContext()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni == null) {
			return false;
		}
		return ni.isConnected();
	}

	@Override
	public void rateApp() {
		Uri uri = Uri.parse("market://details?id="
				+ getApplicationContext().getPackageName());
		Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
		goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY
				| Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET
				| Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
		try {
			startActivity(goToMarket);
		} catch (ActivityNotFoundException e) {
			startActivity(new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://play.google.com/store/apps/details?id="
							+ getApplicationContext().getPackageName())));
		}
	}

	@Override
	public void putSecureValue(String key, String value) {
		securePrefs.put(key, value);
	}

	@Override
	public String getSecureValue(String key) {
		return securePrefs.getString(key);
	}

	@Override
	public void shareAppLink(long score) {
		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey,I scored " + score
				+ " in Ball Hop. See if you can beat me :P "
				+ "\nhttp://play.google.com/store/apps/details?id="
				+ getApplicationContext().getPackageName());
		sendIntent.setType("text/plain");
		startActivity(Intent.createChooser(sendIntent, "Send Via"));
	}

	@Override
	public boolean isTablet() {
		Context context = getApplicationContext();
		return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}

	@Override
	public void saveData(String[] data) {

	}

	@Override
	public String[] loadData() {
		return null;
	}

	@Override
	public void playVideoAd() {
		vunglePub.playAd();
	}

	@Override
	public boolean isVideoAdAvailable() {
		return vunglePub.isAdPlayable();
	}

	@Override
	public void sendTracker(String action, String label, long value) {
		tracker.send(new HitBuilders.EventBuilder().setCategory("Game Event")
				.setAction(action).setLabel(label).setValue(value).build());
	}

}
