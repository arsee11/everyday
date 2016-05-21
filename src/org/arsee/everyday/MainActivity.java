package org.arsee.everyday;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.MediaStore.Audio;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;



public class MainActivity extends Activity {
	static final int REQUEST_WHICH_DATE = 0;
	private String date = Day.getToday();
	public String getDate(){
		return date;
	}
	boolean isJobEdit = false;
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		 if(requestCode == REQUEST_WHICH_DATE && resultCode == RESULT_OK){
			this.date = data.getStringExtra(CalendarActivity.EXTRA_DATE);
			loadDay(date);
			jobEditFragment.initShow();
		}
	}
	@Override
	protected void onDestroy() {		
		// TODO Auto-generated method stub
		super.onDestroy();
		this.unregisterReceiver(tickReceiver);
		Intent il = new Intent(this, LockScreenService.class);
		this.startService(il);
		Log.d(this.getClass().getName(), "onDestroy");
		//Toast toast = Toast.makeText(MainActivity.this, "stop", Toast.LENGTH_LONG);
		//toast.show();
	}
	
	private DbManager dbManager;	
	public SQLiteDatabase getDB(){ return dbManager.getDB();}
	private Day day = new Day();
	public Day getDay(){ return day;}
	private JobShowFragment jobShowFragment = new JobShowFragment();
	private JobEditFragment  jobEditFragment = new JobEditFragment();//new JobListFragment();
	private ReportFragment reportFragment = new ReportFragment();
	private CurrentJobFragment currentJobFragment = new CurrentJobFragment();
	private HeaderFragment fheader = new HeaderFragment(true);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);		
		dbManager = new DbManager(this);
		initToday();
		
		jobEditFragment.setJobEditAction(new JobEditAction());
		currentJobFragment.setInputCurrentJobAction(new CurrentJobAction());
		FragmentManager fm =  this.getFragmentManager();				
		FragmentTransaction ftrans = fm.beginTransaction();
		ftrans.add(R.id.activity_main_header, fheader);
		ftrans.add(R.id.activity_main_current, currentJobFragment);
		ftrans.add(R.id.activity_main_taskshow , jobShowFragment);
		ftrans.commit();
		
		Button editButton = (Button)this.findViewById(R.id.edit_button);
		editButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				isJobEdit = true;
				FragmentManager fm =  getFragmentManager();
				
				FragmentTransaction ftrans = fm.beginTransaction();
				ftrans.hide(currentJobFragment);
				ftrans.hide(jobShowFragment);
				ftrans.hide(reportFragment);
				if(!jobEditFragment.isAdded() )
					ftrans.add(R.id.activity_main_taskshow , jobEditFragment);
				else
					ftrans.show(jobEditFragment);
				
				ftrans.commit();				
			}
		});
		
		Button mainButton = (Button)findViewById(R.id.main_button);
		mainButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				isJobEdit = false;
				FragmentManager fm  = getFragmentManager();
				FragmentTransaction ftrans = fm.beginTransaction();
				ftrans.hide(jobEditFragment);
				ftrans.hide(reportFragment);
				ftrans.show(currentJobFragment);
				if(!jobShowFragment.isAdded())
					ftrans.add(R.id.activity_main_taskshow, jobShowFragment);
				else
					ftrans.show(jobShowFragment);				
				ftrans.commit();
			}
		});
		
		Button reportButton = (Button)this.findViewById(R.id.report_button);
		reportButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				isJobEdit = true;
				FragmentManager fm =  getFragmentManager();
				
				FragmentTransaction ftrans = fm.beginTransaction();
				ftrans.hide(currentJobFragment);
				ftrans.hide(jobShowFragment);
				ftrans.hide(jobEditFragment);
				if(!reportFragment.isAdded() )
					ftrans.add(R.id.activity_main_taskshow , reportFragment);
				else
					ftrans.show(reportFragment);
				
				ftrans.commit();				
			}
		});
		
		this.registerReceiver(tickReceiver, new IntentFilter(TickService.ACTION_UPDATE_DATETIME));
		this.registerReceiver(tickReceiver, new IntentFilter(TickService.ACTION_CLOCKON));
		TickService.setServiceAlarm(this, true);
		//Intent il = new Intent(this, LockScreenService.class);
		//this.startService(il);
		//CreateNotify();		
	}
	
	private void loadDay(String date){
		day = new Day(date);
		ArrayList<TimeArea> areas = dbManager.GetTimeAreas(date);
		for(TimeArea area : areas)
		{
			day.addTimeArea(area);
		}
	}
	private void initToday(){
			loadDay(Day.getToday());
			Today.instance().setDay(day);
	}
	
	///////////////////////////////////////
	//JobEditAction
	class JobEditAction implements JobEditFragment.JobEditActionInterface{
		@Override
		public void handle(Day aday){
			if(aday.getDate().equals(day.getDate())){
				Today.instance().setDay(aday);
				//currentJobFragment.showCurrentJob();
				jobShowFragment.showNext_4_Jobs();
			}
			day = aday;
			dbManager.updateDay(day);
		}
	}
	
	/////////////////////////////////////////////////////
	
	///////////////////////////////////////
	//JobEditAction
	class CurrentJobAction implements CurrentJobFragment.InputCurrentJobActionInterface{
		@Override
		public void handle(DayDid dayDid){
			dbManager.saveDayDid(dayDid);
			dbManager.saveJobLearning(dayDid);
		}
	}

	/////////////////////////////////////////////////////
	BroadcastReceiver tickReceiver  = new TickBroadcastReceiver();
	class TickBroadcastReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if(intent.getAction().equals(TickService.ACTION_UPDATE_DATETIME) ){
				//if(isJobEdit){
					fheader.ShowDate();
				//}else{
				//	fheader.showDateTime();
				//}
			}
			else if(intent.getAction().equals(TickService.ACTION_CLOCKON)){
				jobShowFragment.showNext_4_Jobs();	
			}
			else{
				
			}
		}
		
	}

}
