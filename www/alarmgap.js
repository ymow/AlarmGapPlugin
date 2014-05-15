var AlarmGap = function() {
};

AlarmGap.prototype.saveAlarm = function( alarmJsonObject, successCallback, errorCallback ) {
		
	successCallback = typeof successCallback !== 'undefined' ? successCallback : function(){};
	errorCallback 	= typeof errorCallback !== 'undefined' ? errorCallback : function(){};
 
 	cordova.exec(
		successCallback,
 		errorCallback,
 		"AlarmGap",
 		"saveAlarm",
 		[
 			alarmJsonObject
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