AlarmGap
==============
An alarm phonegap plugin for Android (and later for iOS too)

> TODO I'm still working on the iOS platform. I don't think it is possible to have the custom HTML when the alarm goes on, but I'm still trying to find a good solution (I'm new to iOS development)

Index
------------
1. [How to use it](#how-to-use-it)
    1. [JSON Objects](#json-objects)
        1. [AlarmBean](#alarmbean)
        2. [NotificationBean](#notificationbean)
    2. [Methods](#methods)
        1. [Save alarm](#save-alarm) 
        2. [Delete alarm](#delete-alarm)
        3. [Stop alarm](#stop-alarm)
        4. [Snooze alarm](#snooze-alarm)


How to use it:
------------

With the methods below you can: save an alarm, delete it, stop it from ringing and snooze it to ring it again after some time.

###JSON Objects

First of all let's take a look in the AlarmBean and NotificationBean objects. These are JSON objects with all the needed data to identify and configure an alarm:

####AlarmBean
| Attribute Name | Type | Required | Description |
| ----           | ---- | ----     | ----        |
| alarmId | long | X | The unique identification number used by the host application |
| timeInMillis | long | X | The time in milliseconds when the alarm should be set |
| vibrate | boolean | - |  Set it true if the alarm should vibrate when ringing |
| htmlPath | string | X |  The path to the HTML file that will be loaded when the alarm rings. The path should be relative to the "www" folder |
| extra | string | - | Extra data that should be passed when the alarm goes on |
| notification | Object * | X | A NotificationBean JSON object with the data to run the notification when the alarm goes on |

* Observations:
  * iOS:
    * The iOS platform don't allow any call to an app screen when the "time comes". That's why we can't use the custom HTML file when the alarm goes on.
  * Android:
    * The HTML file should have a JavasScript function that will be called by the plugin when the alarm goes on:

```javascript
function alarmGapReceiveAlarm( _alarmJsonString ) {
  var alarmBean = jQuery.parseJSON( _alarmJsonString );
  
  // you should set the touch events for STOP and SNOOZE calls here
  
}
```

****

####NotificationBean

| Attribute Name | Type | Required | Description |
| ----           | ---- | ----     | ----        |
| title | string | X | The title text to be shown |
| message | string | X | The message text to be shown |
| smallIconName | string | X(Android) | The Resource name used as small icon for the notification: R.drawable.FILENAME |
| largeIconPath | string | - | The path to the image used as large icon for the notification. This should be relative to the "www" folder. |

* Observations:
  * Android
    * smallIconName - Android only. It should be the name of the file (without extension). This file should go in the android project path: "res/drawable/". If none is passed the default icon is used.
    * largeIconPath - Android only. If none is passwd the default icon is used.

****
###Methods

Now let's take a look at the plugin methods:

####Save alarm
Saves the alarm in the plugin database and set it on.
> On Android the alarm is keeped even when the device is rebooted

```javascript
navigator.alarmgap.saveAlarm ( alarmJsonObject );
navigator.alarmgap.saveAlarm ( alarmJsonObject, successCallback );
navigator.alarmgap.saveAlarm ( alarmJsonObject, successCallback, errorCallback );  
```
* Parameters
    * `alarmJsonObject` - the AlarmBean JSON object described above.
    * `successCallback` - (optional) success callback function.
    * `errorCallback` -  (optional) error callback function.

####Delete alarm
Deletes the alarm from the plugin database.

```javascript
navigator.alarmgap.deleteAlarm( alarmId );
navigator.alarmgap.deleteAlarm( alarmId, successCallback );
navigator.alarmgap.deleteAlarm( alarmId, successCallback, errorCallback );
```
* Parameters
    * `alarmId` - the alarmId used to save the alarm.
    * `successCallback` - (optional) success callback function.
    * `errorCallback` -  (optional) error callback function.

####Stop alarm
Stops the alarm when if it is ringing.

```javascript
navigator.alarmgap.stopAlarm( alarmId );
navigator.alarmgap.stopAlarm( alarmId, successCallback );
navigator.alarmgap.stopAlarm( alarmId, successCallback, errorCallback );
```
* Parameters
    * `alarmId` - the alarmId used to save the alarm.
    * `successCallback` - (optional) success callback function.
    * `errorCallback` - (optional) error callback function.

####Snooze alarm
Stops the alarm and set it to ring after `snoozeTimeInMillis` milliseconds later. The time from the AlarmBean is updated and all the other data are preserved.

```javascript
navigator.alarmgap.snoozeAlarm( alarmId, snoozeTimeInMillis );
navigator.alarmgap.snoozeAlarm( alarmId, snoozeTimeInMillis, successCallback, errorCallback );
navigator.alarmgap.snoozeAlarm( alarmId, snoozeTimeInMillis, successCallback, errorCallback );
```

* Parameters
    * `alarmId` - the alarmId used to save the alarm.
    * `snoozeTimeInMillis` - after X milliseconds the alarm goes on again
    * `successCallback` - (optional) success callback function.
    * `errorCallback` - (optional) error callback function.
