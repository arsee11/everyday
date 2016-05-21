package org.arsee.everyday;

import java.util.Calendar;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class TickHandler{
	public final static int MSG_UPDATE_DATETIME = 0;
	public final static int MSG_ALARM = 1;
	public final static int MSG_CLOCKON = 2;
	
	public TickHandler(Context c, boolean isNeedAlarm){
		if(isNeedAlarm){
			 mAlarm = new Alarm( mAlarmHandle);
		}
		mContext = c;
	}
	
	private Context mContext;
	
	interface TaskInterface{
		void run();
	}
	
	Handler msgHandler;
	void setMsgHandler(Handler value){
		msgHandler = value;
	}
		
	Alarm.AlarmHandle mAlarmHandle = new  Alarm.AlarmHandle(){
		@Override
		public void alarm(TimeArea area){
			if(msgHandler != null){
				Message msg = new Message();
				msg.what = MSG_ALARM;
				msg.obj = area;
				msgHandler.sendMessage(msg);
			}
		}
	}; 
	
	Alarm mAlarm = null;
	
		
	class IsClockOn implements TaskInterface{
		@Override
		public void run(){
			//int hour = Calendar.getInstance().get(Calendar.HOUR);
			int min = Calendar.getInstance().get(Calendar.MINUTE);
			int snd = Calendar.getInstance().get(Calendar.SECOND);
			if( min == 0 && snd == 0){
				if(msgHandler != null)
				{
					Message msg = new Message();
					msg.what = MSG_CLOCKON;;
					msgHandler.sendMessage(msg);
				}
			}
		}
	}

	public void run() {
		try{
			Thread.sleep(10);
		}catch(InterruptedException e){
		}
		
		if(msgHandler != null)
		{
			Message msg = new Message();
			msg.what = MSG_UPDATE_DATETIME;
			msgHandler.sendMessage(msg);
		}
		
		if(mAlarm != null){
			mAlarm.run();			
			new IsClockOn().run();
		}	
	}
	
	
}
