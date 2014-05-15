var AlarmGap = function() {
};

AlarmGap.prototype.saveAlarm = function( alarmId, timeInMillis, vibrate, htmlPath, jsonObject, notifyJsonOjbject,
	successCallback, errorCallback ) {

	vibrate 		= typeof vibrate !== 'undefined' ? vibrate : false;
	successCallback = typeof successCallback !== 'undefined' ? successCallback : function(){};
	errorCallback 	= typeof errorCallback !== 'undefined' ? errorCallback : function(){};
 
 	cordova.exec(
		successCallback,
 		errorCallback,
 		"AlarmGap",
 		"saveAlarm",
 		[
 			alarmId,
 			timeInMillis,
 			htmlPath,
 			jsonObject,
 			vibrate,
 			notifyJsonOjbject
 		]
	);
};

AlarmGap.prototype.deleteAlarm = function( alarmId, successCallback, errorCallback  ) {
 
 	successCallback = typeof successCallback !== 'undefined' ? successCallback : function(){};
	errorCallback 	= typeof errorCallback !== 'undefined' ? errorCallback : function(){};
 
 	cordova.exec(
		successCallback,
 		errorCallback,
 		"AlarmGap",
 		"deleteAlarm",
 		[
 			alarmId
 		]
	);
};

AlarmGap.prototype.stopAlarm = function( alarmId, successCallback, errorCallback  ) {
 
 	successCallback = typeof successCallback !== 'undefined' ? successCallback : function(){};
	errorCallback 	= typeof errorCallback !== 'undefined' ? errorCallback : function(){};
 
 	cordova.exec(
		successCallback,
 		errorCallback,
 		"AlarmGap",
 		"stopAlarm",
 		[
 			alarmId
 		]
	);
};

AlarmGap.prototype.snoozeAlarm = function( alarmId, snoozeTimeInMillis, successCallback, errorCallback  ) {
 
 	successCallback = typeof successCallback !== 'undefined' ? successCallback : function(){};
	errorCallback 	= typeof errorCallback !== 'undefined' ? errorCallback : function(){};
 
 	cordova.exec(
		successCallback,
 		errorCallback,
 		"AlarmGap",
 		"snoozeAlarm",
 		[
 			alarmId,
 			snoozeTimeInMillis
 		]
	);
};


module.exports = new AlarmGap();