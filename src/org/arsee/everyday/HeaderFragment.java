package org.arsee.everyday;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



public class HeaderFragment extends Fragment {

	View view;
	boolean m_isShowTime = true;
	
	public HeaderFragment(boolean isShowTime){
		m_isShowTime = isShowTime;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		view = inflater.inflate(R.layout.fragment_header, container, false);
		showDateTime();
		return view;
	}

	public void showDateTime(){
		TextView todayView = (TextView)view.findViewById(R.id.current_task_date);		
		TextView nowView = (TextView)view.findViewById(R.id.current_task_time);		
		if(!m_isShowTime ){
			
			todayView.setVisibility(View.INVISIBLE);
			nowView.setVisibility(View.INVISIBLE);
		}else{
			todayView.setVisibility(View.VISIBLE);
			nowView.setVisibility(View.VISIBLE);
			todayView.setText(Day.getToday());
			todayView.setBackgroundColor(0xa0a0a0);
			todayView.setOnClickListener(null);			
			nowView.setText(Day.getNow());			
		}		
	}
	
	public void ShowDate(){
		TextView todayView = (TextView)view.findViewById(R.id.current_task_date);
		if(!m_isShowTime ){			
			
			todayView.setVisibility(View.INVISIBLE);
		}else{
			todayView.setVisibility(View.VISIBLE);
			todayView.setBackgroundColor(0x000000);
			todayView.setOnClickListener( new View.OnClickListener(){
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent i = new Intent(getActivity(), CalendarActivity.class);
					getActivity().startActivityForResult(i, MainActivity.REQUEST_WHICH_DATE);				
				}			
			});
			
			todayView.setText( ((MainActivity)getActivity()).getDate() );
			
			TextView nowView = (TextView)view.findViewById(R.id.current_task_time);		
			nowView.setText("");
		}		
	}
}
