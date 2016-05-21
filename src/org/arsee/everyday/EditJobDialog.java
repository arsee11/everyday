package org.arsee.everyday;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

public class EditJobDialog extends DialogFragment {
	static final String EXTRA_JOB_NAME = "org.arsee.everyday.extra_job_name";
	static final String EXTRA_JOB_TYPE = "org.arsee.everyday.extra_job_type";
	
	View v;
	ArrayList<JobLearning.JobLearningEnty> jobLearningEnties;
	DbManager m_dbManager ;
	private void sendResult(int resultCode, String jobName, String jobType){
			if(getTargetFragment()==null){
				return ;
			}
			Intent i = new Intent();
			i.putExtra(EXTRA_JOB_NAME,  jobName);
			i.putExtra(EXTRA_JOB_TYPE, jobType);
			getTargetFragment().onActivityResult(this.getTargetRequestCode(), resultCode, i);
	}

	class MyOnItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,	long id) {
			// TODO Auto-generated method stub
			Log.d("MyOnItemClickListener.onItemClick", "position="+position);
			 
			 JobLearning.JobLearningEnty enty = jobLearningEnties.get(position);
			 EditText titleText = (EditText)v.findViewById(R.id.view_job_title);
			 titleText.setText(enty.getKey());
			 Spinner spin = (Spinner)v.findViewById(R.id.spinner_job_type);
			 int spinPosition = -1;
			for(  int i=0; i< JobType.Types.size(); i++ ){
				if(JobType.Types.get(i).equals( enty.getType())){
					spinPosition = i;
				}
			}
			if(spinPosition != -1)
				spin.setSelection(spinPosition);
		}
		
	}
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		 v = getActivity().getLayoutInflater()
						.inflate(R.layout.view_job, null);
		 EditText titleText = (EditText)v.findViewById(R.id.view_job_title);
		 titleText.requestFocus();
		 Spinner spin = (Spinner)v.findViewById(R.id.spinner_job_type);
		 
		 m_dbManager = new DbManager(this.getActivity());
		 
		 ArrayAdapter<String> aa = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, JobType.Types);  
		 aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		 spin.setAdapter(aa);
		 
		 jobLearningEnties = JobLearning.getInstance().getEntyList(m_dbManager.getDB());
		 List<Map<String, Object>> items = new ArrayList<Map<String,Object>>(); 
         for (JobLearning.JobLearningEnty e : jobLearningEnties) { 
           Map<String, Object> item = new HashMap<String, Object>(); 
           item.put("imageItem", R.drawable.job);//添加图像资源的ID   
           item.put("textItem",  e.getKey());//按序号添加ItemText   
           items.add(item); 
         } 

       SimpleAdapter adapter = new SimpleAdapter(this.getActivity(),  
                                                     items,  
                                                     R.layout.job_title_image_item,  
                                                     new String[]{"imageItem", "textItem"},  
                                                     new int[]{R.id.job_title_image_item_image, R.id.job_title_image_item_text}); 

         GridView gv = (GridView)v.findViewById(R.id.view_job_list); 
         gv.setAdapter(adapter); 
         gv.setOnItemClickListener(new MyOnItemClickListener());
	
		return new AlertDialog.Builder(getActivity())
								 .setView(v)
								 .setPositiveButton(android.R.string.ok, 
										 	new DialogInterface.OnClickListener() {
												
												@Override
												public void onClick(DialogInterface dialog, int which) {
													// TODO Auto-generated method stub
													//View v = EditJobDialog.this.getView();
													EditText title = (EditText)v.findViewById(R.id.view_job_title);
													//EditText type = (EditText)v.findViewById(R.id.view_job_type);
													Spinner spinType = (Spinner)v.findViewById(R.id.spinner_job_type);
													String stype =   spinType.getSelectedItem().toString();
													Log.d("d",stype);
													sendResult(Activity.RESULT_OK
															, title.getText().toString().trim()
															,stype);
												}
											}
								 )
								 .create();
	}
	
}
