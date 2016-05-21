package org.arsee.everyday;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class ServiceGuard {
	private Context m_context;
	
	public ServiceGuard(Context c){
		m_context = c;
	}
	public void keepRunning(Class<?> service){
		if( !isRunning(service) ){
			startService(service);
		}
	}
	
	private boolean isRunning(Class<?> service){
		boolean is = false;
	     ActivityManager am = (ActivityManager)m_context.getSystemService(Context.ACTIVITY_SERVICE);
	     List<RunningServiceInfo> list = am.getRunningServices(300);
	     for(RunningServiceInfo info : list){
	         //Log.d("ServiceRunning", info.service.getClassName());
	         if( info.service.getClassName().equals(service.getName()) ){
	        	 is = true;
	        	 break;
	         }	       
	    }	
	     return is;
	}
	
	private void startService(Class<?> service){
		Intent i = new Intent(m_context, service);
		m_context.startService(i);
	}
}
