package com.fr.game;

public interface ActionResolver {
	public boolean getSignedInGPGS();

	public void loginGPGS();

	public void submitScoreGPGS(long gameScore);

	public void unlockAchievementGPGS(String achievementId);

	public void getLeaderboardGPGS();

	public void getAchievementsGPGS();

	public void signOutGPGS();

	public boolean isFirstTimeUser();

	public boolean hasRated();

	public void setRated();

	public boolean isConnecting();

	public long getMaxValue();

	public void showOrLoadInterstital();

	public boolean isNetConnected();

	public void rateApp();

	public void putSecureValue(String key, String value);

	public String getSecureValue(String key);

	public void shareAppLink(long score);

	boolean isTablet();

	public void saveData(String data[]);

	public String[] loadData();

	public void playVideoAd();

	public boolean isVideoAdAvailable();

	public void sendTracker(String action, String label, long value);
}
