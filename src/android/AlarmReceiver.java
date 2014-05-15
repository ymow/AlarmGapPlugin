package com.simasteam.phonegap.plugin.alarmgap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

public abstract class AlarmReceiver extends BroadcastReceiver {
	
	private static final String ACTION_STOP		= ".alarmgap.AlarmReceiver.ACTION_STOP";
	private static final String ACTION_SNOOZE 	= ".alarmgap.AlarmReceiver.ACTION_SNOOZE";
	
	private static final String EXTRA_ALARM_ID 		= "EXTRA_ALARM_ID";
	private static final String EXTRA_SNOOZE_MILLIS = "EXTRA_SNOOZE_MILLIS";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		
		if ( getApplicationAction( context, ACTION_STOP ).equals( action ) ) {
			
			long alarmId = intent.getLongExtra( EXTRA_ALARM_ID, -1 );
			
			onStopReceived( alarmId );
			
		} else if ( getApplicationAction( context, ACTION_SNOOZE ).equals( action ) ) {
			
			long snoozeTimeInMillis = intent.getLongExtra( EXTRA_SNOOZE_MILLIS, -1 );
			long alarmId = intent.getLongExtra( EXTRA_ALARM_ID, -1 );

			onSnoozeReceived( alarmId, snoozeTimeInMillis );
			
		}
		
	}
	
	public abstract void onStopReceived( long alarmId );
	
	public abstract void onSnoozeReceived(  long alarmId, long snoozeTimeInMillis );
	
	/**
	 * Using package name in action. So you don't receive broadcasts from other applications using this plugin
	 * @param context
	 * @param partialAction
	 * @return full action name
	 */
	public static String getApplicationAction( Context context, String partialAction ) {
		return context.getPackageName() + partialAction;
	}
	
	public static void register( Context context, AlarmReceiver receiver ) {
		IntentFilter intentFilter = new IntentFilter();
		
		intentFilter.addAction(  getApplicationAction( context, ACTION_SNOOZE ) );
		intentFilter.addAction( getApplicationAction( context, ACTION_STOP ) );
		
		context.registerReceiver( receiver, intentFilter );
	}
	
	public static void sendStop( Context context, long alarmId ) {
		
		Log.d("AlarmReceiver", "sendStop alarmId = " + alarmId );
		
		Intent intent = new Intent( getApplicationAction( context, ACTION_STOP ) );
		intent.putExtra( EXTRA_ALARM_ID , alarmId );
		
		context.sendBroadcast( intent );
	}
	
	public static void sendSnooze( Context context, long alarmId, Long snoozeTimeInMillis ) {
		Log.d("AlarmReceiver", "sendSnooze alarmId = " + alarmId + ", time = " + snoozeTimeInMillis );
		
		Intent intent = new Intent( getApplicationAction( context, ACTION_SNOOZE ) );
		intent.putExtra( EXTRA_SNOOZE_MILLIS , snoozeTimeInMillis );
		intent.putExtra( EXTRA_ALARM_ID , alarmId );
		
		context.sendBroadcast( intent );
	}

}
