package org.arsee.everyday;



import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

public class LockScreenService extends Service {

	private Intent mFxLockIntent = null;  
     IntentFilter mScreenOnOrOffFilter = new IntentFilter();
     IntentFilter mTimetickFilter = null;
     private TickHandler ticker = new TickHandler(this, false);
     
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent)
		{
			if (intent.getAction().equals("android.intent.action.SCREEN_ON")
				||intent.getAction().equals("android.intent.action.SCREEN_OFF"))
			{
				//启动该第三方锁屏
				startActivity(mFxLockIntent);
			}	
			
			if (intent.getAction().equals(Intent.ACTION_TIME_TICK))
			{
				ServiceGuard g = new ServiceGuard(LockScreenService.this);
				g.keepRunning(TickService.class);
			}			
		}
	};
	
    @Override
	public void onCreate() 
	{
		// TODO Auto-generated method stub
		super.onCreate();
		mFxLockIntent = new Intent(LockScreenService.this, LockActivity.class);
		mFxLockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		ticker.setMsgHandler(mHandler);
		registerComponent();		
	}
	

	@Override
	public void onDestroy() 
	{
		// TODO Auto-generated method stub
		Log.d(this.getClass().getName(), "onDestroy");
		super.onDestroy();
		unregisterComponent();
		Log.d(this.getClass().getName(), "onDestroy");
		//被销毁时启动自身，保持自身在后台存活
		startService(new Intent(this, LockScreenService.class));
	}
	
	@Override
	public IBinder onBind(Intent arg0)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(this.getClass().getName(), "onStartCommand");
		flags = START_STICKY;
		//CreateInform();	
		 return super.onStartCommand(intent, flags, startId);
	}
	
	//注册广播监听
	public void registerComponent()
	{
		// TODO Auto-generated method stub
		IntentFilter mScreenOnOrOffFilter = new IntentFilter();
		mScreenOnOrOffFilter.addAction("android.intent.action.SCREEN_ON");
		mScreenOnOrOffFilter.addAction("android.intent.action.SCREEN_OFF");
		mTimetickFilter = new IntentFilter(Intent.ACTION_TIME_TICK);
		this.registerReceiver(mReceiver, mScreenOnOrOffFilter);
		this.registerReceiver(mReceiver, mTimetickFilter);
					
	}

    //解除广播监听
	public void unregisterComponent() 
	{
		if (mReceiver != null)
		{
			this.unregisterReceiver(mReceiver);
		}
	}

	//Handler message
	Handler mHandler = new Handler(){
			public void handleMessage(Message msg){
				if(msg.what == TickHandler.MSG_UPDATE_DATETIME){
					ServiceGuard g = new ServiceGuard(LockScreenService.this);
					g.keepRunning(TickService.class);
				}
		}		
	};
}
