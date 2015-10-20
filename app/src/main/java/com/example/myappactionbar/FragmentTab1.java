package com.example.myappactionbar;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;


public class FragmentTab1 extends Fragment {
	
	communicate cm;
	private MediaPlayer mMediaPlayer;
	private String[] mMusicList;
	private Button openFile;
	private Button connect1,connect2;
	private Set<BluetoothDevice> pairedDevices;
	private ListView myListView;
	public static Context globalContext = null;
	private ArrayAdapter<String> BTArrayAdapter;
	private static final int REQUEST_ENABLE_BT = 1;
	int tint;
	final String MEDIA_PATH = new String("/sdcard/Music/");
    String externalStoragePath;
	private BluetoothAdapter BA;
	ArrayList<String> msqlist;
	File listFile[];
	int a;
	ArrayList<String> absolutepath;
	private String[] STAR = { "*" };
	TextView songName,songDuration = null;
	 SeekBar sk1 = null;
	Globals g = Globals.getInstance();
	   PlayerStates state = PlayerStates.getInstance();
	   Button playPauseButton,stopButton,nextButton,previousButton,repeatButton,reverbButton = null;
	   Drawable repeatIon = null;

  public View onCreateView(LayoutInflater inflater, ViewGroup container, 
                           Bundle savedInstanceState){
	  
	 
	  View windows = inflater.inflate(R.layout.tab_frag1, container, false);
	  
      externalStoragePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"//Music//";
      
      //ListView mqList = (ListView) windows.findViewById(R.id.listView1);
      

	    return windows;
  }
  
  
  
  @Override
  public void onAttach(Activity activity) {
	  super.onAttach(activity);
	  globalContext = getActivity();
	  repeatIon = getResources().getDrawable(R.drawable.ic_action_repeat);

  }

