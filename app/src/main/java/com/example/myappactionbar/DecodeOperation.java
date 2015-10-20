package com.example.myappactionbar;

import java.io.FileDescriptor;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Random;

import android.content.res.AssetFileDescriptor;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.appcompat.R.integer;
import android.util.Log;

/**
* AsyncTask that takes care of running the decode/playback loop
*
*/
public class DecodeOperation extends AsyncTask<String, Long, String> {
	communicate cm;
	protected MediaExtractor extractor;
	protected MediaCodec codec;
	PlayerEvents events ;
	PlayerStates state = PlayerStates.getInstance();
	long presentationTimeUs = 0, duration = 0;
	int bitrate = 0;
	boolean stop = false;
	protected int inputBufIndex;
	protected String mypath;
	private byte[][] chunks = new byte[100][];
	private byte[][]delayoutSamples = new byte[100][];
	protected byte[] chunk,chunkOut;
	short newSample1,oldnewSample1,oldnewSample2=0;
	int el=0,rl=0;
	Handler handler = new Handler();
	Globals g  = Globals.getInstance();
	int noOutputCounter = 0;
    int noOutputCounterLimit = 100;
    boolean sawInputEOS = false;
    boolean sawOutputEOS = false;
    Random noise = new Random();
     @Override
     protected String doInBackground(String... values) {
        System.out.println(values[0]);
        this.mypath = values[0];
       decodeLoop(mypath);
       String stopFinishFlag = null;
       if(!g.getFlagStopSong() &&  sawOutputEOS ){
    	   stopFinishFlag= "finished";}
       else{
    	   stopFinishFlag = "stopped";
          }
       return stopFinishFlag;
     }
 
     @Override
     protected void onPreExecute() {
    	 events.onStart(duration);
     }
     
     @Override
     protected void onPostExecute(String result){
    	 super.onPostExecute(result);
    	
    	 events.onStop(result);
    	
     }
 
     @Override
     protected void onProgressUpdate(Long... values) {
    	  
    	 Long presentTime = values[0];
    	 Long durationTime = values[1];
    	 final int percent =  (duration == 0)? 0 : (int) (100 * presentationTimeUs / duration);
    	 //long progress = (100*presentTime/durationTime);
    	 events.onPlayUpdate(percent, presentTime, durationTime);
    	
    	 
     }
     
     
    
     
     public void setEventsListener(PlayerEvents events) {
 	 this.events = events;
 		
 	}
     
     public DecodeOperation() {
    	 
     }  
     
     
     public DecodeOperation(PlayerEvents events){
    	 setEventsListener(events);
     }
     
     public synchronized void syncNotify() {
     	notify();
     }
     
	 public boolean isLive() {
	 		return (duration == 0);
	 	}
     
	   public void stop() {
	 		
		   	if(state.isReadyToPlay()){
	 			syncNotify();
	 		}
		   	state.set(PlayerStates.STOPPED);
	 		stop = true;

	 	}
	     
	     public void pause() {
	 		state.set(PlayerStates.READY_TO_PLAY);
	 	}
	 
	 
     public void seek(long pos) {
	 	if(extractor!=null)	
    	 extractor.seekTo(pos, MediaExtractor.SEEK_TO_CLOSEST_SYNC);
	 	}
	 	
	 public void seek(int percent) {
	 		long pos = percent * duration / 100;
	 		seek(pos);
	 	}
     
