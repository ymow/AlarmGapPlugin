package com.simasteam.phonegap.plugin.alarmgap;

import java.io.Serializable;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

public class AlarmBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public long alarmId;
	public long timeInMillis;
	public String jsonObject;
	public String htmlPath;
	
	public static AlarmBean parse( JSONArray args ) {
		Log.d( AlarmGapPlugin.TAG, "args = " + args.toString() );
		
		AlarmBean ab = new AlarmBean();
		
		ab.alarmId = args.optLong( 0, -1 );
		ab.timeInMillis = args.optLong( 1 , -1 );
		ab.htmlPath = args.optString( 2, AlarmGapPlugin.HTML_DEFAULT );
		
		JSONObject jsonObj = args.optJSONObject( 3 );
		ab.jsonObject = jsonObj == null ? null : jsonObj.toString();
		
		return ab;
	}

	@Override
	public String toString() {
		return "AlarmBean [alarmId=" + alarmId + ", timeInMillis="
				+ timeInMillis + ", jsonObject=" + jsonObject + ", htmlPath="
				+ htmlPath + "]";
	}

}
