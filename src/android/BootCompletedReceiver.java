package com.simasteam.phonegap.plugin.alarmgap;

import java.util.Date;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootCompletedReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		
		String action = intent.getAction();
		
		if ( Intent.ACTION_BOOT_COMPLETED.equals( action ) ) {
			recoverAlarms( context );
		}

	}

	private void recoverAlarms(Context context) {
		AlarmBO alarmBO = AlarmBO.getInstance( context );
		
		List<AlarmBean> list = alarmBO.listAll();
		
		if ( list == null || list.size() == 0 ) {
			return;
		}
		
		long timeNow = new Date().getTime();
		
		for (AlarmBean alarmBean : list) {
			
			if ( timeNow >= alarmBean.getTimeInMillis() ) {
				alarmBO.deleteAlarm( alarmBean );
			} else {
				alarmBO.setAlarm( alarmBean );
			}
			
		}
		
	}

}
