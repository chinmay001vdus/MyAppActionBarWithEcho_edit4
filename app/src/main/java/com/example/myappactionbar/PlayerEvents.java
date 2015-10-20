package com.example.myappactionbar;

public interface PlayerEvents {

	//public void onStart(String mime, int sampleRate,int channels, long duration);
	//public void onPlay();
	public void onStart(long duration);
	public void onPlayUpdate(int progress, long currentms, long totalms);
	public void onStop(String result);
	//public void onError();
	
}
