package org.arsee.everyday;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.widget.GridView;
import android.widget.ListView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.SimpleAdapter;

public class ReportDetailDialog extends DialogFragment {
	private View v;
	private String jobType;
	
	public ReportDetailDialog(String jobType){
		this.jobType = jobType;
	}
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		 v = getActivity().getLayoutInflater()
						.inflate(R.layout.dialog_report_detail, null);
				 
		 SQLiteDatabase db = ((MainActivity)this.getActivity()).getDB();
				 
		 List<Map<String, Object>> items = new ArrayList<Map<String,Object>>(); 
		 ArrayList<ReportDetail.ReportDetailItem> detail = ReportDetail.getReportDetail(true, db, Day.getToday(), jobType);
         for (ReportDetail.ReportDetailItem e : detail) { 
           Map<String, Object> item = new HashMap<String, Object>(); 
           item.put("titleItem", e.getJobTitle());
           item.put("typeItem",  e.getJobType());
           item.put("timeItem", e.getJobTime());
           items.add(item); 
         } 

       SimpleAdapter adapter = new SimpleAdapter(this.getActivity(),  
                                                     items,  
                                                     R.layout.dialog_report_detail_item,  
                                                     new String[]{"titleItem", "typeItem", "timeItem"},  
                                                     new int[]{R.id.dialog_report_detail_jobTitle, R.id.dialog_report_detail_jobType, R.id.dialog_report_detail_jobTime}); 

         ListView lv = (ListView)v.findViewById(R.id.dialog_report_detail_listView); 
         lv.setAdapter(adapter); 
         //lv.setOnItemClickListener(new MyOnItemClickListener());
	
		return new AlertDialog.Builder(getActivity())
								 .setView(v)
								 .setPositiveButton(android.R.string.ok, 
										 	new DialogInterface.OnClickListener() {
												
												@Override
												public void onClick(DialogInterface dialog, int which) {
													// TODO Auto-generated method stub
													//sendResult(Activity.RESULT_OK
													//		, title.getText().toString().trim()
													//		,stype);
												}
											}
								 )
								 .create();
	}
}
