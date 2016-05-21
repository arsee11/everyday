package org.arsee.everyday;

import java.util.ArrayList;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ReportDetail {
	
	private static ArrayList<ReportDetailItem> m_report;
	public static ArrayList<ReportDetailItem>  getReportDetail(boolean isNew, SQLiteDatabase db, String day, String jobType){
		if(isNew)
			m_report =  selectFromDb(db, day, jobType);
		
		return m_report;
	}
	
	private static ArrayList<ReportDetailItem> selectFromDb(SQLiteDatabase db, String day, String jobType){
		ArrayList<ReportDetailItem> report = new ArrayList<ReportDetailItem>();
		Cursor c = db.rawQuery("select Job.type, Job.title, time from DayDid join Job on DayDid.job_id=Job.id"
														+" where date=? and Job.type=? order by DayDid.time"
														,  new String[]{ day, jobType }
		);		
	
		while( c.moveToNext() ){		
		    report.add( new ReportDetailItem(c.getString(0)
		    						,c.getString(1)
		    						,c.getString(2)));		
		}
		return report;
	}
	public static  class ReportDetailItem{
	
		ReportDetailItem(String type, String title, String time)
		{
			setJobType(type);
			setJobTitle(title);
			setJobTime(time);
		}
		
		private String jobTitle;
		private String jobType;
		private String jobTime;
		public String getJobTitle() {
			return jobTitle;
		}
		public void setJobTitle(String jobTitle) {
			this.jobTitle = jobTitle;
		}
		public String getJobType() {
			return jobType;
		}
		public void setJobType(String jobType) {
			this.jobType = jobType;
		}
		public String getJobTime() {
			return jobTime;
		}
		public void setJobTime(String jobTime) {
			this.jobTime = jobTime;
		}		
		
		
	}
}