  @Override
  public void onViewCreated(final View view, Bundle savedInstanceState) {
          super.onViewCreated(view, savedInstanceState);
          
        
          //reverbButton = (Button)view.findViewById(R.id.button8);
          //reverbButton.setText(" On");
          playPauseButton = (Button)view.findViewById(R.id.button1);
          playPauseButton.setBackgroundResource(R.drawable.ic_action_play);
          stopButton = (Button)view.findViewById(R.id.button2);
          stopButton.setBackgroundResource(R.drawable.ic_action_stop);
          nextButton = (Button)view.findViewById(R.id.button3);
          nextButton.setBackgroundResource(R.drawable.ic_action_next);
          previousButton = (Button)view.findViewById(R.id.button4);
          previousButton.setBackgroundResource(R.drawable.ic_action_previous);
          repeatButton = (Button)view.findViewById(R.id.button5);
          repeatButton.setBackground(repeatIon);
          
    	  if((g.getPlayerStateRepeat() == 1)){
              repeatIon.setColorFilter(Color.rgb(120,210,20), PorterDuff.Mode.SRC_IN);
              
              }
             else if(g.getPlayerStateRepeat() == 2){
              	  repeatButton.setText("1");
              	  repeatButton.setTextScaleX(1.5f);
              	  repeatButton.setTextColor(Color.rgb(120,210,20));
              repeatIon.setColorFilter(Color.rgb(120,210,20), PorterDuff.Mode.SRC_IN);
                          }
             else{            	  
           repeatIon.clearColorFilter();
       	   repeatButton.setText("");
             	   
             }


        songName =  (TextView)view.findViewById(R.id.textView1);
        songDuration = (TextView)view.findViewById(R.id.textView3);
  		//songName.setText("afterthefall");
          
//          final RelativeLayout seekBarlayout = (RelativeLayout) view.findViewById(R.id.relativeLayout4);
//          
//          
//          short numberFrequencyBands = MainActivity.mEqualizer.getNumberOfBands();
//    		  final short lowerEqualizerBandLevel = MainActivity.mEqualizer.getBandLevelRange()[0];
//    		  final short upperEqualizerBandLevel = MainActivity.mEqualizer.getBandLevelRange()[1];
//          
//              RelativeLayout.LayoutParams layoutParamsLevelLowerText = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.MATCH_PARENT);
//        	  
//              layoutParamsLevelLowerText.leftMargin=5;
//              layoutParamsLevelLowerText.topMargin=165;
//              
//              RelativeLayout.LayoutParams layoutParamsLevelUpperText = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.MATCH_PARENT);
//        	  
//              layoutParamsLevelUpperText.leftMargin=10;
//              layoutParamsLevelUpperText.topMargin=45;
//    		  
//    		  TextView lowerEqualizerBamnLevelTextView = new TextView(globalContext);
//    		  TextView upperEqualizerBamnLevelTextView = new TextView(globalContext);
//    		  
//    		  lowerEqualizerBamnLevelTextView.setTextSize(10);
//    		  upperEqualizerBamnLevelTextView.setTextSize(10);
//    		  lowerEqualizerBamnLevelTextView.setLayoutParams(layoutParamsLevelLowerText);
//              upperEqualizerBamnLevelTextView.setLayoutParams(layoutParamsLevelUpperText);
//              lowerEqualizerBamnLevelTextView.setText((lowerEqualizerBandLevel/100)+"dB");
//              upperEqualizerBamnLevelTextView.setText((upperEqualizerBandLevel/100)+"dB");
//              seekBarlayout.addView(lowerEqualizerBamnLevelTextView);
//              seekBarlayout.addView(upperEqualizerBamnLevelTextView);
//    		  
//          for (short i = 0; i < numberFrequencyBands; i++) {
//        	  
//        	  final short equalizerBandIndex = i;
//        	  
//              RelativeLayout.LayoutParams layoutParamsFreqText = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
//        	  
//              layoutParamsFreqText.leftMargin=50+90*i;
//              layoutParamsFreqText.width=90;
//              
//        	  
//        	  TextView frequencyHeaderTextView = new TextView(globalContext);
//        	  //frequencyHeaderTextView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
//        	  frequencyHeaderTextView.setText((MainActivity.mEqualizer.getCenterFreq(equalizerBandIndex)/1000)+"Hz");
//        	  frequencyHeaderTextView.setTextSize(11);
//        	  frequencyHeaderTextView.setGravity(Gravity.CENTER_VERTICAL);
//        	  frequencyHeaderTextView.setLayoutParams(layoutParamsFreqText);
//        	 
//              
//        	  RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
//        	 
//        	  
//        	  layoutParams.leftMargin=50+90*i;
//        	  layoutParams.width=LayoutParams.WRAP_CONTENT;
//        	  layoutParams.height = 200;
//        	  layoutParams.topMargin=20;
//        	  
//        	  
//        	  
//        	  VerticalSeekBar seekBar = new VerticalSeekBar(globalContext);
//        	  
//        	  seekBar.setId(i);
//        	  
//        	  seekBar.setMax(upperEqualizerBandLevel-lowerEqualizerBandLevel);
//        	  seekBar.setProgress(MainActivity.mEqualizer.getBandLevel(equalizerBandIndex));
//        	  seekBar.setLayoutParams(layoutParams);
//
//        	  
//        	  seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//    			
//    			@Override
//    			public void onStopTrackingTouch(SeekBar seekBar) {
//    				// TODO Auto-generated method stub
//    				
//    			}
//    			
//    			@Override
//    			public void onStartTrackingTouch(SeekBar seekBar) {
//    				// TODO Auto-generated method stub
//    				
//    			}
//    			
//    			@Override
//    			public void onProgressChanged(SeekBar seekBar, int progress,
//    					boolean fromUser) {
//    				// TODO Auto-generated method stub
//    				MainActivity.mEqualizer.setBandLevel(equalizerBandIndex, (short)(progress+lowerEqualizerBandLevel) );
//    				
//    			}
//    		});
//        	  
//        	  seekBarlayout.addView(seekBar);
//        	  seekBarlayout.addView(frequencyHeaderTextView);
//        	       	  
//        	  
//          }
//        
//          
//          
//          
//        ArrayList<String> equalizerPresetNames = new ArrayList<String>();
//  		ArrayAdapter<String> equalizerPresetSpinnerAdapter = new ArrayAdapter<String>(globalContext,android.R.layout.simple_spinner_item,equalizerPresetNames);
//  		equalizerPresetSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//  		Spinner equalizerPresetSpinner = (Spinner)view.findViewById(R.id.spinner1);
//  		//equalizerPresetSpinner.setBackgroundColor(Color.rgb(255, 100, 30));
//  		for(short i = 0; i<MainActivity.mEqualizer.getNumberOfPresets(); i++){
//  		equalizerPresetNames.add(MainActivity.mEqualizer.getPresetName(i));
//  		}
//  		
//  		equalizerPresetSpinner.setAdapter(equalizerPresetSpinnerAdapter);
//        
//  		equalizerPresetSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//
//			@Override
//			public void onItemSelected(AdapterView<?> parent, View v,
//					int position, long id) {
//				// TODO Auto-generated method stub
//			MainActivity.mEqualizer.usePreset((short)position);
//			short numberFrequencyBands = MainActivity.mEqualizer.getNumberOfBands();
//			short lowerEqualizerBandLevel = MainActivity.mEqualizer.getBandLevelRange()[0];
//			
//			for(short i = 0; i<numberFrequencyBands; i++){
//			
//				short equalizerBandIndex = i;
//				VerticalSeekBar seekBar = (VerticalSeekBar) view.findViewById(equalizerBandIndex);
//
//				seekBar.setProgress(MainActivity.mEqualizer.getBandLevel(equalizerBandIndex)-lowerEqualizerBandLevel);
//				
//			}
//			
//			}
//
//			
//			
//			@Override
//			public void onNothingSelected(AdapterView<?> parent) {
//				// TODO Auto-generated method stub
//				
//			}
//  			
//		});
//  		
  		
//          Spinner reverbSpinner = (Spinner) view.findViewById(R.id.spinner);
//          
//          ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(globalContext,
//        	        R.array.planets_array, android.R.layout.simple_spinner_item);
//          adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//          reverbSpinner.setAdapter(adapter);
//          
//          reverbSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//
//			@Override
//			public void onItemSelected(AdapterView<?> parent, View view,
//					int position, long id) {
//				// TODO Auto-generated method stub
//				 MainActivity.mReverb.setPreset((short)position);
//				
//			}
//
//			@Override
//			public void onNothingSelected(AdapterView<?> parent) {
//				// TODO Auto-generated method stub
//				
//			}
//        	  
//          });
          
           sk1 = (SeekBar)view.findViewById(R.id.seekBar1);
//          if(MainActivity.mPlayer.isPlaying()){
//          long duration = MainActivity.mPlayer.getDuration();
//          long currentPosition = MainActivity.mPlayer.getCurrentPosition();
//          long percent = currentPosition/duration;
//          sk1.setProgress((int)(percent*(sk1.getMax())));
//          }
      
          
          sk1.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				if(fromUser){
				cm.seekPosition(progress);
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
				
			}
        	  
          });
          
          final SeekBar sk2 = (SeekBar)view.findViewById(R.id.seekBar2);
          
          sk2.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
        	  
        	  @Override
      		public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
      			// TODO Auto-generated method stub
      			g.setEchoLevel((int)(progress*0.99f));
      			//t1.setTextSize(p);
      		}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
      	 }); 

          
 SeekBar sk3 = (SeekBar)view.findViewById(R.id.seekBar3);
          
          sk3.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
        	  
        	  @Override
      		public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
      			// TODO Auto-generated method stub
      			g.setReverbLevel((int)(progress*0.2f));
      			//t1.setTextSize(p);
      		}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
      	 }); 

          
         playPauseButton.setOnClickListener(new OnClickListener() {                       
                  @Override
                  public void onClick(View v) {
                	  //g.setData(true);
                	  g.setFlagStopSong(false);
                	  if(state.isPlaying()){
                		  cm.pauseM();
                		  //state.set(PlayerStates.READY_TO_PLAY);
                		  playPauseButton.setBackgroundResource(R.drawable.ic_action_play);
                		  Toast.makeText(globalContext,"Pause",Toast.LENGTH_SHORT).show();  
                	  }else{
                	  cm.playM();
                	  //state.set(PlayerStates.PLAYING);
                	  playPauseButton.setBackgroundResource(R.drawable.ic_action_pause);
                	  Toast.makeText(globalContext,"PLay", Toast.LENGTH_SHORT).show();
                	  }
                  }
          });
          
          repeatButton.setOnClickListener(new OnClickListener() {                       
              @Override
              public void onClick(View v) {
            	  
            if((g.getPlayerStateRepeat() == 0)){
            repeatIon.setColorFilter(Color.rgb(120,210,20), PorterDuff.Mode.SRC_IN);
            g.setPlayerStateRepeat(1);
            Toast.makeText(globalContext,"repeat", Toast.LENGTH_SHORT).show();
            }
           else if(g.getPlayerStateRepeat() == 1){
            	  g.setPlayerStateRepeat(2);
            	  repeatButton.setText("1");
            	  repeatButton.setTextScaleX(1.5f);
            	  repeatButton.setTextColor(Color.rgb(120,210,20));
            repeatIon.setColorFilter(Color.rgb(120,210,20), PorterDuff.Mode.SRC_IN);
            Toast.makeText(globalContext,"repeat Single", Toast.LENGTH_SHORT).show();
              }
           else{            	  
           g.setPlayerStateRepeat(0);
           repeatIon.clearColorFilter();
     	   repeatButton.setText("");
                	   
           }
            
            
              }
      });
          
          stopButton.setOnClickListener(new OnClickListener() {                       
              @Override
              public void onClick(View v) {
            	  g.setFlagStopSong(true);
            	  cm.stopM();
            	  playPauseButton.setBackgroundResource(R.drawable.ic_action_play);
            	  Toast.makeText(globalContext, 
            	          "Stop", Toast.LENGTH_SHORT).show();
              }
      });
          
          nextButton.setOnClickListener(new OnClickListener() {                       
              @Override
              public void onClick(View v) {
            	  g.setFlagStopSong(false);
            	  playPauseButton.setBackgroundResource(R.drawable.ic_action_pause);
            	  cm.nextM();
            	  
            	  Toast.makeText(globalContext, 
            	          "Next", Toast.LENGTH_SHORT).show();
              }
      });
          
          previousButton.setOnClickListener(new OnClickListener() {                       
              @Override
              public void onClick(View v) {
            	  g.setFlagStopSong(false);
            	  playPauseButton.setBackgroundResource(R.drawable.ic_action_pause);
            	  cm.previousM();
            	  Toast.makeText(globalContext, 
            	          "Previous", Toast.LENGTH_SHORT).show();
              }
      });
          
          view.findViewById(R.id.button0).setOnClickListener(new OnClickListener() {                       
              @Override
              public void onClick(View v) {
            	  Intent i = new Intent(globalContext, PlayListActivity.class);
            	  i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                  startActivityForResult(i, 100);
            	  
            	 // Toast.makeText(globalContext,"Open File", Toast.LENGTH_SHORT).show();
            	    }
      });
          
         
          
          view.findViewById(R.id.button6).setOnClickListener(new OnClickListener() {                       
              @Override
              public void onClick(View v) {
            	  cm.connectBlueData();
            	 // Toast.makeText(globalContext, "Connect HC-06", Toast.LENGTH_SHORT).show();
              }
      });
          
          view.findViewById(R.id.button7).setOnClickListener(new OnClickListener() {                       
              @Override
              public void onClick(View v) {
            	  //cm.connectBlueAudio();
            	  //Toast.makeText(globalContext,"Bluetooth Audio", Toast.LENGTH_SHORT).show();
//            	  if(!MainActivity.equalizerFragment.isAdded()){
//            	  getFragmentManager().beginTransaction().add(R.id.container, MainActivity.equalizerFragment,"Equalizer").addToBackStack("mainFragment").commit();
//            	  }
            	  Intent i = new Intent(globalContext, EqualizerActivity.class);
            	  i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                  startActivity(i);
              }
      });
