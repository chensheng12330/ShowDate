package com.example.showdate;

import android.os.Bundle;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import android.os.Handler;
import android.os.Message;
import android.widget.SeekBar;
import android.widget.TextView;


@SuppressLint({ "SimpleDateFormat", "HandlerLeak" }) public class MainActivity extends Activity {

	private TextView textViewTime;
	private TextView textViewDate;
	private TextView textViewWeek;
    private SeekBar  seekBar;
	
	public class SHDate {
		public String Date;
		public String Time;
		public String Week;
        public int brightness;
	}
	
	private Handler handler;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
      //无title    
        requestWindowFeature(Window.FEATURE_NO_TITLE);    
         //全屏    
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,      
                       WindowManager.LayoutParams. FLAG_FULLSCREEN);   
            

        setContentView(R.layout.activity_main);
        
        //setLight(this,1);
        
        textViewTime=(TextView) findViewById(R.id.textViewTime);
        textViewDate=(TextView) findViewById(R.id.textViewDate);
        textViewWeek=(TextView) findViewById(R.id.textViewWeek);

        seekBar= (SeekBar)findViewById(R.id.seekBar);
        seekBar.setMax(255);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            /**
             * 拖动条停止拖动的时候调用
             */
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //description.setText("拖动停止");
            }
            /**
             * 拖动条开始拖动的时候调用
             */
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //description.setText("开始拖动");
            }
            /**
             * 拖动条进度改变的时候调用
             */
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                //description.setText("当前进度："+progress+"%");
                setLight(MainActivity.this,progress);
            }
        });

        //////////
        handler = new Handler() {
            public void handleMessage(Message msg) {
            	
            	SHDate myDate =  (SHDate)msg.obj;
            	textViewDate.setText(myDate.Date);
            	textViewTime.setText(myDate.Time);
            	textViewWeek.setText(myDate.Week);

                setLight(MainActivity.this, myDate.brightness);
            }
        };
        
        Runnable r=new Runnable(){
        	public void run() {
                // TODO Auto-generated method stub
                try {
                    while(true){
                    	
                    	SHDate myDate = new SHDate();
                    	
                        SimpleDateFormat sdfDate=new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat sdfTime=new SimpleDateFormat("HH:mm:ss");
                        
                        Date date = new Date();

                        String strDate=sdfDate.format(date);
                        String strTime=sdfTime.format(date);
                        
                        myDate.Date = strDate;
                        myDate.Time = strTime;


                        Calendar cal = Calendar.getInstance();
                        int nWeek = cal.get(Calendar.DAY_OF_WEEK);
                        myDate.Week = getWeek(nWeek);

                        int nHour = cal.get(Calendar.HOUR_OF_DAY);

                        myDate.brightness = calBrightnesWith(nHour,nWeek);

                        handler.sendMessage(handler.obtainMessage(100,myDate));
                        Thread.sleep(1000);
                    }
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };
        
        Thread t=new Thread (r);
        t.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public static String getWeek(int i){
        switch (i) {
        case 1:
            return "星期日";
        case 2:
            return "星期一";
        case 3:
            return "星期二";
        case 4:
            return "星期三";
        case 5:
            return "星期四";
        case 6:
            return "星期五";
        case 7:
            return "星期六";
        default:
            return "";
        }
    }

    public static int calBrightnesWith(int mHour, int mWeek){
        //0-255
        int baseV = 255;

        int nbrightness= baseV/2;

        if (mHour>=0 && mHour<7) //睡觉
        {
            nbrightness = 3;
        }
        else if(mHour>=7 && mHour<8)//  起床
        {
            nbrightness =(int) (baseV* 0.6);
        }
        else if(mHour>=8 && mHour<19)//白天
        {
            nbrightness =(int) (baseV* 0.01);
        }
        else if(mHour>=19 && mHour<=24)//下班
        {
            nbrightness =(int) (baseV* 0.2);
        }
        return nbrightness;
    }
    
 
	private void setLight(final Activity context, final int brightness) {

        WindowManager.LayoutParams lp = context.getWindow().getAttributes();

        if (brightness != lp.screenBrightness)
        {
            lp.screenBrightness = Float.valueOf(brightness) * (1f / 255f);
            context.getWindow().setAttributes(lp);
        }

}
    
    
}
