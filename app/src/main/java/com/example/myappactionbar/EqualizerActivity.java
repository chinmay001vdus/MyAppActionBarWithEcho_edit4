package com.example.myappactionbar;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.VerticalSeekBar1;

public class EqualizerActivity extends ActionBarActivity{


	protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.equalizeractivitylayout);
        
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
 
        Spinner equalizerPresetSpinner = new Spinner(this);
        
        LinearLayout.LayoutParams layoutParamsEqSpinner = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParamsEqSpinner.gravity = Gravity.CENTER_HORIZONTAL;
        layoutParamsEqSpinner.topMargin = 10;
        equalizerPresetSpinner.setLayoutParams(layoutParamsEqSpinner);
        //equalizerPresetSpinner.setPadding(2, 2, 2, 2);
        
        LinearLayout seekBarlayout = (LinearLayout) findViewById(R.id.linearLayout1);
            
        	      TextView equalizerHeading = new TextView(this);
        	      equalizerHeading.setText("Equalizer");
        	      equalizerHeading.setTextSize(30);
        	      equalizerHeading.setGravity(Gravity.CENTER_HORIZONTAL);
        	      
        	      LinearLayout.LayoutParams layoutParamsheaderText = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        	      layoutParamsheaderText.topMargin = 40;
            	  layoutParamsheaderText.bottomMargin = 20;
        	      equalizerHeading.setLayoutParams(layoutParamsheaderText);
        	      seekBarlayout.addView(equalizerHeading);
        	    
                short numberFrequencyBands = MainActivity.mEqualizer.getNumberOfBands();
      		  final short lowerEqualizerBandLevel = MainActivity.mEqualizer.getBandLevelRange()[0];
      		  final short upperEqualizerBandLevel = MainActivity.mEqualizer.getBandLevelRange()[1];
      		  
      		  
      		  
                LinearLayout.LayoutParams layoutParamsLevelLowerText = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.MATCH_PARENT);
          	  
                
                layoutParamsLevelLowerText.topMargin=230;
                
                LinearLayout.LayoutParams layoutParamsLevelUpperText = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.MATCH_PARENT);
          	  
                
                layoutParamsLevelUpperText.topMargin=30;
      		  
      		  TextView lowerEqualizerBamnLevelTextView = new TextView(this);
      		  TextView upperEqualizerBamnLevelTextView = new TextView(this);
      		  
      		  lowerEqualizerBamnLevelTextView.setTextSize(10);
      		  upperEqualizerBamnLevelTextView.setTextSize(10);
      		  lowerEqualizerBamnLevelTextView.setLayoutParams(layoutParamsLevelLowerText);
                upperEqualizerBamnLevelTextView.setLayoutParams(layoutParamsLevelUpperText);
                lowerEqualizerBamnLevelTextView.setText((lowerEqualizerBandLevel/100)+"dB");
                upperEqualizerBamnLevelTextView.setText((upperEqualizerBandLevel/100)+"dB");
                
                
                LinearLayout RowLayout = new LinearLayout(this);
                
                RowLayout.setOrientation(LinearLayout.HORIZONTAL);
          	  RowLayout.setGravity(Gravity.CENTER_HORIZONTAL);
          	  
                
                LinearLayout VerLayout = new LinearLayout(this);
                VerLayout.setOrientation(LinearLayout.VERTICAL);
                VerLayout.addView(upperEqualizerBamnLevelTextView);
                VerLayout.addView(lowerEqualizerBamnLevelTextView);
          	  
                
                RowLayout.addView(VerLayout);     
      		  
            for (short i = 0; i < numberFrequencyBands; i++) {
          	  
          	  final short equalizerBandIndex = i;
          	  
                
          	  
          	  TextView frequencyHeaderTextView = new TextView(this);
          	  frequencyHeaderTextView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
          	  frequencyHeaderTextView.setGravity(Gravity.CENTER_HORIZONTAL);
          	  frequencyHeaderTextView.setText((MainActivity.mEqualizer.getCenterFreq(equalizerBandIndex)/1000)+"Hz");
          	                
          	  LinearLayout seekBarRowLayout = new LinearLayout(this);
          	  seekBarRowLayout.setOrientation(LinearLayout.HORIZONTAL);
          	  seekBarRowLayout.setGravity(Gravity.CENTER_HORIZONTAL);
          	  
          	  LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
          	  //layoutParams.weight = 1;
          	  
          	  layoutParams.leftMargin=30;
          	  layoutParams.rightMargin=30;
          	  layoutParams.height = 280;

          	  
          	   	  
          	  VerticalSeekBar1 seekBar = new VerticalSeekBar1(this);
          	  
          	  seekBar.setId(i);
          	  seekBar.setLayoutParams(layoutParams);        	  
          	  seekBar.setMax(upperEqualizerBandLevel-lowerEqualizerBandLevel);
          	  seekBar.setProgress(MainActivity.mEqualizer.getBandLevel(equalizerBandIndex));


          	  
          	  seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      			
      			@Override
      			public void onStopTrackingTouch(SeekBar seekBar) {
      				// TODO Auto-generated method stub
      				
      			}
      			
      			@Override
      			public void onStartTrackingTouch(SeekBar seekBar) {
      				// TODO Auto-generated method stub
      				
      			}
      			
      			@Override
      			public void onProgressChanged(SeekBar seekBar, int progress,
      					boolean fromUser) {
      				// TODO Auto-generated method stub
      				MainActivity.mEqualizer.setBandLevel(equalizerBandIndex, (short)(progress+lowerEqualizerBandLevel) );

      			}
      		});
          	  
          	  
          	  //seekBarRowLayout.addView(lowerEqualizerBamnLevelTextView);
          	  LinearLayout singleVslide = new LinearLayout(this);
          	  singleVslide.setOrientation(LinearLayout.VERTICAL);
          	  LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
          	  layoutParams1.topMargin = 30;
          	  singleVslide.setLayoutParams(layoutParams1);
          	  singleVslide.addView(frequencyHeaderTextView);
          	  singleVslide.addView(seekBar);
          	  
          	  seekBarRowLayout.addView(singleVslide);
                //seekBarRowLayout.addView(upperEqualizerBamnLevelTextView);
          	  //seekBarlayout.addView(frequencyHeaderTextView);
          	   
          	  RowLayout.addView(seekBarRowLayout);     	  

            }
            
            seekBarlayout.addView(equalizerPresetSpinner);
            seekBarlayout.addView(RowLayout);
            
          ArrayList<String> equalizerPresetNames = new ArrayList<String>();
    		ArrayAdapter<String> equalizerPresetSpinnerAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,equalizerPresetNames);
    		equalizerPresetSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    		//Spinner equalizerPresetSpinner = (Spinner)equalizer.findViewById(R.id.spinner1);
    		//equalizerPresetSpinner.setBackgroundColor(Color.rgb(255, 100, 30));
    		for(short i = 0; i<MainActivity.mEqualizer.getNumberOfPresets(); i++){
    		equalizerPresetNames.add(MainActivity.mEqualizer.getPresetName(i));
    		}
    		
    		equalizerPresetSpinner.setAdapter(equalizerPresetSpinnerAdapter);
          
    		equalizerPresetSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

  			@Override
  			public void onItemSelected(AdapterView<?> parent, View v,
  					int position, long id) {
  				// TODO Auto-generated method stub
  			
  		    MainActivity.mEqualizer.usePreset((short)position);
  			short numberFrequencyBands = MainActivity.mEqualizer.getNumberOfBands();
  			short lowerEqualizerBandLevel = MainActivity.mEqualizer.getBandLevelRange()[0];
  			
  			for(short i = 0; i<numberFrequencyBands; i++){
  				short equalizerBandIndex = i;
  				VerticalSeekBar1 seekBar = (VerticalSeekBar1) findViewById(equalizerBandIndex);
  				String a = "band"+i;
  				
  				seekBar.setProgress(MainActivity.mEqualizer.getBandLevel(equalizerBandIndex)-lowerEqualizerBandLevel);
  				 
  			}
  			
  			}

  			
  			
  			@Override
  			public void onNothingSelected(AdapterView<?> parent) {
  				// TODO Auto-generated method stub
  				
  			}
    			
  		});

	      
	}
	
	@Override
    public void onResume() {
        super.onResume();
    }
	
	@Override
	protected void onPause(){
		super.onPause();
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
	}
	 
//	@Override
//	public void onSaveInstanceState(Bundle savedInstanceState) {
//		super.onSaveInstanceState(savedInstanceState);
//		for(int i=0;i<MainActivity.mEqualizer.getNumberOfBands();i++){
//			  String a = "band"+i;
//			  savedInstanceState.putInt(a, MainActivity.mEqualizer.getBandLevel((short) i));
//		  }
//
//
//		}


}
