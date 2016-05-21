package org.arsee.everyday;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.MediaStore.Audio;
import android.util.Log;

public class TickService extends Service {
	public static final String ACTION_UPDATE_DATETIME = "org.arsee.everyday.updatetime";
	public static final String ACTION_CLOCKON = "org.arsee.everyday.clockon";
	
	public static void setServiceAlarm(Context c, boolean isOn){
		Intent i = new Intent(c, TickService.class);
		PendingIntent pi= PendingIntent.getService(c, 0, i, 0);
		AlarmManager am  = (AlarmManager)c.getSystemService(Context.ALARM_SERVICE);
		if(isOn){
			am.setRepeating(AlarmManager.RTC_WAKEUP, 
					System.currentTimeMillis(), 1000, pi);
		}else{
			am.cancel(pi);
			pi.cancel();
		}
	}
	private TickHandler ticker = new TickHandler(this, true);
	
	private final IBinder binder = new LocalBinder();  

    public class LocalBinder extends Binder {  
    	TickService getService() {  
            return TickService.this;  
        }  
    }  
  
    @Override
    public IBinder onBind(Intent intent) {  
        return binder;  
    }  

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		ticker.setMsgHandler(mHandler);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.d("TickService", "onStartCommand");
		ticker.run();
		return super.onStartCommand(intent, flags, startId);			
	}
	
	//创建通知  
    public void CreateNotify(String msg) {  
        Intent intent = new Intent(this,MainActivity.class);  
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);  
      
    	Notification.Builder builder = new Notification.Builder(this);
    	builder.setSmallIcon(R.drawable.ic_launcher);
    	builder.setContentTitle(msg);
    	builder.setLights(Color.RED, 500, 1000);
    	builder.setSound(Uri.parse(Audio.Media.ALBUM));
    	builder.setContentIntent(	pendingIntent); 
        Notification notification =builder.build();  
        NotificationManager nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);  
        nManager.notify(100, notification);//id是应用中通知的唯一标识  
        //startForeground(100, notification);   //一直存在
    }  

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	//Handler message
	Handler mHandler = new Handler(){
			public void handleMessage(Message msg){
				if(msg.what == TickHandler.MSG_UPDATE_DATETIME){
					//send broadcast intent to MainActivity
					Intent i = new Intent(ACTION_UPDATE_DATETIME);
					//i.setAction( ACTION_UPDATE_DATETIME);
					TickService.this.sendBroadcast(i);
					//ServiceGuard g = new ServiceGuard(TickService.this);
					//g.keepRunning(LockScreenService.class);
				}else if(msg.what==TickHandler.MSG_ALARM){
					TimeArea area = (TimeArea)msg.obj;
					Intent i = new Intent(TickService.this, AlarmActivity.class);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					i.putExtra(AlarmActivity.JOB_TITLE, area.getJobs().get(0).getTitle());
					i.putExtra(AlarmActivity.JOB_ID, area.getJobs().get(0).getId());
					i.putExtra(AlarmActivity.JOB_END_TIME, area.getEnd().get());
					i.putExtra(AlarmActivity.JOB_START_TIME, area.getBegin().get());
					startActivity(i);	
					CreateNotify(area.getJobs().get(0).getTitle());
				}
				else if(msg.what == TickHandler.MSG_CLOCKON)	{
					//currentJobFragment.showCurrentJob();
					Intent i = new Intent(ACTION_CLOCKON);
					//i.setAction( ACTION_CLOCKON);
					TickService.this.sendBroadcast(i);	
				}
		}		
	};	  	
}
