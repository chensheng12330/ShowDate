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
import android.widget.TextView;


@SuppressLint({ "SimpleDateFormat", "HandlerLeak" }) public class MainActivity extends Activity {

	private TextView textViewTime;
	private TextView textViewDate;
	private TextView textViewWeek;
	
	public class SHDate {
		public String Date;
		public String Time;
		public String Week;
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
        
        handler = new Handler() {
            public void handleMessage(Message msg) {
            	
            	SHDate myDate =  (SHDate)msg.obj;
            	textViewDate.setText(myDate.Date);
            	textViewTime.setText(myDate.Time);
            	textViewWeek.setText(myDate.Week);
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
                        myDate.Week = getWeek();
                        
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
    
    public static String getWeek(){
        Calendar cal = Calendar.getInstance();
        int i = cal.get(Calendar.DAY_OF_WEEK);
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
    
 
	private void setLight(final Activity context, final int brightness) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.screenBrightness = Float.valueOf(brightness) * (1f / 255f);
        context.getWindow().setAttributes(lp);
}
    
    
}
