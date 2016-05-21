package org.arsee.everyday;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

public class CalendarActivity extends Activity {
	public final static String EXTRA_DATE  = "org.arsee.everday.date";
	private String date;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.activity_calendar);
		
		Button btnOK = (Button)findViewById(R.id.calendar_ok_button);
		btnOK.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				i.putExtra(EXTRA_DATE,  CalendarActivity.this.date);
				setResult(RESULT_OK, i);
				finish();				
			}
		});
		
		Button btnCancel = (Button)findViewById(R.id.calendar_cancel_button);
		btnCancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
					setResult(RESULT_CANCELED);
					finish();
			}
		});
		
		CalendarView calendar = (CalendarView)findViewById(R.id.calendarView);
		date = Day.FormatDay(calendar.getDate());
		calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                    int dayOfMonth) {
                // TODO Auto-generated method stub
                 date = year + "-" + (month+1) + "-" + dayOfMonth ;
            }
        });
	}

}
