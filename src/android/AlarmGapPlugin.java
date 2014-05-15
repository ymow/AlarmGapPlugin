package com.simasteam.phonegap.plugin.alarmgap;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.apache.cordova.PluginResult.Status;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.util.Log;

public class AlarmGapPlugin extends CordovaPlugin {

	public static final String TAG = "AlarmGap";
	
	public static final String ACTION_SAVE_ALARM 	= "saveAlarm";
	public static final String ACTION_DELETE_ALARM 	= "deleteAlarm";
	public static final String ACTION_STOP_ALARM 	= "stopAlarm";
	public static final String ACTION_SNOOZE_ALARM 	= "snoozeAlarm";

	public static final String HTML_DEFAULT = "alarmgap/alarmpage.html";
	
	private Activity activity;

	private AlarmBO alarmBO;

	private AlarmBean bean;
	private long snoozeTimeInMillis;
	private long alarmId;
	
	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		
		try {
			init( action, args );
			
			if ( ACTION_SAVE_ALARM.equals( action ) ) {
				saveAlarm( callbackContext );
				
			} else if ( ACTION_DELETE_ALARM.equals( action ) ) {
				alarmBO.deleteAlarm( bean );
				
			} else if ( ACTION_STOP_ALARM.equals( action ) ) {
				AlarmReceiver.sendStop( activity, alarmId );
				
			} else if ( ACTION_SNOOZE_ALARM.equals( action ) ) {
				AlarmReceiver.sendSnooze( activity, alarmId, snoozeTimeInMillis );
				
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

	private void init( String action, JSONArray args ) {
		activity = this.cordova.getActivity();
		alarmBO = AlarmBO.getInstance( activity );
		
		if ( ACTION_SNOOZE_ALARM.equals( action ) ) {
			alarmId = args.optLong( 0 );
			snoozeTimeInMillis = args.optLong( 1 );
			
		} else if ( ACTION_STOP_ALARM.equals( action ) ) {
			alarmId = args.optLong( 0 );

		} else {
			bean = AlarmBean.parse( args );
		}
		
	}

	private void saveAlarm( CallbackContext callbackContext) {
		boolean result = alarmBO.saveAlarm( bean );
		
		PluginResult pluginResult = new PluginResult( Status.OK, result );
		callbackContext.sendPluginResult( pluginResult );
	}

}
