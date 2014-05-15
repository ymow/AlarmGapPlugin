package com.simasteam.phonegap.plugin.alarmgap;

import java.util.Date;

import org.apache.cordova.CordovaActivity;
import org.apache.cordova.CordovaWebViewClient;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

public class ShowAlarmActivity extends CordovaActivity {
	
	public static final String EXTRA_ALARM = "EXTRA_ALARM";
	
	private AlarmBean bean;

	private AlarmBO alarmBO;
	
	private String loadedUrl;
	
	private AlarmReceiver receiver = new AlarmReceiver() {
		
		@Override
		public void onStopReceived( long alarmId ) {
			if ( alarmId == bean.getAlarmId() ) {
				alarmBO.stopAlarm( bean );
				finish();
			}
		}
		
		@Override
		public void onSnoozeReceived( long alarmId, long snoozeTimeInMillis ) {
			if ( alarmId == bean.getAlarmId() ) {
				alarmBO.snoozeAlarm( bean, snoozeTimeInMillis );
				finish();
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initScreen();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		
		initScreen();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		this.unregisterReceiver( receiver );
	}

	private void initScreen() {
		Intent intent = getIntent();
		bean = (AlarmBean) intent.getSerializableExtra( EXTRA_ALARM );
		
		alarmBO = AlarmBO.getInstance( this );
		AlarmReceiver.register( this, receiver );
		
		if ( bean == null ) {
			return;
		}
			
		String url = AlarmBO.getAlarmUrl( bean );
		
		if ( url.equals( loadedUrl ) ) {
			attachAlarmBean();
			refreshAlarm();
			return;
		}
		
		
		this.loadUrl( url );
		loadedUrl = url;
		
		this.appView.setWebViewClient( new CordovaWebViewClient( this, this.appView ) {
			@Override
			public void onPageFinished(WebView webView, String url) {
				super.onPageFinished(webView, url);
				
				attachAlarmBean();
				
				new Thread( new Runnable() {
					@Override
					public void run() {
						refreshAlarm();
					}
				}).start();
			}
		});
	}
	
	private void refreshAlarm() {
		long now = new Date().getTime();
		
		if ( now >= bean.getTimeInMillis() ) {
			alarmBO.startAlarm( bean );
			alarmBO.deleteAlarm( bean );
		}
	}
	
	private void attachAlarmBean() {
		// FIXME sendjavascript not working... am I using it right?
		//this.sendJavascript( "console.log( 'test sendjavascript' );" );
		this.loadUrl("javascript:console.log( 'test loadUrl' )");
		
		JSONObject alarmJson = bean.toJson();
		String stringAlarm = alarmJson.toString().replaceAll( "\\'" , "\\\\'");
		
		StringBuffer sb = new StringBuffer();
		sb.append( "javascript:alarmGapReceiveAlarm( " );
			sb.append( " '" + stringAlarm + "' ");
		sb.append( " );" );
		
		Log.d( AlarmGapPlugin.TAG, "sb.toString() = " + sb.toString() );
		
		this.loadUrl( sb.toString() );
		//this.sendJavascript( sb.toString() );
	}

}
