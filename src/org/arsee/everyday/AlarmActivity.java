package org.arsee.everyday;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class AlarmActivity extends Activity {
	public static final String JOB_ID = "org.arsee.everyday.jobid";
	public static final String JOB_TITLE = "org.arsee.everyday.job_title";
	public static final String JOB_START_TIME = "org.arsee.everyday.job_start";
	public static final String JOB_END_TIME = "org.arsee.everyday.job_end";
	
	private int mJobId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.activity_alarm);
		
		receiveExtras();
		
		Button okBtn = (Button)this.findViewById(R.id.alarm_button_cancel);
		okBtn.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
			});
		}
		
	private void receiveExtras(){
		Bundle b = getIntent().getExtras();
		String jobTitle = b.getString( JOB_TITLE );
		mJobId = b.getInt( JOB_ID );
		int start = b.getInt( JOB_START_TIME );
		int end = b.getInt( JOB_END_TIME );
		TextView jobTitleView = (TextView)this.findViewById(R.id.alarm_textView_job);
		jobTitleView.setText(jobTitle);
		
		TextView jobStartView= (TextView)this.findViewById(R.id.alarm_textView_jobStart);
		jobStartView.setText( new Clock(start).toString() );
		
		TextView jobEndView = (TextView)this.findViewById(R.id.alarm_textView_jobEnd);
		jobEndView.setText( new Clock(end).toString() );
	}


}
