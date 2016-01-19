# CloudMonitor24 Android SDK
Android library for logging variable and alarms on CloudMonitor24 platform.<br>
It requires a valid CloudMonitor24 account.

### Getting started
Follow these few steps to start logging data on your Android App:<br>
* Sign up for a free account: [CloudMonitor24 Sign up](http://www.cloudmonitor24.com/en/iot/signup)
* [Sign in to your new account](https://iot.cloudmonitor24.com), enter Sensor Map area and get the credentials of a plant (identifier and token). Every plant is composed by many sensors, each one with a specific sensor Id. 
* Create an empty project with Android Studio or open your existing project
* Import .aar library into your project
* Import package on your main activity<br>
```
    import com.cloudmonitor24.sdk.AlarmIds;
    import com.cloudmonitor24.sdk.CloudMonitor24;
    import com.cloudmonitor24.sdk.VarIds;
```
* Get an instance of CloudMonitor24 object and start logging:<br>
```
    CloudMonitor24 logger = CloudMonitor24.createInstance( getApplicationContext(), "MyPlantIdentifier", "MyPlantToken" );
    logger.logVar( VarIds.VAR_ID_TEMPERATURE, 3.456f, __MySensorId );
    logger.logAlarm( AlarmIds.ALARM_ID_USER_ALARM, __MySensorId );
```
### APIs
This SDK is intended only for logging data. For further information about Rest APIs:<br>
[CloudMonitor24 Docs](http://www.cloudmonitor24.com/iot/docs)