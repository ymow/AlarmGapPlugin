package com.simasteam.phonegap.plugin.alarmgap;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao.CreateOrUpdateStatus;

public class AlarmBO {
	
	private static final long[] VIBRATE_PATTERN = { 0, 500, 1000 };

	private static final long SNOOZE_DEFAULT = 5 * 60 * 1000; // 5min ?

	private static final int NOTIFICATION_ID = -123456;

	private static final String URL_HTML_BASE = "file:///android_asset/www/";
	
	private static AlarmBO instance;
	
	private Context context;
	private DataBaseOpenHelper dataHelper;

	private MediaPlayer mediaPlayer;
	
	private AlarmBO( Context context ) {
		this.context = context;
	}
	
	public static AlarmBO getInstance(Context context) {
		if ( instance == null ) {
			instance = new AlarmBO( context );
		}
		
		return instance;
	}
	
	private void openDatabase() {
		if ( dataHelper == null ) {
			dataHelper = OpenHelperManager.getHelper( context, DataBaseOpenHelper.class );
		}
	}
	
	/**
	 * List all saved alarms
	 * @return
	 */
	public List<AlarmBean> listAll() {
		List<AlarmBean> list = null;
		openDatabase();
		try {
			list = dataHelper.getAlarmDao().queryForAll();
			
		} catch (SQLException e) {
			Log.e( AlarmGapPlugin.TAG, "listAll", e );
		}
		
		return list;
	}
	
	/**
	 * Saves an alarm in database and set it in the AlarmManager
	 * @param alarmBean
	 * @return 
	 */
	public boolean saveAlarm( AlarmBean alarmBean ) {
		boolean isSaved = false;
		
		openDatabase();
		try {
			
			AlarmBean savedAlarm = this.findByAlarmId( alarmBean.getAlarmId() );
			
			if ( savedAlarm != null ) { // update
				alarmBean.setId( savedAlarm.getId() );
				alarmBean.getNotification().setId( savedAlarm.getNotification().getId() );
				
			} else { // create
				dataHelper.getNotificationDao().create( alarmBean.getNotification() );
			}
			
			CreateOrUpdateStatus status = dataHelper.getAlarmDao().createOrUpdate( alarmBean );
			isSaved = status.isCreated() || status.isUpdated(); 
			
			if ( isSaved ) {
				setAlarm( alarmBean );
			}
			
		} catch (SQLException e) {
			Log.e( AlarmGapPlugin.TAG, "saveAlarm", e );
		}
		
		return isSaved;
	}
	
	public void deleteAlarm( AlarmBean alarmBean ) {
		openDatabase();
		
		try {
			AlarmBean savedAlarm = this.findByAlarmId( alarmBean.getAlarmId() );
			
			if ( savedAlarm != null ) {
				dataHelper.getAlarmDao().deleteById( savedAlarm.getId() );
				dataHelper.getNotificationDao().deleteById( savedAlarm.getNotification().getId() );
				
			}
			
		} catch (SQLException e) {
			Log.e( AlarmGapPlugin.TAG, "deleteAlarm", e );
		}
		
	}
	
	private AlarmBean findByAlarmId( Long alarmId ) throws SQLException {
		
		if ( alarmId == null ) {
			return null;
		}
		
		List<AlarmBean> list = dataHelper.getAlarmDao().queryForEq( AlarmBean.FIELD_ALARM_ID, alarmId );
		
		if ( list == null || list.isEmpty() ) {
			return null;
		}
		
		return list.get( 0 );
	}
	
	private Intent createIntent( AlarmBean bean ) {
		Intent intent = new Intent( context, ShowAlarmActivity.class );
		
		intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
		intent.addFlags( Intent.FLAG_ACTIVITY_SINGLE_TOP );
		
		intent.putExtra( ShowAlarmActivity.EXTRA_ALARM, bean );
		
		return intent;
	}
	
	private PendingIntent createPendingAlarm( AlarmBean bean ) {
		Intent intent = createIntent( bean );
		return PendingIntent.getActivity( context, bean.getAlarmId().intValue(), intent, PendingIntent.FLAG_UPDATE_CURRENT );
	}
	
	private PendingIntent createPendingNotify(AlarmBean bean) {
		Intent intent = createIntent( bean );
		return PendingIntent.getActivity( context, NOTIFICATION_ID , intent, PendingIntent.FLAG_UPDATE_CURRENT );
	}

	public void setAlarm(AlarmBean bean) {
		PendingIntent pIntent = createPendingAlarm( bean );
		getAlarmManager().set( AlarmManager.RTC_WAKEUP , bean.getTimeInMillis().longValue(), pIntent );
	}
	
	private void cancelAlarm(AlarmBean bean) {
		PendingIntent pIntent = createPendingAlarm( bean );
		getAlarmManager().cancel( pIntent );
	}
	
	private AlarmManager getAlarmManager() {
		return (AlarmManager) context.getSystemService( Context.ALARM_SERVICE );
	}
	
	private Vibrator getVibrator() {
		return (Vibrator) context.getSystemService( Context.VIBRATOR_SERVICE );
	}
	
