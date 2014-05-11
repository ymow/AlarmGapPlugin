package com.simasteam.phonegap.plugin.alarmgap;

import org.apache.cordova.CordovaActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class ShowAlarmActivity extends CordovaActivity {
	
	public static final String EXTRA_ALARM = "EXTRA_ALARM";
	
	private AlarmBean alarmBean;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.d( AlarmGapPlugin.TAG , "ShowAlarmActivity.onCreate" );
		
		Intent intent = getIntent();
		alarmBean = (AlarmBean) intent.getSerializableExtra( EXTRA_ALARM );
		
		Log.d( AlarmGapPlugin.TAG , "alarmBean = " + alarmBean );
		
		loadUrl( "file:///android_asset/www/" + alarmBean.htmlPath );
		// TODO vibrate
		// TODO sound
	}

}
