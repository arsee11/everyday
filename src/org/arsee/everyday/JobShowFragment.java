package org.arsee.everyday;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.ListFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class JobShowFragment extends ListFragment {
	
	Day day = new Day();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		showNext_4_Jobs();		
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		
		//this.getListView().setDividerHeight(0);
		//this.getListView().setd
		//this.getListView().setBackgroundColor(0xf0f0f0);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		
	}

	private class JobListAdapter extends ArrayAdapter<TimeArea>{
		public JobListAdapter(ArrayList<TimeArea> areas){
			super(getActivity(), 0, areas);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView==null){
				convertView = getActivity().getLayoutInflater().inflate(R.layout.fragment_current_jobs_show, null);
			}
			
			TimeArea a = getItem(position);
			if(position==0){
				showCurrentJob(convertView);
			}
			TextView idxTextView=(TextView)convertView.findViewById(R.id.job_show_idx_view);
			idxTextView.setText(getIndexString(position));
			TextView titleTextView = (TextView)convertView.findViewById(R.id.job_show_clock_view);
			String c= a.getBegin().toString();
			titleTextView.setText(c);
			
			TextView dateTextView = (TextView)convertView.findViewById(R.id.job_show_content_view);
			dateTextView.setText(a.getJobs().get(0).getTitle());	
			
			return convertView;
		}
	}
	
	private String getIndexString(int idx){
		if(idx==0)
			return "1st";
		
		if(idx==1)
			return "2nd";
		
		if(idx==2)
			return "3rd";
		
		if(idx==3)
			return "4th";
		
		return "";
	}
	
	public void showNext_4_Jobs(){
		Day day = Today.instance().getDay();
		if( day != null){
			int currentIdx = day.getAreas().size();
			ArrayList<TimeArea> next4s = new ArrayList<TimeArea>();
			//current job
			for(int i=0; i<day.getAreas().size(); i++){			
					int now =Clock.getNow();
					if( day.getAreas().get(i).isIn(now) ){
						currentIdx = i;
					}							
			}
			
			//nearest job
			if(currentIdx >= day.getAreas().size()){
				for(int i=0; i<day.getAreas().size(); i++){			
					int now =Clock.getNow();
					if( day.getAreas().get(i).isLessThan(now) ){
						currentIdx = i;
					}	
				}
			}
			
			//next 3 job
			for(int j=currentIdx; j<currentIdx+4&&j<day.getAreas().size(); j++){
				next4s.add(day.getAreas().get(j));
			}
			JobListAdapter adapter = new JobListAdapter(next4s);
			setListAdapter(adapter);
			adapter.notifyDataSetChanged();
		}
	}
	
	public void showCurrentJob(View v){
		TextView contentTextView = (TextView)v.findViewById(R.id.job_show_content_view);
		contentTextView.setTextColor(Color.RED);
		
	}
}
