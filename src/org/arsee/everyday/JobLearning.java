package org.arsee.everyday;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class JobLearning{

	private static final int MAX_JOBS=9;
	
	public static JobLearning getInstance(){ 
		if(m_myself == null)
			m_myself = new JobLearning();
			
		return m_myself;
	}
	
	private static JobLearning m_myself=null;
	
	private JobLearning(){
		this.m_entyList.add( new JobLearningEnty("吃饭", "生活") );
		this.m_entyList.add( new JobLearningEnty("睡觉", "生活") );
		this.m_entyList.add( new JobLearningEnty("买菜", "生活") );
		
		this.m_entyList.add( new JobLearningEnty("上班", "工作") );
		this.m_entyList.add( new JobLearningEnty("下班", "工作") );
		this.m_entyList.add( new JobLearningEnty("学习", "工作") );
		
		this.m_entyList.add( new JobLearningEnty("听音乐", "休闲娱乐") );
		this.m_entyList.add( new JobLearningEnty("散步", "休闲娱乐") );
		this.m_entyList.add( new JobLearningEnty("看电影", "休闲娱乐") );
		
	}
	
	static public class JobLearningEnty {
	
		public JobLearningEnty(String key, String type){
			m_key = key;
			m_type = type;
		}
		
		public JobLearningEnty(DayDid.Element e){
			m_key = e.job.getTitle();
			m_type = e.job.getType();
			m_clock.add( Clock.toClock(e.time) );
		}
		
		public void persistent(SQLiteDatabase db){
			int whose = User.getInstance().getID();
			try{
					db.execSQL("insert into JobLearning(key, type, clock,whose) values(?, ?,?,?)"
						,  new Object[]{m_key, m_type, 0, whose } );
			}catch(Exception exp){
				Log.d("JobLearningEnty.persistent()", exp.toString());
				//if insert failed it means the key is existed,so refCount++
				try{
					db.execSQL("update  JobLearning set refCount=refCount+1, type=? where key=?", new Object[]{m_type, m_key});;
				}catch(Exception e){
					Log.d("JobLearningEnty.persistent()", e.toString());
				}
			}
			
			try{
				for(int i=0; i<m_clock.size(); i++){
					Cursor c = db.rawQuery("select * from Joblearning_clock where key=? and clock=? and whose=?"
							, new String[]{m_key, Integer.toString( m_clock.get(i)),  Integer.toString(whose)});
					if( c.getCount() == 0 ){
						db.execSQL("insert into JobLearning_clock(key, clock,whose) values(?, ?,?)"
							,  new Object[]{m_key,  m_clock.get(i), whose } );
					}
				}
			}catch(Exception exp){
				Log.d("JobLearningEnty.persistent()", exp.toString());
			}
		}
		
		public String getKey() {
			return m_key;
		}
		public void setKey(String m_key) {
			this.m_key = m_key;
		}
		public String getType() {
			return m_type;
		}
		public void setType(String m_type) {
			this.m_type = m_type;
		}
		public List<Integer> getClock() {
			return m_clock;
		}
		public void setClock(int m_clock) {
			this.m_clock.add(m_clock);
		}
		
		private String m_key;
		private String m_type;
		private List<Integer> m_clock = new ArrayList<Integer>();
		//private String m_whose;
	}
 
	private ArrayList<JobLearningEnty> m_entyList = new ArrayList<JobLearningEnty>();
	public ArrayList<JobLearningEnty> getEntyList(SQLiteDatabase db) {
		int clock = Clock.toClock( Day.getNow() );
		return selectEnties(db, clock);
	}
 
	private ArrayList<JobLearningEnty> selectEnties(SQLiteDatabase db, int clock){
		int whose = User.getInstance().getID();
		ArrayList<JobLearningEnty> enties = new ArrayList<JobLearningEnty>();
		try{
			Cursor c = db.rawQuery("select JobLearning.key, JobLearning.type "+
				    "from JobLearning join JobLearning_clock on "+
					"JobLearning.key=JobLearning_clock.key and JobLearning.whose=JobLearning_clock.whose "+
					"where (JobLearning_clock.clock<=?+1 and JobLearning_clock.clock>=?-1) and JobLearning.whose=? order by refCount desc limit 9;" 
					, new String[]{Integer.toString(clock),  Integer.toString(clock), Integer.toString(whose)});
	
			while( c.moveToNext() ){
				String  key = c.getString(0);			
				String type = c.getString(1);
				enties.add( new JobLearningEnty(key, type) );
			}
			
			//if less than MAX_JOBS, so ignore the clock
			int n = MAX_JOBS - enties.size();
			if( n > 0){
				Cursor cc = db.rawQuery("select key, type "+
					    "from JobLearning "+
						" where (clock>?+1 or clock<?-1) and whose=? order by refCount desc limit ?;" 
						, new String[]{Integer.toString(clock),  Integer.toString(clock)
						, User.getInstance().getName(), Integer.toString(n)}
				);
		
				while( cc.moveToNext() ){
					String  key = cc.getString(0);			
					String type = cc.getString(1);
					enties.add( new JobLearningEnty(key, type) );
				}
			}
		}catch(Exception exp){
			Log.d("JobLearning.selectEnties()", exp.toString());
		}
		
		//if less than MAX_JOBS, so add the presets
		int n = MAX_JOBS - enties.size();
		for(int i=0; i< this.m_entyList.size(); i++){
			if(n <= 0)
				break;
			
			int j=0;
			for( j=0; j<enties.size(); j++){
				if( enties.get(j).getKey().equals( m_entyList.get(i).getKey()) 
						&& enties.get(j).getType().equals( m_entyList.get(i).getType())
				){
					break;
				}					
			}
			if( j >= enties.size() ){
				enties.add(  this.m_entyList.get(i));
				n--;
			}
			
		}
		
		return enties;
	}
 
}
