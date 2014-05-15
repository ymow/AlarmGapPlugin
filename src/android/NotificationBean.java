package com.simasteam.phonegap.plugin.alarmgap;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable( tableName="notification" )
public class NotificationBean implements Serializable {

	private static final String FIELD_MESSAGE 	= "message";
	private static final String FIELD_TITLE 	= "title";
	private static final String FIELD_SMALL_ICON_NAME = "smallIconPath";
	private static final String FIELD_LARGE_ICON_PATH = "largeIconName";

	private static final long serialVersionUID = 1L;
	
	public static final String TITLE_DEFAULT 	= "AlarmGap";
	public static final String MESSAGE_DEFAULT 	= "It's time!";
	public static final String LARGE_ICON_PATH_DEFAULT 	= "alarmgap/alarmgap_large_icon.png";
	public static final String SMALL_ICON_NAME_DEFAULT 	= "alarmgap_notification";
	
	@DatabaseField(generatedId = true)
	private Long id;
	
	@DatabaseField(canBeNull = false)
	private String title;
	
	@DatabaseField(canBeNull = false)
	private String message;
	
	@DatabaseField(canBeNull = true)
	private String largeIconPath;
	
	@DatabaseField(canBeNull = true)
	private String smallIconName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getLargeIconPath() {
		return largeIconPath;
	}

	public void setLargeIconPath(String iconLargePath) {
		this.largeIconPath = iconLargePath;
	}

	public String getSmallIconName() {
		return smallIconName;
	}

	public void setSmallIconName(String iconSmallName) {
		this.smallIconName = iconSmallName;
	}

	@Override
	public String toString() {
		return "NotificationBean [id=" + id + ", title=" + title + ", message="
				+ message + ", largeIconPath=" + largeIconPath
				+ ", smallIconName=" + smallIconName + "]";
	}

	public static NotificationBean parse( JSONObject jo ) {
		NotificationBean nb = new NotificationBean();
		
		nb.title = jo.optString( FIELD_TITLE, TITLE_DEFAULT );
		nb.message = jo.optString( FIELD_MESSAGE, MESSAGE_DEFAULT );
		nb.smallIconName = jo.optString( FIELD_SMALL_ICON_NAME, null );
		nb.largeIconPath = jo.optString( FIELD_LARGE_ICON_PATH, null );
		
		return nb;
	}
	
	public JSONObject toJSON() {

		try {
			JSONObject jo = new JSONObject();
			
			jo.put( FIELD_TITLE, title );
			jo.put( FIELD_MESSAGE, message );
			jo.put( FIELD_SMALL_ICON_NAME, smallIconName );
			jo.put( FIELD_LARGE_ICON_PATH, largeIconPath );
			
			return jo;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
}