	private NotificationManager getNotificationManager() {
		return (NotificationManager) context.getSystemService( Context.NOTIFICATION_SERVICE );
	}
	
	private void playAlarmSound() {
		if ( mediaPlayer != null ) {
			return;
		}
		
		try {
			Uri uriAlarm = RingtoneManager.getDefaultUri( RingtoneManager.TYPE_ALARM );
			
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setDataSource( context, uriAlarm );
			
			AudioManager audioManager = (AudioManager) context.getSystemService( Context.AUDIO_SERVICE );
			
		    float streamVolumeCurrent = audioManager.getStreamVolume( AudioManager.STREAM_ALARM );
		    float streamVolumeMax = audioManager.getStreamMaxVolume( AudioManager.STREAM_ALARM );
		    float volume = streamVolumeCurrent / streamVolumeMax;
			
		    mediaPlayer.setVolume( volume, volume );
			mediaPlayer.setAudioStreamType( AudioManager.STREAM_ALARM );
			mediaPlayer.setLooping( true );
			mediaPlayer.setOnPreparedListener( new OnPreparedListener() {
				
				@Override
				public void onPrepared(MediaPlayer mp) {
					mp.start();
				}
			});
			mediaPlayer.prepare();
			
		} catch (Exception e) {
			Log.e( AlarmGapPlugin.TAG, "playAlarmSound", e );
		}
	}
	
	private void stopAlarmSound() {
		if ( mediaPlayer != null ) {
			mediaPlayer.stop();
			mediaPlayer = null;
		}
	}
	
	public void startAlarm( AlarmBean bean ) {
		showNotification( bean );
		
		startVibrating( bean );
			
		playAlarmSound();
	}
	
	private void startVibrating( AlarmBean bean ) {
		if ( bean.isVibrateEnabled() ) {
			getVibrator().vibrate( VIBRATE_PATTERN, 0 );
		}
	}
	
	private void stopVibrating( AlarmBean bean ) {
		if ( bean.isVibrateEnabled() ) {
			getVibrator().cancel();
		}
	}
	
	private void showNotification( AlarmBean bean ) {
		
		NotificationBean nb = bean.getNotification();
		
		Bitmap bitmap = loadLargeIconBitmap( nb );
		
		PendingIntent pIntent = createPendingNotify( bean );
		
		Notification notification = new NotificationCompat.Builder( context )
				.setContentTitle( nb.getTitle() )
				.setContentText( nb.getMessage() )
				.setLargeIcon( bitmap )
				.setSmallIcon( getSmallIcon( nb ) )
				.setContentIntent( pIntent )
				.setAutoCancel( false )
			.build();
		
		getNotificationManager().notify( bean.getAlarmId().intValue(), notification );
	}
	
	/**
	 * Gets the Resource Id in the drawable folder by name
	 * @param nb the notification bean
	 * @return the resId
	 */
	private int getSmallIcon(NotificationBean nb) {
		
		String smallIconName = nb.getSmallIconName();
		if ( TextUtils.isEmpty( smallIconName ) ) {
			smallIconName = NotificationBean.SMALL_ICON_NAME_DEFAULT;
		}
		
		String packageName = context.getPackageName();
		
		try {
			Class<?> classDrawable = Class.forName( packageName + ".R$drawable" );
			
			return classDrawable.getField( smallIconName ).getInt( null );
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		
	}

	private Bitmap loadLargeIconBitmap(NotificationBean nb) {
		
		String largeIconPath = nb.getLargeIconPath();
		
		if ( TextUtils.isEmpty( largeIconPath ) ) {
			largeIconPath = NotificationBean.LARGE_ICON_PATH_DEFAULT;
		}
		
		InputStream inputStream = null;
		
		try {
			inputStream = context.getAssets().open( "www/" + largeIconPath );
		} catch (IOException e) {
			Log.e( AlarmGapPlugin.TAG, "loadLargeIconBitmap", e );
		}
		
		return BitmapFactory.decodeStream( inputStream );
		
	}

	private void hideNotification( AlarmBean bean ) {
		getNotificationManager().cancel( bean.getAlarmId().intValue() );
	}

	public void stopAlarm( AlarmBean bean ) {
		deleteAlarm( bean );
		cancelAlarm( bean );
		
		hideNotification( bean );
		
		stopAlarmSound();
		stopVibrating( bean );
	}
	
	public void snoozeAlarm( AlarmBean bean, long timeInMillis ) {
		stopAlarmSound();
		stopVibrating( bean );
		
		if ( bean == null ) {
			return;
		}
		
		if ( timeInMillis <= 0 ) {
			timeInMillis = SNOOZE_DEFAULT;
		}
		
		timeInMillis += new Date().getTime();
		bean.setTimeInMillis( timeInMillis );
		
		showNotification( bean );
		
		this.saveAlarm( bean );
	}
	
	public static String getAlarmUrl( AlarmBean bean ) {
		String htmlPath = bean.getHtmlPath();
		if ( TextUtils.isEmpty( htmlPath ) ) {
			htmlPath =  AlarmGapPlugin.HTML_DEFAULT;
		}
		
		
		return URL_HTML_BASE + htmlPath;
	}
	
}
