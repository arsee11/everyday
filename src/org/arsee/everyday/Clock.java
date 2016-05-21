//Clock.java

package org.arsee.everyday;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Clock{
	public static final int CLOCK_COUNT = 48;
	
	public static int getNow(){
		int hour =Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		int minute  =Calendar.getInstance().get(Calendar.MINUTE);
		int now = hour * 2;
		if( minute >= 30 )
			now++;
		
		return now;
	}
	
	///@param time hh:mm:ss
	public static int toClock(String time){
		try{
			Date date = new SimpleDateFormat("HH:mm:ss").parse(time);
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			int hour = c.get(Calendar.HOUR_OF_DAY);
			int minute  = c.get(Calendar.MINUTE);
			int now = hour * 2;
			if( minute >= 30 )
				now++;
			
			return now;
		}catch(ParseException e){
			e.printStackTrace();
			return -1;
		}	
	}
	
	public static float diffHours(String time1, String time2){
		try{
			Date date = new SimpleDateFormat("HH:mm:ss").parse(time1);
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			int hour1 = c.get(Calendar.HOUR_OF_DAY);
			int minute1  = c.get(Calendar.MINUTE);
			int ms1 = hour1*60 + minute1;
			
			Date date2 = new SimpleDateFormat("HH:mm:ss").parse(time2);
			Calendar c2 = Calendar.getInstance();
			c2.setTime(date2);
			int hour2 = c2.get(Calendar.HOUR_OF_DAY);
			int minute2  = c2.get(Calendar.MINUTE);
			int ms2 = hour2*60 + minute2;
			float hours =( ms2-ms1 )/ 60.0f; 
			return hours;
		}catch(ParseException e){
			e.printStackTrace();
			return 0;
		}	
	}
	
	public Clock(int clock){
		this.clock = clock;
	}
	
	public int get(){ return clock;}
	public String toString()
	{ 
		if( clock %2 == 0)
			return clock/2 + ":00";
		else
			return clock/2 + ":30";
	}
	
	private int clock;

}
