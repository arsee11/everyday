package org.arsee.everyday;

import java.util.ArrayList;

import org.arsee.everyday.MainActivity.CurrentJobAction;
import org.arsee.everyday.MainActivity.TickBroadcastReceiver;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class LockActivity extends Activity {

	private HeaderFragment header = new HeaderFragment(false);
	private CurrentJobFragment currentJobFragment = new CurrentJobFragment();
	private JobShowFragment jobShowFragment = new JobShowFragment();
	private TimeShowFragment timeShowFragment = new TimeShowFragment();
	private Day m_Day;
	private DbManager m_DbManager;
	public static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);  
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  WindowManager.LayoutParams.FLAG_FULLSCREEN);            
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED, WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);        
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD, 	WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);  
        getWindow().setFlags(FLAG_HOMEKEY_DISPATCHED, FLAG_HOMEKEY_DISPATCHED);
        getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
        m_DbManager = new DbManager(this);
        currentJobFragment.setInputCurrentJobAction(new CurrentJobAction());
        initToday();
        
        setContentView(R.layout.activity_lock);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.activity_lock_header, header);
        ft.add(R.id.activity_lock_current, currentJobFragment);
        ft.add(R.id.activity_lock_taskshow , jobShowFragment);
        ft.add(R.id.activity_lock_time_show, timeShowFragment);
        ft.commit();   
        
        Button mainButton = (Button)findViewById(R.id.lock_unlock_button);
		mainButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				if( !currentJobFragment.getJobName().isEmpty() ){
					finish();
				}
			}
		});
       
		this.registerReceiver(tickReceiver, new IntentFilter(TickService.ACTION_UPDATE_DATETIME));
		this.registerReceiver(tickReceiver, new IntentFilter(TickService.ACTION_CLOCKON));        
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		Log.d("LockActivity", "onStop()");
		super.onStop();
//		finish();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	private void loadDay(String date){
		m_Day = new Day(date);
		ArrayList<TimeArea> areas = m_DbManager.GetTimeAreas(date);
		for(TimeArea area : areas)
		{
			m_Day.addTimeArea(area);
		}
	}
	
	private void initToday(){
			loadDay(Day.getToday());
			Today.instance().setDay(m_Day);
	}

	 //使back键，音量加减键失效
	   @Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
		   	return disableKeycode(keyCode, event);
		}
	    
	    private boolean disableKeycode(int keyCode, KeyEvent event)
	    {
	    	int key = event.getKeyCode();
	    	switch (key)
	    	{
	    	case KeyEvent.KEYCODE_HOME:
	    	case KeyEvent.KEYCODE_BACK:		
			case KeyEvent.KEYCODE_VOLUME_DOWN:
			case KeyEvent.KEYCODE_VOLUME_UP:
						return true;			
			}
	    	return super.onKeyDown(keyCode, event);
	    }
	    
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Log.d("LockActivity", "onDestroy()");		
		this.unregisterReceiver(tickReceiver);
		super.onDestroy();
	}

	///////////////////////////////////////
	//JobEditAction
	class CurrentJobAction implements CurrentJobFragment.InputCurrentJobActionInterface{
		@Override
		public void handle(DayDid dayDid){
			m_DbManager.saveDayDid(dayDid);
			m_DbManager.saveJobLearning(dayDid);
		}
	}

	/////////////////////////////////////////////////////
	BroadcastReceiver tickReceiver  = new TickBroadcastReceiver();
	class TickBroadcastReceiver extends BroadcastReceiver{
	
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if(intent.getAction().equals(TickService.ACTION_UPDATE_DATETIME) ){
				timeShowFragment.showDateTime();
			}
			else if(intent.getAction().equals(TickService.ACTION_CLOCKON)){
				jobShowFragment.showNext_4_Jobs();	
			}
			else{
			
			}
		}

	}
}
