//Alarm.java

package org.arsee.everyday;

import java.util.ArrayList;
	
class Alarm{
	
	public Alarm(AlarmHandle handle){
		this.mHandle = handle;
	}
	
	///////////////////////////////////////
	//AlarmItem
	class AlarmItem{
					
		abstract class State{
			abstract boolean IsOn(int clock);
		}
		
		State mState = new NotAlarmState();
		
		class NotAlarmState extends State{
		
			@Override
			boolean IsOn(int clock) {
				if( Day.getToday().equals(mDate) ){
					if(mTimeArea.isIn(clock )){
						mState = new AtAlarmState();
						return true;
					}
				}
				return false;
			}
		}
		
		class AtAlarmState extends State{
				
				@Override
				boolean IsOn(int clock) {
						return false;
				}				
			}
			
		public AlarmItem(String date, TimeArea area){
			mTimeArea = area;
			mDate = date;
		}
		
		TimeArea mTimeArea;
		String mDate;

		public TimeArea getTimeArea() {
			return mTimeArea;
		}

		public void setTimeArea(TimeArea timeArea) {
			mTimeArea = timeArea;
		}
		
		public void cancel(){
			
		}
		
		public boolean isOn(int clock){			
			return mState.IsOn(clock);
		}
	}
	
	ArrayList<AlarmItem> mAlarmList = new ArrayList<AlarmItem>();
	void  buildAlarm(Day day){
		for( TimeArea area : day.getAreas() ){
			AlarmItem a = new AlarmItem(day.getDate(), area);
			mAlarmList.add(a);
		}
	}
	
	void cancel(AlarmItem item){
		for(AlarmItem i : mAlarmList){
			if( i  == item ){
				mAlarmList.remove(i);
			}
		}
	}

	public interface AlarmHandle{
		void alarm(TimeArea area);
	}

	public void setAlarmHandle(AlarmHandle handle){
		this.mHandle = handle;
	}

	String date = "";
	public void run(){
		if( !date.equals(Day.getToday()) ){
			Day day = Today.instance().getDay();
			if( day == null)
				return;
		
			mAlarmList.clear();
			buildAlarm( day );
			date = Day.getToday();
		}
		
		for( AlarmItem a : mAlarmList){
			if( a.isOn( Clock.getNow() ) ){
				if(mHandle != null)
					mHandle.alarm(a.getTimeArea());
			}				
		}
	}

	AlarmHandle mHandle;
}
