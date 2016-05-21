package org.arsee.everyday;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CurrentJobFragment extends Fragment {

	/////////////////////////////////////////////////////////////
	///InputCurrentJobActionInterface
	public interface InputCurrentJobActionInterface{
		void handle(DayDid dayDidy);
	}

	final static int  REQUEST_CURRENT_JOB_INPUT = 1;
	View view;
	TimeArea currentArea = null;
	InputCurrentJobActionInterface inputCurrentJobAction;
	String m_jobName = "";
	public String getJobName(){ return m_jobName;}
	
	public void setInputCurrentJobAction(	InputCurrentJobActionInterface inputCurrentJobAction) {
			this.inputCurrentJobAction = inputCurrentJobAction;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		view = inflater.inflate(R.layout.fragment_current_job, container, false);
				
		TextView jobText = (TextView)view.findViewById(R.id.current_job_actual);
		jobText.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Toast toast = Toast.makeText(CurrentJobFragment.this.getActivity(), "view.onclick", Toast.LENGTH_LONG);
				//toast.show();
				
				FragmentManager fm = getActivity().getFragmentManager();            	
            	EditJobDialog edtDlg = new EditJobDialog();
            	edtDlg.setTargetFragment(CurrentJobFragment.this, REQUEST_CURRENT_JOB_INPUT);
            	edtDlg.show(fm, "edit");
            	getActivity().sendBroadcast(new Intent("org.arsee.action.test"));
			}
		});
			
		return view;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		if( resultCode != Activity.RESULT_OK) return;
		
		if(requestCode == REQUEST_CURRENT_JOB_INPUT){	
			m_jobName = data.getStringExtra(EditJobDialog.EXTRA_JOB_NAME);
			String jobType = data.getStringExtra(EditJobDialog.EXTRA_JOB_TYPE);
			if(!m_jobName.isEmpty()){
				TextView jobText = (TextView)view.findViewById(R.id.current_job_actual);
				jobText.setText(m_jobName);
				DayDid day = new DayDid();
				day.setDate(Day.getToday());
				Job job = new Job(m_jobName);
				job.setType(jobType);
				day.putJob(job);
				if(this.inputCurrentJobAction != null){
					this.inputCurrentJobAction.handle(day);
				}
			}
		}
	}
	
	
	

}
