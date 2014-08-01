package com.simasteam.phonegap.plugin.alarmgap;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="alarm")
public class AlarmBean implements Serializable {

	public static final String FIELD_ALARM_ID = "alarmId";
	
	private static final String FIELD_EXTRA = "extra";
	private static final String FIELD_NOTIFICATION = "notification";
	private static final String FIELD_HTML_PATH = "htmlPath";
	private static final String FIELD_VIBRATE = "vibrate";
	private static final String FIELD_TIME_IN_MILLIS = "timeInMillis";

	private static final long serialVersionUID = 1L;
	
	@DatabaseField(generatedId = true)
	private Long id;
	
	@DatabaseField(canBeNull = false, unique = true, columnName = FIELD_ALARM_ID)
	private Long alarmId;
	
	@DatabaseField(canBeNull = false)
	private Long timeInMillis;
	
	@DatabaseField(canBeNull = true)
	private String extra;
	
	@DatabaseField(canBeNull = true)
	private String htmlPath;
	
	@DatabaseField
	private boolean vibrate = true;
	
	@DatabaseField(canBeNull = false, foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true )
	private NotificationBean notification;
	
	public static AlarmBean parse( JSONObject jo ) {
		AlarmBean ab = new AlarmBean();
		
		ab.alarmId = jo.optLong( FIELD_ALARM_ID, 1 );
		ab.timeInMillis = jo.optLong( FIELD_TIME_IN_MILLIS , 0 );
		ab.htmlPath = jo.optString( FIELD_HTML_PATH, AlarmGapPlugin.HTML_DEFAULT );
		ab.vibrate = jo.optBoolean( FIELD_VIBRATE, false );

		JSONObject jsonObj = jo.optJSONObject( FIELD_NOTIFICATION );
		ab.notification = jsonObj == null ? null : NotificationBean.parse( jsonObj );
		
		jsonObj = jo.optJSONObject( FIELD_EXTRA );
		ab.extra = jsonObj == null ? null : jsonObj.toString();
		
		return ab;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAlarmId() {
		return alarmId;
	}

	public void setAlarmId(Long alarmId) {
		this.alarmId = alarmId;
	}

	public Long getTimeInMillis() {
		return timeInMillis;
	}

	public void setTimeInMillis(Long timeInMillis) {
		this.timeInMillis = timeInMillis;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String jsonObject) {
		this.extra = jsonObject;
	}

	public String getHtmlPath() {
		return htmlPath;
	}

	public void setHtmlPath(String htmlPath) {
		this.htmlPath = htmlPath;
	}

	public boolean isVibrateEnabled() {
		return vibrate;
	}

	public void setVibrate(boolean enabled) {
		this.vibrate = enabled;
	}

	public NotificationBean getNotification() {
		return notification;
	}

	public void setNotification(NotificationBean notification) {
		this.notification = notification;
	}

	@Override
	public String toString() {
		return "AlarmBean [id=" + id + ", alarmId=" + alarmId
				+ ", timeInMillis=" + timeInMillis + ", jsonObject="
				+ extra + ", htmlPath=" + htmlPath + ", vibrate="
				+ vibrate + ", notification=" + notification + "]";
	}

	/**
	 * 
	 * @return object or null
	 */
	public JSONObject toJson() {
		try {
			JSONObject json = new JSONObject();
			
			json.put( FIELD_ALARM_ID, this.alarmId );
			json.put( FIELD_TIME_IN_MILLIS, this.timeInMillis );
			json.put( FIELD_VIBRATE, this.vibrate );
			json.put( FIELD_HTML_PATH, this.htmlPath );
			json.put( FIELD_NOTIFICATION , notification.toJSON() );
			
			JSONObject jsonExtra = null;
			if ( extra != null ) {
				jsonExtra = new JSONObject( extra );
			}
			json.put( FIELD_EXTRA , jsonExtra );
			
			
			return json;
		} catch (JSONException e) {
			Log.e( AlarmGapPlugin.TAG, "AlarmBean", e );
			return null;
		}
		
	}

}
