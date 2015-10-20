package com.example.myappactionbar;

public class Globals {
    private static Globals instance;
    
    private int isRepeat = 0;
    private long timerCountdown = 0;
    private int echoLevel,reverbLevel;
    private boolean doStop =false;
    private String bDataStr;
    private boolean sawOutputEOS = false;
    private int noOutputCounter = 0;
    private boolean sendataflag = false;

    

    private Globals() {}
    
    public void setPlayerStateRepeat(int ba){
    	this.isRepeat= ba;
    }
    
    public int getPlayerStateRepeat(){
    	return isRepeat;
    }

    public long getTimerCountdown(){return timerCountdown;}

    public void setTimerCountdown(long a){this.timerCountdown = a;}
    
    public void setdatasendflag(boolean b){
    	this.sendataflag = b;
    }

    public void setFlagStopSong(boolean a){
        this.doStop=a;
    }
    
    public void setData2(boolean a){
    	 this.sawOutputEOS = a;
    }
    
   public void setData3( int a){
	     this.noOutputCounter = a;
    }
   
   public void setEchoLevel(int a){
       this.echoLevel=a;
   }

   public void setReverbLevel(int a){
       this.reverbLevel=a;
   }
    public boolean getFlagStopSong(){
    	 return this.doStop;
         
    }
    
    public boolean getData2(){
    	return this.sawOutputEOS;
    }
    
    public int getData3(){

        return this.noOutputCounter;
    }
    
    public int getEchoLevel(){

        return this.echoLevel;
    }
    
    public int getReverbLevel(){

        return this.reverbLevel;
    }
    public String getBDataString(){
    	return this.bDataStr;
    }
    
    
    public boolean getdatasendflag(){
    return this.sendataflag;
    }
    
    
    public void setBDataString(String s){
    	this.bDataStr = s;
    }
    
    
    
    

    public static synchronized Globals getInstance(){
        if(instance==null){
            instance=new Globals();
        }
        return instance;
    }
}
