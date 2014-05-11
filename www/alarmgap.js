var AlarmGap = function() {
};

AlarmGap.prototype.saveAlarm = function( alarmId, timeInMillis, htmlPath, jsonObject, successCallback, errorCallback ) {
 
 	cordova.exec(
		successCallback,
 		errorCallback,
 		"AlarmGap",
 		"saveAlarm",
 		[
 			alarmId,
 			timeInMillis,
 			htmlPath,
 			jsonObject
 		]
	);
};


module.exports = new AlarmGap();