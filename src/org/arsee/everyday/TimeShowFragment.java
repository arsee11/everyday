package org.arsee.everyday;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TimeShowFragment extends Fragment {

	private View view;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		view = inflater.inflate(R.layout.fragment_time_show, container, false);
		showDateTime();
		return view;
	}
	
	public void showDateTime(){
		TextView todayView = (TextView)view.findViewById(R.id.current_date_show);		
		TextView nowView = (TextView)view.findViewById(R.id.current_time_show);			
		todayView.setVisibility(View.VISIBLE);
		nowView.setVisibility(View.VISIBLE);
		todayView.setText(Day.getToday());
		todayView.setBackgroundColor(0xa0a0a0);
		todayView.setOnClickListener(null);			
		nowView.setText(Day.getNow());	
	}
}
