package com.simasteam.phonegap.plugin.alarmgap;

import java.io.Serializable;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import android.util.Log;

@DatabaseTable(tableName="alarm")
public class AlarmBean implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String FIELD_ALARM_ID = "alarmId";
	
	@DatabaseField(generatedId = true)
	private Long id;
	
	@DatabaseField(canBeNull = false, unique = true, columnName = FIELD_ALARM_ID)
	private Long alarmId;
	
	@DatabaseField(canBeNull = false)
	private Long timeInMillis;
	
	@DatabaseField(canBeNull = true)
	private String jsonObject;
	
	@DatabaseField(canBeNull = true)
	private String htmlPath;
	
	@DatabaseField
	private boolean vibrate = true;
	
	@DatabaseField(canBeNull = false, foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true )
	private NotificationBean notification;
	
	public static AlarmBean parse( JSONArray args ) {
		Log.d( AlarmGapPlugin.TAG, "args = " + args.toString() );
		
		AlarmBean ab = new AlarmBean();
		
		ab.alarmId = args.optLong( 0, 1 );
		ab.timeInMillis = args.optLong( 1 , 0 );
		
		Log.d("AlarmBean", "ab.timeInMillis = " + ab.timeInMillis );
		Log.d("AlarmBean", "Now = " + new Date() );
		Log.d("AlarmBean", "alarm time = " + new Date( ab.timeInMillis ) );
		
		ab.htmlPath = args.optString( 2, AlarmGapPlugin.HTML_DEFAULT );
		
		JSONObject jsonObj = args.optJSONObject( 3 );
		ab.jsonObject = jsonObj == null ? null : jsonObj.toString();
		
		ab.vibrate = args.optBoolean( 4, false );
		
		jsonObj = args.optJSONObject( 5 );
		ab.notification = jsonObj == null ? null : NotificationBean.parse( jsonObj );
		
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

	public String getJsonObject() {
		return jsonObject;
	}

	public void setJsonObject(String jsonObject) {
		this.jsonObject = jsonObject;
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
				+ jsonObject + ", htmlPath=" + htmlPath + ", vibrate="
				+ vibrate + ", notification=" + notification + "]";
	}

	/**
	 * 
	 * @return object or null
	 */
	public JSONObject toJson() {
		try {
			JSONObject json = new JSONObject();
			
			json.put( "alarmId", this.alarmId );
			json.put( "timeInMillis", this.timeInMillis );
			json.put( "vibrate", this.vibrate );
			json.put( "htmlPath", this.htmlPath );
			
			return json;
		} catch (JSONException e) {
			Log.e( AlarmGapPlugin.TAG, "AlarmBean", e );
			return null;
		}
		
	}

}
