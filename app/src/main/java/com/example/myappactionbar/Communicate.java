package com.example.myappactionbar;

import android.widget.SeekBar;

interface communicate {
	 
	  public void playM();
	  public void pauseM();
	  public void stopM();
	  public void previousM();
	  public void nextM();
	  public void connectBlueData();
	  public void connectBlueAudio();
	  void sliderM(int x, int y);
	  
	void sendSliderData(int sliderv,String sliderno);
	void circleSliderData(int a);
	public void overAllVolume(float mdist);
	public void seekPosition( int progress);
	
	
	
	
	}