     public synchronized void waitPlay(){
     	 if (duration == 0) return;
         while(state.get() == PlayerStates.READY_TO_PLAY) {
             try {
                 wait();
            	 //Thread.sleep(100);
             } catch (InterruptedException e) {
                 e.printStackTrace();
             }
         }
     } 
     
     
	private void decodeLoop(String path){
		     android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
		     
		     ByteBuffer[] codecInputBuffers;
		     ByteBuffer[] codecOutputBuffers;
		 
		     // extractor gets information about the stream
		     extractor = new MediaExtractor();
		     
		     //String path = PlayListActivity.songsList.get(MainActivity.currentSongIndex).get("songPath");
		     try {
		          extractor.setDataSource(path);
		     } catch (Exception e) {
		         System.out.println("failed to upload file");
				 return;
		     }
		 
		     MediaFormat format = extractor.getTrackFormat(0);
		     final String mime = format.getString(MediaFormat.KEY_MIME);
		     final int sampleRate = format.getInteger(MediaFormat.KEY_SAMPLE_RATE);

		     final int channels = format.getInteger(MediaFormat.KEY_CHANNEL_COUNT);

		try {
		     duration = format.getLong(MediaFormat.KEY_DURATION);
			 g.setTimerCountdown(duration);}
		catch (Exception e) {
				System.out.println("failed to upload file");

			}

		     //bitrate = format.getInteger(MediaFormat.KEY_BIT_RATE);
		     // the actual decoder
		     
		     
				try {
					codec = MediaCodec.createDecoderByType(mime);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Log.e("tag", "*****decoder nit created by mime");
					e.printStackTrace();
				}
			
		   //if (events != null) handler.post(new Runnable() { @Override public void run() { events.onStart(mime, sampleRate, channels, duration);  } });	
				
		     codec.configure(format, null /* surface */, null /* crypto */, 0 /* flags */);
		     codec.start();
		     codecInputBuffers = codec.getInputBuffers();
		     codecOutputBuffers = codec.getOutputBuffers();
		     
		 
		     // get the sample rate to configure AudioTrack
		int formatChannel;
		if(channels==1){
		     formatChannel = AudioFormat.CHANNEL_OUT_MONO;}
		else{
				 formatChannel = AudioFormat.CHANNEL_OUT_STEREO;
			}
		     // create our AudioTrack instance
		     MainActivity.audioTrack = new AudioTrack(
		          AudioManager.STREAM_MUSIC, 
			  sampleRate,
					 formatChannel,
			  AudioFormat.ENCODING_PCM_16BIT, 
			  AudioTrack.getMinBufferSize (
			       sampleRate,
					  formatChannel,
			       AudioFormat.ENCODING_PCM_16BIT
			  ), 
			  AudioTrack.MODE_STREAM
		     );
		     //MainActivity.audioTrack.setStereoVolume(0.1f, 1.0f);
		     MainActivity.audioTrack.attachAuxEffect(MainActivity.mEqualizer.getId());
		     MainActivity.audioTrack.setAuxEffectSendLevel(1.0f);
		     //MainActivity.audioTrack.attachAuxEffect(MainActivity.mReverb.getId());
		     //MainActivity.audioTrack.setAuxEffectSendLevel(0.5f);
		     // start playing, we will feed you later
		     
		     
		     MainActivity.audioTrack.play();
		     
		     extractor.selectTrack(0);
		 
		     // start decoding
		     final long kTimeOutUs = 10000;
		     MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
		     
		     
		     int samplenum =0;
		     
		   state.set(PlayerStates.PLAYING);

		   while (!sawOutputEOS && noOutputCounter < noOutputCounterLimit &&  !stop) {
	    	 //Log.i("LOG_TAG", "loop ");  
			  
			  waitPlay();
			  
			   //noOutputCounter++;
		    	 if (!sawInputEOS) {  
		    		 inputBufIndex = codec.dequeueInputBuffer(kTimeOutUs);
		    		 //Log.d("LOG_TAG", " bufIndexCheck " );
		     
		    		 if (inputBufIndex >= 0) {
		                     ByteBuffer dstBuf = codecInputBuffers[inputBufIndex];
		
		                      int sampleSize = extractor.readSampleData(dstBuf, 0 /* offset */);
		                      presentationTimeUs = 0;
		                      
		             // can throw illegal state exception (???) 
		         
		         if (sampleSize < 0  ) { Log.d("LOG_TAG", "saw input EOS.");  
		         sawInputEOS = true;
		         sampleSize = 0;
		         }else{
		        	 presentationTimeUs = extractor.getSampleTime();
		        	 
		        	 //if (events != null) handler.post(new Runnable() { @Override public void run() { events.onPlayUpdate(percent, presentationTimeUs / 1000, duration / 1000);   } });   
		        	 publishProgress(presentationTimeUs / 1000, duration / 1000);
		         }
		         
		         
		         codec.queueInputBuffer(inputBufIndex,0 /* offset */,sampleSize,presentationTimeUs,sawInputEOS ? MediaCodec.BUFFER_FLAG_END_OF_STREAM : 0);
		         
		         if (!sawInputEOS) { 
		        	 extractor.advance();
		        	 }
		         }
		         else { 
		        	 Log.e("LOG_TAG", "inputBufIndex " +inputBufIndex); 
		        	 }
		    	 }
		    	 
		    	 
		       int res = codec.dequeueOutputBuffer(info, kTimeOutUs);
		       
		       noOutputCounter++;
		      
		       if (res >= 0) {		         
		
		        // Log.d("LOG_TAG", "got frame, size " + info.size + "/" + info.presentationTimeUs);
		         
		     if (info.size > 0) {
		         noOutputCounter = 0;
		     }
//		    
		     int outputBufIndex = res;
		     
		     el = g.getEchoLevel();
		     rl = g.getReverbLevel();
		     for( int y = 99; y > 0; y-- ){
		     
		    	 if(chunks[y-1] != null && delayoutSamples[y-1] != null){
			    	 //chunkold2 = new byte[info.size];
				     chunks[y] = chunks[y-1];
				     delayoutSamples[y] = delayoutSamples[y-1];
//				     for(int t = 9; t > y; t--){
//				    	 //java.util.Arrays.fill(chunks[t], (byte) 0);
//				    	 chunks[t]=null;
				     //}
				     }
		    	 
		     }
		     chunks[0] = chunkOut;
             delayoutSamples[0] = chunk;
		     ByteBuffer buf = codecOutputBuffers[outputBufIndex];
		     
		     
		    // byte[] filterBuffer = new byte[4*info.size];
		     chunk = new byte[info.size];
		     chunkOut = new byte[chunk.length];
		     //System.out.println("index "+outputBufIndex) ;
//		     for(outputBufIndex=0;outputBufIndex<4;){
//		    	 buf.get(filterBuffer, info.size*outputBufIndex, info.size);
//		    	 
//		     }
		          
		     buf.get(chunk);
		     buf.clear();
		     
		     short newSample =0;
		     
		     
		     if(chunk.length > 0 && chunks[el]!= null && chunk.length == chunks[el].length  ){   
		    	 chunkOut = chunk;
		    	 
		    	 if( el>0){
		    		 chunkOut = echofilter(chunk, delayoutSamples[el], chunk.length);
		    		 
		    		 //chunkOut = echofilter(chunk, delayoutSamples[el], chunk.length);
		    	 
		    	 } else if( rl>0){
		    		 //chunkOut = echofilter(chunk, chunks[rl], chunk.length);
		    		 
		    		 chunkOut = reverbAllpassfilter(chunk, chunks[rl],delayoutSamples[rl], chunk.length);
		    	 
		    	 } else {
		    		 
		    		 chunkOut = echofilter(chunk,new byte[chunk.length], chunk.length);
		    	 }
		    	 //chunkOut = stereoDiferFilter(chunk,chunk.length);
		    	//chunk = newfilter(chunk,chunk,chunk.length);
		    	 
		    	MainActivity.audioTrack.write(chunkOut,0,chunkOut.length);
		     }
		     codec.releaseOutputBuffer(outputBufIndex, false /* render */);
		     
		     if ((info.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
		         Log.d("LOG_TAG", "saw output EOS.");
		         sawOutputEOS = true;		     }
		     } else if (res == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
		     codecOutputBuffers = codec.getOutputBuffers();
		     Log.d("LOG_TAG", "output buffers have changed.");
		 } else if (res == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
		     MediaFormat oformat = codec.getOutputFormat();
		     //audioTrack.setPlaybackRate(oformat.getInteger(MediaFormat.KEY_SAMPLE_RATE));
		     Log.d("LOG_TAG", "output format has changed to " + oformat);
		 } else {
		     Log.d("LOG_TAG", "dequeueOutputBuffer returned " + res);
		 }
	}
		Log.d("LOG_TAG", "stopping...");
		 MainActivity.audioTrack.stop();
		 relaxResources(true);
		 state.set(PlayerStates.STOPPED);
		 stop = true;
		        
		        
		
		//g.setData1(true);
		}



	
	public static short getSample(byte[] buffer, int position) {
	    return (short) (((buffer[position + 1] & 0xff) << 8) | (buffer[position] & 0xff));
	  }
	
	public static void setSample(byte[] buffer, int position, short sample) {
	    buffer[position] = (byte) (sample & 0xff);
	    buffer[position + 1] = (byte) ((sample >> 8) & 0xff);
	  }

	public byte[] echofilter(byte[] samples, byte[] offset, int length) {

	    for (int i = 0; i < length; i+=2) {
	      // update the sample
	    	short offSet=0,offset1=0,offset2 = 0,offset3 = 0,offset4 = 0;
	    	
	      short oldSample = getSample(samples, i);
	     
	      offSet = getSample(offset,i);
	      
	     
	      short newSample =  (short)((0.6)*(oldSample+(0.7f*offSet)));
	      setSample(samples, i, newSample);
	      
	    }
		return samples;
	   }
	
	
	public byte[] reverbAllpassfilter(byte[] samples, byte[] oldSamples,byte[] oldOutputSamples, int length) {

	    
		for (int i = 0; i < length; i+=2) {
	      // update the sample
	    	
	    	
	      short currentSamples = getSample(samples, i);
	  
	      short oldsamples = getSample(oldSamples,i);
	      short oldOutSample = getSample(oldOutputSamples,i);
	      short outSample = 0;
	     
	     
	      outSample = (short)((0.9)*(oldsamples+(0.8f*currentSamples)-(0.8f*oldOutSample)));
	      //short newSample =  (short)((0.7)*(oldSample+(0.5f*offSet)-(0.25*offSetf1)+(0.18*offSetf2)-(0.17*offSetf3)));
	      //short newSample =  (short)((0.6)*(oldSample+(0.7f*offSet)));
	      setSample(samples, i, outSample);
	      
	    }
	    
		return samples;
	    }

	
	public byte[] stereoDiferFilter(byte[] samples, int length){
		
		for(int i = 0; i < length-2; i+=2){
			
			short left = getSample(samples,i);
			short right = getSample(samples,i+1);
			short diff = (short) ((left - right)/2);
			short sample = (short) (diff);
			setSample(samples,i,sample);
		}
		
		
		return samples;
		
	}
	
	public byte[] newfilter(byte[] samples, byte[] offset, int length) {

	    for (int i = 0; i < length; i+=2) {
	      // update the sample
	    	short offSet=0,offset1=0,offset2 = 0,offset3 = 0,offset4 = 0;
	    	
	      short oldSample = getSample(samples, i);
	      offset4  = offset3;
	      offset3  = offset2;
	      offset2  = offset1;
	      offset1 = offSet; 
	      offSet = getSample(offset,i);
	      
	      short offSetf=0,offSetf1 = 0,offSetf2 = 0,offSetf3 = 0,offSetf4 = 0;
	      if(i>0){
	      offSetf = getSample(samples,i-2);
	      }else{
	       offSetf = 0;
	     }
	      
	      if(i>2){
		      offSetf1 = getSample(samples,i-4);
		      }else{
		       offSetf1 = 0;
		     }
	      
	      if(i>4){
		      offSetf2 = getSample(samples,i-6);
		      }else{
		       offSetf2 = 0;
		     }
	      
	      if(i>6){
		      offSetf3 = getSample(samples,i-8);
		      }else{
		       offSetf3 = 0;
		     }
	      if(i>8){
		      offSetf4 = getSample(samples,i-10);
		      }else{
		       offSetf4 = 0;
		     }
	      
	      short newSample = (short) ((0.5f)*(noise.nextFloat()+oldSample+ (0.5f*offSet)-(0.4*offset2)-(0.2*offset3)-(0.1*offset4)-(0.25*offSetf1)+(0.18*offSetf2)-(0.17*offSetf3)+(0.03*offSetf4)));
	      newSample = (short)(0.6f*(newSample+oldSample));
	      //short newSample =  (short)((0.7)*(oldSample+(0.5f*offSet)-(0.25*offSetf1)+(0.18*offSetf2)-(0.17*offSetf3)));
	      //short newSample =  (short)((0.6)*(oldSample+(0.7f*offSet)));
	      setSample(samples, i, newSample);
	      
	    }
		return samples;
	    }
	
	public byte[] highPassfilter(byte[] samples, int length) {

	    for (int i = 0; i < length; i+=2) {
	      // update the sample
	      short oldSample = getSample(samples, i);
	      short offSetf,offSetf1 = 0;
	      if(i>0){
	      offSetf = getSample(samples,i-2);
	      }else{
	       offSetf = 0;
	     }
	      
	      if(i>2){
		      offSetf1 = getSample(samples,i-4);
		      }else{
		       offSetf1 = 0;
		     }
	      
	      oldnewSample2 = oldnewSample1;
	      oldnewSample1 = newSample1;
	      newSample1 = (short)(0.7* (oldSample - (0.5*offSetf) -(0.4* offSetf1 )));
	      newSample1 = (short) (0.6*(newSample1 + oldSample));
	      setSample(samples, i, newSample1);
	      
	    }
		return samples;
	    }
	
void relaxResources(Boolean release)
{
     if(codec != null){
          if(release){
		codec.stop();
		codec.release();
		codec = null;
	  }	    
     }
     if(MainActivity.audioTrack != null){
    	
    	 MainActivity.audioTrack.flush();
    	 MainActivity.audioTrack.release();
    	 MainActivity.audioTrack = null;	
     }
}


}
