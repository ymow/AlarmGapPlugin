package com.simasteam.phonegap.plugin.alarmgap;

import java.util.Date;

import org.apache.cordova.CordovaActivity;
import org.apache.cordova.CordovaWebViewClient;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;

public class ShowAlarmActivity extends CordovaActivity {
	
	public static final String EXTRA_ALARM = "EXTRA_ALARM";
	
	private AlarmBean bean;

	private AlarmBO alarmBO;
	
	private AlarmReceiver receiver = new AlarmReceiver() {
		
		@Override
		public void onStopReceived( long alarmId ) {
			Log.d( AlarmGapPlugin.TAG, "onStopReceveid = " + alarmId );
			
			if ( alarmId == bean.getAlarmId() ) {
				
				alarmBO.stopAlarm( bean );
				finish();
			}
			
		}
		
		@Override
		public void onSnoozeReceived( long alarmId, long snoozeTimeInMillis ) {
			Log.d( AlarmGapPlugin.TAG, "onSnoozeReceived = " + alarmId + ", " + snoozeTimeInMillis );
			
			if ( alarmId == bean.getAlarmId() ) {
				alarmBO.snoozeAlarm( bean, snoozeTimeInMillis );
				finish();
			}
			
		}
	};

	private String loadedUrl;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.d( AlarmGapPlugin.TAG , "ShowAlarmActivity.onCreate" );
		
		initScreen();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		
		Log.d( AlarmGapPlugin.TAG , "ShowAlarmActivity.onNewIntent" );
		
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
		
		Log.d( AlarmGapPlugin.TAG , "alarmBean = " + bean );
		
		if ( bean != null ) {
			
			String htmlPath = bean.getHtmlPath();
			if ( TextUtils.isEmpty( htmlPath ) ) {
				htmlPath =  AlarmGapPlugin.HTML_DEFAULT;
			}
			
			
			String url = "file:///android_asset/www/" + htmlPath;
			
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
	}
	
	private void refreshAlarm() {
		long now = new Date().getTime();
		if ( now >= bean.getTimeInMillis() ) {
			alarmBO.startAlarm( bean );
			
			alarmBO.deleteAlarm( bean );
		}
	}
	
	private void attachAlarmBean() {
		Log.d( AlarmGapPlugin.TAG, "attachAlarmBean" );
		
		// FIXME sendjavascript not working... am I using it right?
		this.sendJavascript( "console.log( 'test sendjavascript' );" );
		this.loadUrl("javascript:console.log( 'test loadUrl' )");
		
		JSONObject alarmJson = bean.toJson();
				
		StringBuffer sb = new StringBuffer();
		
		String jsonObject = bean.getJsonObject().replaceAll( "\\'" , "\\\\'");
		
		sb.append( "javascript:alarmGapReceiveAlarm( " );
			sb.append( " '" + alarmJson.toString() + "' ");
			sb.append( ", '" + jsonObject + "' ");
		sb.append( " );" );
		
		Log.d( AlarmGapPlugin.TAG, "sb.toString() = " + sb.toString() );
		
		this.loadUrl( sb.toString() );
		//this.sendJavascript( sb.toString() );
	}

}