//
//          reverbButton.setOnClickListener(new OnClickListener() {
//              @Override
//              public void onClick(View v) {
//            	  if(!MainActivity.mReverb.getEnabled()){
//            	  MainActivity.mReverb.setEnabled(true);
//            	  reverbButton.setTextColor(Color.rgb(120,210,20));
//            	  reverbButton.setText("Off");
//            	  }else{
//            		  MainActivity.mReverb.setEnabled(false);
//            		//  view.findViewById(R.id.button8)
//            		  reverbButton.setTextColor(Color.rgb(0,0,0));
//            		  reverbButton.setText(" On");
//
//            	  }
//            	  //Toast.makeText(globalContext,"Bluetooth Audio", Toast.LENGTH_SHORT).show();
//              }
//      });
          
         
}

  
  
  
  public void onActivityCreated(Bundle savedInstanceState){
	  super.onActivityCreated(savedInstanceState);
      cm = (communicate) getActivity();
      
      
	    }


  public static boolean isSdPresent() 
  {
      return android.os.Environment.getExternalStorageState().equals(
                  android.os.Environment.MEDIA_MOUNTED);
  }





//
//@Override
//public void onStart(String mime, int sampleRate, int channels, long duration) {
//	// TODO Auto-generated method stub
//	
//	songDuration.setText(String.valueOf(duration));
//	//sk1.setProgress(0);
//	
//}
//
//
//
//@Override
//public void onPlay() {
//	// TODO Auto-generated method stub
//	
//}
//
//
//
//@Override
//public void onPlayUpdate(int percent, long currentms, long totalms) {
//	// TODO Auto-generated method stub
//	sk1.setProgress(percent*100);
//	
//}
//
//
//
//@Override
//public void onError() {
//	// TODO Auto-generated method stub
//	
//}
// 
  
 
}