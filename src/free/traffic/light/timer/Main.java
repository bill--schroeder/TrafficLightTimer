package free.traffic.light.timer;

import com.zealocity.*;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class Main extends baseActivityAmazon {

	private SoundManager _SoundManager = null;
    private Vibrator _Vibrator = null;
	
	private Handler mHandler = new Handler();
	private long mStartTime = 5L;
	
	SeekBar seekbar;
	EditText value;
	ImageView image;
	Button butTimer;

	CountDownTimer timer;
	boolean bStop;
	boolean bWarningSound;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        
        _SoundManager = new SoundManager();
        _SoundManager.initSounds(getBaseContext());
        _SoundManager.addSound(1, R.raw.alarm1);

        _Vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        
        image = (ImageView) findViewById(R.id.imageView);
        value = (EditText) findViewById(R.id.editText1);
        seekbar = (SeekBar) findViewById(R.id.seekBar1);
        butTimer = (Button)findViewById(R.id.buttonTimer);
        
        seekbar.setKeyProgressIncrement(10);
        
        //if(bStop)
        //	value.setText(formatTime(seekbar.getProgress()));
        
        seekbar.setOnSeekBarChangeListener( new OnSeekBarChangeListener()
        {
        	@Override
        	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
        	{
        		// TODO Auto-generated method stub
        		//value.setText(formatTime(progress));
        		value.setText(String.valueOf(progress));
        		
        		if(progress < 1)
        			seekbar.setProgress(1);
            }

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
			}
        });
           
        Button b = (Button)findViewById(R.id.buttonTimer);
        b.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	            	//Toast.makeText(Main.this, "buttonTimer clicked", Toast.LENGTH_SHORT).show();
	            	
	            	long duration = Long.valueOf(value.getText().toString());
	            	if(duration > seekbar.getMax()) {
	            		duration = seekbar.getMax();
	            	} else if(duration < 1) {
	            		duration = 1;
	            	}
	            	value.setText(String.valueOf(duration));
	            	seekbar.setProgress(Integer.valueOf(String.valueOf(duration)));
	            	mStartTime = duration * 1000;
	            	
	            	if(butTimer.getText().toString().toLowerCase().equals("start")) {
	            		bStop = false;
	            		bWarningSound = false;
	            		butTimer.setText("Stop");
		            	mHandler.removeCallbacks(mTimerTask);
		            	mHandler.postDelayed(mTimerTask, 1);
	            	} else {
	            		bStop = true;
	            		timer.cancel();
	            		timer.onFinish();
	            	}
	            }
            });
                             
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy()
    {
		//super.logMessage("onDestroy");
		
		super.onDestroy();
        
        _SoundManager = null;
        _Vibrator = null;
		timer = null;

        mHandler.removeCallbacks(mTimerTask);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    

	private Runnable mTimerTask = new Runnable() {
	   public void run() {
		   /*
		   boolean bStop = false;
		   
	       final long start = mStartTime;
	       long millis = SystemClock.uptimeMillis() - start;
	       int seconds = (int) (millis / 1000);
	       int minutes = seconds / 60;
	       seconds     = seconds % 60;

	       Button b = (Button)findViewById(R.id.buttonTimer);
       	   if(b.getText().toString().toLowerCase() == "stop")
       		   bStop = true;
       	   
	       String Message = "";
	       Message = "Start: " + start;
	       Toast.makeText(Main.this, Message, Toast.LENGTH_SHORT).show();
	       
	       final TextView text = (TextView) findViewById(R.id.textViewCountDown);
	       //text.setText("Danger removed ... " + (start + (((minutes * 60) + seconds + 1) * 1000)) + " ... ");
	       text.setText("" + (start + (((minutes * 60) + seconds + 1) * 1000)));

	       
	       if (!bStop) {
	    	   //mHandler.postAtTime(this, start + (((minutes * 60) + seconds + 1) * 1000));
	    	   mHandler.postAtTime(this, 1000);
	       } else {
	    	   mHandler.removeCallbacks(mTimerTask);
	       }
	      	*/
		   		   
		   timer = new CountDownTimer(mStartTime, 1000) {
			     public void onTick(long millisUntilFinished) {
			         //Toast.makeText(Main.this, "seconds remaining: " + millisUntilFinished / 1000, Toast.LENGTH_SHORT).show();
			    	 //Toast.makeText(Main.this, "time: " + mStartTime + " - " + millisUntilFinished, Toast.LENGTH_SHORT).show();
			    	 //Toast.makeText(Main.this, "time: " + (millisUntilFinished * 100) / mStartTime, Toast.LENGTH_SHORT).show();
			    	 
			    	 image.setImageResource(R.drawable.trafficlightsgreen);

			    	 if(((millisUntilFinished * 100) / mStartTime) < 21) {
			    		 image.setImageResource(R.drawable.trafficlightsyellow);
			    		 
			    		 if(!bWarningSound) {
			    			 bWarningSound = true;
			    			 soundAlarm();
			    		 }
			    	 }

			    	 final TextView text = (TextView) findViewById(R.id.textViewCountDown);
			         //text.setText("0" + (millisUntilFinished / 1000));
			         text.setText(formatTime(millisUntilFinished));
			     }

			     public void onFinish() {
			         //Toast.makeText(Main.this, "Done!", Toast.LENGTH_SHORT).show();

		        	 soundAlarm();
                     
			    	 image.setImageResource(R.drawable.trafficlightsred);
			    	 
			         final TextView text = (TextView) findViewById(R.id.textViewCountDown);
			         //text.setText("00:00");
			         text.setText(formatTime(0));
			         
			         butTimer.setText("Start");
			         
		        	 soundAlarm();
                     
			         //mHandler.removeCallbacks(mTimerTask);
			     }
			  }.start();
			  
			  mHandler.removeCallbacks(mTimerTask);
	   }
	};	
	
	private void soundAlarm() {
		if(!bStop) {
	    	 _SoundManager.playSound(1);
             _Vibrator.vibrate(21);
		}
	}
	
	private String formatTime(long seconds){
		String Formatted;
		
		Formatted = String.format("%04d", seconds / 1000);
		
		float minutes = Float.valueOf(seconds) / 60000;
		if(minutes > 0){
			int Stop = String.valueOf(minutes).indexOf(".");
			long mins = Long.valueOf(String.valueOf(minutes).substring(0, Stop));
			Formatted = String.format("%02d", mins);
			long secs = (seconds - (mins * 60000)) / 1000;
			//Formatted += ":" + String.format("%02d", Long.valueOf(String.valueOf(minutes).substring(Stop + 1, String.valueOf(minutes).length())));
			Formatted += ":" + String.format("%02d", secs);
		} else {
			Formatted = "00:" + String.format("%02d", seconds / 1000);
		}
		
		return Formatted;
	}
	
}
