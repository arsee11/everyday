package org.arsee.everyday;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ReportSimple {
	
	private static ArrayList<ReportSimpleItem> m_report;
	public static ArrayList<ReportSimpleItem>  getReportSimple(boolean isNew, SQLiteDatabase db, String day){
		if( isNew )
			m_report = selectFromDb(db, day);
		
		return m_report;
	}
	
	private static ArrayList<ReportSimpleItem> selectFromDb(SQLiteDatabase db, String day){
		ArrayList<ReportSimpleItem> report = new ArrayList<ReportSimpleItem>();
		Cursor c = db.rawQuery("select Job.type, time from DayDid join Job on DayDid.job_id=Job.id where date=? order by DayDid.idx"
				,  new String[]{ day });
		
		String type = "",  time="";
		Map<String, Float> map = new HashMap<String, Float>();
		while( c.moveToNext() ){
			String t = c.getString(0);			
			if( !type.equals(t) ){
				if(type.isEmpty() )
				{
					type = t;
					time = c.getString(1);	
				}
				
				float hour =  Clock.diffHours(time, c.getString(1));
				if(map.containsKey(type))
					map.put(type, map.get(type) + hour);		
				else
					map.put(type,  hour);		
				
				type =t;
				time = c.getString(1);	
			}
					
		}
		
		//lastTime	
		float hour =  Clock.diffHours(time, "23:59:59");
		if(map.containsKey(type))
			map.put(type, map.get(type) + hour);		
		else
			map.put(type,  hour);		
		
		float value24 = 24;
		for (Entry<String, Float> entry: map.entrySet()) {			
		    String key = entry.getKey();
		    float value = entry.getValue();
		    report.add( new ReportSimpleItem( key, value, value/24*100 ));
		    value24 -= value;		    
		}
		if( value24 > 0.0 ){
			report.add( new ReportSimpleItem( "未记录", value24, value24/24*100 ));
		}
		return report;
	}
	public static  class ReportSimpleItem{
	
		ReportSimpleItem(String type, float hours, float percent)
		{
			setJobType(type);
			setHours(hours);
			setPercent(percent);
		}
		
		private String jobType;
		private float hours;
		private float percent;
		
		public String getJobType() {
			return jobType;
		}
		public void setJobType(String jobType) {
			this.jobType = jobType;
		}
		public float getHours() {
			return hours;
		}
		public void setHours(float hours) {
			this.hours = hours;
		}
		public float getPercent() {
			return percent;
		}
		public void setPercent(float percent) {
			this.percent = percent;
		}
	}
}
