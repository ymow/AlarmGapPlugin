package com.simasteam.phonegap.plugin.alarmgap;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmGapPlugin extends CordovaPlugin {

	public static final String TAG = "AlarmGap";
	
	public static final String ACTION_SAVE_ALARM = "saveAlarm";
	public static final String ACTION_DELETE_ALARM = "deleteAlarm";
	public static final String ACTION_UPDATE_ALARM = "updateAlarm";
	
	// TODO define it:
	public static final String HTML_DEFAULT = "";
	
	private Activity activity;
	private AlarmManager manager;
	private AlarmBean bean;
	
	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		
		try {
			init( args );
			
			if ( ACTION_SAVE_ALARM.equals( action ) ) {
				saveAlarm( args, callbackContext );
			} else if ( ACTION_DELETE_ALARM.equals( action ) ) {
				//deleteAlarm( args, callbackContext );
			} else {
				callbackContext.error("Invalid action");
				return false;
			}
			
			return true;
			
		} catch( Exception e ) {

			Log.e( AlarmGapPlugin.TAG , "AlarmGapPlugin.execute", e );
			
            callbackContext.error( e.getMessage() );
			return false;
		}
		
	}

	private void init( JSONArray args ) {
		activity = this.cordova.getActivity();
		manager = (AlarmManager) activity.getSystemService( Context.ALARM_SERVICE );
		bean = AlarmBean.parse( args );
	}
	
	private PendingIntent createPendingIntent( JSONArray args ) {
		
		Intent intent = new Intent( activity, ShowAlarmActivity.class );
		
		intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
		intent.putExtra( ShowAlarmActivity.EXTRA_ALARM, bean );
		
		return PendingIntent.getActivity( activity, (int) bean.alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT );
	}
	
	private void saveAlarm(JSONArray args, CallbackContext callbackContext) {
		
		PendingIntent pIntent = createPendingIntent( args );
		
		manager.set( AlarmManager.RTC_WAKEUP , bean.timeInMillis, pIntent );
		
	}

}
