package org.arsee.everyday;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.app.FragmentManager;
import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ReportFragment extends ListFragment {

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		pullDatas();		
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		
		ReportSimple.ReportSimpleItem e = ReportSimple.getReportSimple( false
				,((MainActivity)getActivity()).getDB(), Day.getToday() )
				.get(position) ;
		
		ReportDetailDialog dlg = new ReportDetailDialog(e.getJobType());
    	FragmentManager fm = getActivity().getFragmentManager();  
    	//dlg.setTargetFragment(ReportFragment.this);
    	dlg.show(fm, "Report detail");
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.d(this.getClass().getName(), "onStart");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d(this.getClass().getName(), "onResume");
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
		if( !hidden ){
			pullDatas();
		}
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.d(this.getClass().getName(), "onPause");
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.d(this.getClass().getName(), "onStop");
	}

	private class JobListAdapter extends ArrayAdapter<ReportSimple.ReportSimpleItem>{
		public JobListAdapter(){
			super(getActivity(), 0, 
					ReportSimple.getReportSimple(true, ((MainActivity)getActivity()).getDB(), Day.getToday() ) 
			);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView==null){
				convertView = getActivity().getLayoutInflater().inflate(R.layout.fragment_report, null);
			}
			
			ReportSimple.ReportSimpleItem a = getItem(position);
			TextView idxTextView=(TextView)convertView.findViewById(R.id.report_job_type_view);
			idxTextView.setText(a.getJobType());
			TextView titleTextView = (TextView)convertView.findViewById(R.id.report_job_hour_view);
			DecimalFormat df=new DecimalFormat("0.00");
			titleTextView.setText( df.format(a.getHours()) + "h");
			
			TextView dateTextView = (TextView)convertView.findViewById(R.id.report_job_percent_view);
			dateTextView.setText(df.format(a.getPercent())+ "%");	
			
			return convertView;
		}
	}

	private void pullDatas(){
		JobListAdapter adapter = new JobListAdapter();
		setListAdapter(adapter);
		adapter.notifyDataSetChanged();
	}
	
}
