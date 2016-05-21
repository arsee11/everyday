package org.arsee.everyday;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class MyBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub		
		Log.d("MyBroadcastReceiver", "onReceive("+intent.getAction()+")");
		if(intent.getAction().equals("org.arsee.everyday.alarm"))
		{
			//new ServiceGuard(context).keepRunning(TickService.class);
		}else if(intent.getAction().equals("android.intent.action.USER_PRESENT")){
			Intent mFxLockIntent = new Intent(context, LockActivity.class);
			mFxLockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(mFxLockIntent);
		}
	}

}
