package org.arsee.everyday;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DbManager {

	private JobDatabaseHelpler dbHelper;
	private SQLiteDatabase db;
	
	public DbManager(Context context) {  
		dbHelper = new JobDatabaseHelpler(context);  
        db = dbHelper.getWritableDatabase();  
    }  
	void closeDB(){
		db.close();
	}
	
	SQLiteDatabase getDB(){ return db;}
	
	//@return the new  id if success else 0
	public int insertOne(Job job){
		//title and type are primary key
		Cursor c = db.rawQuery("select id from Job where title=? and type=?",  new String[]{job.getTitle(), job.getType()} );
		if(c.moveToNext())
		{
			return c.getInt(0);
		}
		
		db.execSQL( "insert into Job(title, type)  values(?,?)", new Object[]{job.getTitle(), job.getType()} );
		int id = getID("Job");
		return id;
	}
	
	//@return the new  id if success else 0
	public int  insertArea(TimeArea area){
		int areaID=0;
		db.beginTransaction();
		try{
			db.execSQL( "insert into TimeArea(begin, end)  values(?,?)", new Object[]{area.getBegin().get(), area.getEnd().get()} );
			areaID =getID("TimeArea");
			
			for(Job job : area.getJobs()){
				int jobID = insertOne(job);
				ContentValues values = new ContentValues();
				values.put("object_id", areaID);
				values.put("value",  jobID);
				db.insert("TimeArea_jobs", null, values);
			}
			db.setTransactionSuccessful();
		}finally{
			db.endTransaction();
		}
		
		return areaID;
	}
	
	public ArrayList<TimeArea> GetTimeAreas(String date){
		ArrayList<TimeArea> areas = new ArrayList<TimeArea>();
		Cursor c = db.rawQuery("select Day.id, Day.date, Day.whose, TimeArea.id, TimeArea.begin, TimeArea.end, Job.id, Job.title,Job.type "+
													    "from Day join Day_areas on Day.id=Day_areas.object_id "+
														"join TimeArea on Day_areas.value = TimeArea.id "+
														"join TimeArea_jobs on TimeArea_jobs.object_id = TimeArea.id "+
														"join Job on Job.id=TimeArea_jobs.value where Day.date=?;" 
														, new String[]{date});
		
		int areaID = 0;
		TimeArea area = new TimeArea(0,0);	
		while( c.moveToNext() ){
			int areaIDTmp = c.getInt(3);			
			if( areaIDTmp != areaID){
				area = new TimeArea(c.getInt(4), c.getInt(5));		
				area.setId(areaIDTmp);
				areas.add(area);
				areaID = areaIDTmp;
			}
			Job job = new Job( c.getString(7) );
			job.setId( c.getInt(6) );
			job.setType( c.getString(8) );			
			area.attachJob(job);			
		}
		
		return areas;
	}
	
	//@return the new  id if success else 0
	public int  saveDay(Day day){
		int userID = 0;
		int dayID=0;
		if(day.getAreas().size() == 0)
			return dayID;
		
		try{
			db.beginTransaction();
			db.execSQL("insert into Day(date, whose) values(?,?)",  new Object[]{day.getDate(), userID} );
			dayID = getID("Day");
			for( TimeArea area : day.getAreas()){
				int areaID = insertArea(area);
				db.execSQL("insert into Day_areas(object_id, value) values(?,?)",  new Object[]{dayID, areaID} );
			}
			db.setTransactionSuccessful();
		}catch(Exception e){
			Log.d("DbManager.SaveDay()", e.toString());
		}finally{
			db.endTransaction();		
		}
		return dayID;
	}
	
	public void updateDay(Day day){
		clearDay(day);
		saveDay(day);
	}
	
	public void clearDay(Day day){
		if(day.getAreas().size() == 0)
			return ;
		
		try{
			db.beginTransaction();
			int dayID = 0;
			Cursor cursor = db.rawQuery("select id from Day where date=?", new String[]{ day.getDate()} );
			if( cursor.moveToFirst())
				dayID = cursor.getInt( 0);
			
			db.execSQL("delete from Day where date=?",  new Object[]{day.getDate()} );
			db.execSQL("delete from Day_areas where object_id=?",  new Object[]{dayID} );
			
			for( TimeArea area : day.getAreas()){
					db.execSQL("delete from TimeArea  where id=?",  new Object[]{area.getId()} );
					db.execSQL("delete from TimeArea_jobs where object_id=?",  new Object[]{area.getId()} );
			}
			db.setTransactionSuccessful();
		}catch(Exception e){
			Log.d("DbManager.SaveDay()", e.toString());
		}finally{
			db.endTransaction();		
		}
	}
	
	private int getID(String table){
		Cursor cursor = db.rawQuery("select max(id) from "+table, null);
		cursor.moveToFirst();
		int id = cursor.getInt( 0);
		return id;
	}
	
	public void saveDayDid(DayDid dayDid){
		DayDid.Element e = dayDid.getJobs().get(0);
		try{
			int jobId = this.insertOne(e.job);
			db.execSQL("insert into DayDid(date, time, job_id) values(?,?, ?)",  new Object[]{dayDid.getDate(), e.time, jobId} );
		}catch(Exception exp){
			Log.d("DbManager.saveDayDid()", exp.toString());
		}
	}
	
	public void saveJobLearning(DayDid dayDid){
		DayDid.Element e = dayDid.getJobs().get(0);
		JobLearning.JobLearningEnty enty = new JobLearning.JobLearningEnty(e);
		enty.persistent(db);		
	}
}
