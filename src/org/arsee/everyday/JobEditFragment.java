package org.arsee.everyday;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class JobEditFragment extends Fragment {
	static final int REQUEST_JOB_NAME = 0;	
	static final int TEXT_SIZE = 22;
	static final int ITEM_SIZE = 120;
	static final int ITEM_COUNT = Clock.CLOCK_COUNT;
	static final String empty_job_name = "...";
	private LinearLayout clocksPanel ;
	private LinearLayout contentsPanel;
	private View view;
	private JobEditActionInterface jobEditAction;
	public void setJobEditAction(JobEditActionInterface value){
		jobEditAction = value;
	}
	
	
	/////////////////////////////////////////////////////////////
	///JobEditAction
	public interface JobEditActionInterface{
		void handle(Day aday);
	}
	
	////////////////////////////////////////////////////////
	///MyState
	abstract class MyState  {
		public abstract void onClick(JobItem item);
		public abstract void show();
		public  void edit(){ }
		public void editSubmit(){}
		public void editCancel(){}
	}
	
	private MyState myState  = new ShowState();
	
	class ShowState extends MyState{

		@Override
		public void show() {
			addItem();
			getColor();
			Button btnOK = (Button)JobEditFragment.this.view.findViewById(R.id.job_edit_ok_button);
			btnOK.setVisibility(View.INVISIBLE);
      		LayoutParams lp = btnOK.getLayoutParams();
      		lp.height = 0;
      		btnOK.setLayoutParams(lp);
      		
      		Button btnCancel = (Button)JobEditFragment.this.view.findViewById(R.id.job_edit_cancel_button);
      		btnCancel.setVisibility(View.INVISIBLE);
      		LayoutParams lp2 = btnCancel.getLayoutParams();
      		lp2.height = 0;
      		btnCancel.setLayoutParams(lp2);
		}
		@Override
		public void onClick(JobItem item) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void edit() {
			myState = new EditState();				
		}
		
	}
	
	private class EditState extends MyState{

		@Override
		public void show() {
			contentsPanel.removeAllViews();
      		addItemSplit();
      		Button btn = (Button)JobEditFragment.this.view.findViewById(R.id.job_edit_ok_button);
      		btn.setVisibility(View.VISIBLE);
      		LayoutParams lp = btn.getLayoutParams();
      		lp.height = LayoutParams.WRAP_CONTENT;
      		btn.setLayoutParams(lp);
      		
      		Button btnCancel = (Button)JobEditFragment.this.view.findViewById(R.id.job_edit_cancel_button);
      		btnCancel.setVisibility(View.VISIBLE);
      		LayoutParams lp2 = btnCancel.getLayoutParams();
      		lp2.height = LayoutParams.WRAP_CONTENT;
      		btnCancel.setLayoutParams(lp2);
		}

		@Override
		public void onClick(JobItem item) {
			item.onClick();
			item.show();			
		}

		@Override
		public void editSubmit() {
			if(jobEditAction != null){
				jobEditAction.handle(BuildDay());
			}			
			
			contentsPanel.removeAllViews();
			initJobItems();
			myState = new ShowState();				
		}
		
		public void editCancel(){
			contentsPanel.removeAllViews();
			initJobItems();
			myState = new ShowState();	
		}
	}
	
	
	///////////////////////////////////////////////
	//Job item
	 class JobItem {
	
	////////////////////////////////////////////////////////
	///state
		abstract class State  {
			public abstract void onClick();
			public abstract void show();
			public void appyJobName(String jobName){}
			public boolean isSelected() { return false; }
		}
		
		private State  state = new NormalState();
		public State getState() {
			return state;
		}

		public void setState(State state) {
			if(state  instanceof SelectedState ){
				this.state = new SelectedState();
			}	
			else if( state instanceof  NormalState){
				this.state = new NormalState();
			}
			else if( state instanceof  ActiveState){
				this.state = new ActiveState();
			}
		}
		
		class NormalState extends State{

			@Override
			public void onClick() {
				JobItem.this.state = new SelectedState();
			}
			
			@Override
			public void show() {
				contentView.setText(jobName);
				if(JobItem.this.getColor() != 0 ){
					contentView.setBackgroundColor(JobItem.this.getColor());
					contentView.setTextColor(Color.WHITE);	
				}else{
					contentView.setBackgroundColor(Color.WHITE);
					contentView.setTextColor(Color.BLACK);
				}
			}
		}
		
		class SelectedState extends State{

			@Override
			public void onClick() {
					JobItem.this.state = new NormalState();		
			}
				
			@Override
			public void show() {
				contentView.setBackgroundColor(Color.GREEN);
				contentView.setTextColor(Color.WHITE);		
				contentView.setText(jobName);
			}
			
			public void appyJobName(String jobName){
				JobItem.this.setJobName(jobName);
				JobItem.this.contentView.setText(jobName);
				JobItem.this.state = new NormalState();
			}
			
			public boolean isSelected() { return true; }
		}
		
		class ActiveState extends State{

			@Override
			public void onClick() {
				JobItem.this.state = new NormalState();
			}
			
			@Override
			public void show() {
				contentView.setBackgroundColor(JobItem.this.getColor());
				contentView.setTextColor(Color.WHITE);	
				contentView.setText(jobName);
			}
		}
//state	
////////////////////////////////////////////////////
		
		public JobItem(){
			jobName = new String(empty_job_name);			
		}
		
		public void onClick(){
			state.onClick();
		}
		
		public void show(){
			state.show();
		}
		
		public void appyJobName(String jobName){
			state.appyJobName(jobName);
		}
		
		TimeArea area=null;
		public TimeArea getTimeAera() {
			return area;
		}
		
		public void setTimeArea(TimeArea area){
			this.area = area;
		}
		
		public boolean isSelected() {
			return state.isSelected();
		}

		String jobName;
		public void setJobName(String value){
			jobName = value;
		}
		public String getJobName(){ 
				return jobName;
		}
		
		String jobType;		
		public String getJobType() {
			return jobType;
		}

		public void setJobType(String jobType) {
			this.jobType = jobType;
		}

		int color=0;
		public int getColor() {
			return color;
		}

		public void setColor(int color) {
			this.color = color;
		}
		
		TextView contentView = null;
		public View getContentView(){
			return contentView;
		}
		
		TextView separatorView = null;		
		private void createContentView(int height){
			contentView = new TextView(getActivity());
			contentView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			contentView.setTextSize(TEXT_SIZE);
			contentView.setHeight(height );
			contentView.setPadding(4, 12, 2, 12);
			contentView.setGravity(Gravity.CENTER_VERTICAL);
			contentView.setClickable(true);						
			contentView.setOnClickListener( new View.OnClickListener() {			
				@Override
				public void onClick(View v) {
					JobItem item = findJobItem(jobItems, v);
					JobEditFragment.this.myState.edit();
					JobEditFragment.this.myState.show();
					JobEditFragment.this.myState.onClick(item);					
					
				}
			});
		
			contentView.setOnLongClickListener(new View.OnLongClickListener() {
	          	@Override
				public boolean onLongClick(View v) {
					// TODO Auto-generated method stub
	          		if(!isAnyItemSelected())
	            		return true;
	  	          		
	            	FragmentManager fm = getActivity().getFragmentManager();            	
	            	EditJobDialog edtDlg = new EditJobDialog();
	            	edtDlg.setTargetFragment(JobEditFragment.this, REQUEST_JOB_NAME);
	            	edtDlg.show(fm, "edit");
	            	
	                return true;
				}
			}); 
		
			separatorView = new TextView(getActivity());
			separatorView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 1));
			separatorView.setBackgroundColor(0xffa0a0a0);
			
			contentsPanel.addView(contentView);
			contentsPanel.addView(separatorView);
			
		}	
	}
		
	////////////////////////////////////////////////
	//jobItems
	JobItem[] jobItems = new JobItem[ITEM_COUNT];
	private boolean isAnyItemSelected(){
		for(JobItem i:jobItems){
			if(i != null && i.isSelected())
				return true;
		}
		
		return false;
	}
	
	private void initJobItems(){
		for(int i=0; i<ITEM_COUNT; i++){
			jobItems[i] = null;							
		}	
	}
	private JobItem findJobItem(JobItem[] jobItems, View v){
		for(JobItem i : jobItems){
			if( i != null && i.getContentView() == v){
				return i;
			}
		}
		
		return null;
	}
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		initJobItems();		
	}

	public void initShow(){
		contentsPanel.removeAllViews();
		initJobItems();
		myState = new ShowState();
		myState.show();
	}
	
	@Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		 view = inflater.inflate(R.layout.fragment_job_edit, container, false);
		clocksPanel = (LinearLayout)view.findViewById(R.id.job_edit_clocksPanel);
		contentsPanel = (LinearLayout)view.findViewById(R.id.job_edit_contentsPanel);
		for(int i=0; i<ITEM_COUNT; i++ ){
			addClockView(i);
		}
		myState.show();
		
		Button btnOK = (Button)view.findViewById(R.id.job_edit_ok_button);
		btnOK.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				myState.editSubmit();
				myState.show();
			}
		});
		
		Button btnCancel = (Button)view.findViewById(R.id.job_edit_cancel_button);
		btnCancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				myState.editCancel();
				myState.show();
			}
		});
		return view;
	}
	
	private void getColor(){
		ColorGenerator colorGen = new ColorGenerator();
		for(JobItem item:jobItems){
			if(item != null && item.getTimeAera() != null){
				int color = colorGen.getNewColor();
				item.setColor(color);
				item.show();
			}
		}
	}
	private void  addItem(){
		ArrayList<TimeArea> areas = ((MainActivity)getActivity()).getDay().getAreas();			
		
		for(int i=0; i<ITEM_COUNT; i++ ){
			boolean isInArea = false;
			for(TimeArea area : areas){
				if(area.isIn(i))
				{
					isInArea = true;
					if( jobItems[area.getBegin().get()] == null){
						int height = ITEM_SIZE* (area.getEnd().get()-area.getBegin().get())
										     + area.getEnd().get()-area.getBegin().get()-1; 
						JobItem item = new JobItem();
						jobItems[area.getBegin().get()] = item;
						item.setJobName( area.getJobs().get(0).getTitle() );
						item.createContentView(height);	
						item.setTimeArea(area);
						item.show();		
					}
				}
			}
			if( !isInArea ){
				JobItem item = new JobItem();
				if( jobItems[i] == null){					
					jobItems[i] = item;
				}else{
					item = jobItems[i];
				}
				item.createContentView(ITEM_SIZE);
				jobItems[i].show();	
			}			
		}
	}

	private void  addItemSplit(){
		ArrayList<TimeArea> areas = ((MainActivity)getActivity()).getDay().getAreas();			
		
		for(int i=0; i<ITEM_COUNT; i++ ){
			String jobName = empty_job_name;
			JobItem item = new JobItem();
			for(TimeArea area : areas){
				if(area.isIn(i))
				{
					jobName = area.getJobs().get(0).getTitle();
					item = new JobItem();
					item.setColor(jobItems[area.getBegin().get()].getColor());
					item.setState(jobItems[area.getBegin().get()].getState());
					break;
				}
			}
			
			
			if( jobItems[i] == null){					
				jobItems[i] = item;
			}else{
				item = jobItems[i];
				jobName = item.getJobName();
			}
			item.setJobName(jobName);
			item.createContentView(ITEM_SIZE);
			jobItems[i].show();	
			
		}
	}

	
	private void addClockView(int i){
		TextView text = new TextView(getActivity());
		text.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, ITEM_SIZE));
		text.setTextSize(TEXT_SIZE);
		text.setPadding(4, 12, 2, 12);
		text.setText(new Clock(i).toString());
		text.setGravity(Gravity.CENTER_VERTICAL);
		clocksPanel.addView(text);		
		TextView line = new TextView(getActivity());
		line.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 1));
		line.setBackgroundColor(0xffa0a0a0);
		clocksPanel.addView(line);
	}

	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		if( resultCode != Activity.RESULT_OK) return;
		if(requestCode == REQUEST_JOB_NAME){	
			String jobName = data.getStringExtra(EditJobDialog.EXTRA_JOB_NAME);
			String jobType = data.getStringExtra(EditJobDialog.EXTRA_JOB_TYPE);
			for(int i=0; i<ITEM_COUNT; ++i){
				if( !jobName.isEmpty() ){
						jobItems[i].appyJobName(jobName);
						jobItems[i].setJobType(jobType);
				}
			}
		}
	}
	
	Day BuildDay(){
		Day day = new Day( ((MainActivity)getActivity()).getDate() );
		if(jobItems.length==0)
			return day;
		
		String jobName=jobItems[0].getJobName();
		int startClock=0, endClock=0;
		for(int i=0; i<ITEM_COUNT; i++){
			JobItem item = jobItems[i];
			if( !item.getJobName().equals( jobName) ){	
				endClock = i;		
				if( !jobName.equals(empty_job_name)){
					TimeArea area = new TimeArea(startClock, endClock);
					Job job = new Job(jobName);
					job.setType(item.getJobType());
					area.attachJob(job);
					day.addTimeArea(area);
				}
				startClock = endClock;
				jobName = item.getJobName();
				
			}
		}
		
		if( !jobName.equals(empty_job_name)){
			TimeArea area = new TimeArea(startClock, ITEM_COUNT);
			area.attachJob(new Job(jobName));
			day.addTimeArea(area);
		}
		
		return day;
	}
}
