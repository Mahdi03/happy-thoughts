package com.aymanstudios.happythoughts;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;
import static com.aymanstudios.happythoughts.MainActivity.setTimeHour;
import static com.aymanstudios.happythoughts.MainActivity.setTimeMinute;

public class BootReceiver extends BroadcastReceiver {
    int setTimeHourSharedPreferences;
    int setTimeMinuteSharedPreferences;
    //Use Android built-in Shared Preferences feature to save variables even after reboot
    private SharedPreferences mPreferences;
    private String sharedPreferencesFile = "com.aymanstudios.happythoughts";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

            //Actually Define Shared Preferences File
            mPreferences = context.getSharedPreferences(sharedPreferencesFile, MODE_PRIVATE);

            //Usage: mPreferences.getInt("key name", default);
            setTimeHourSharedPreferences = mPreferences.getInt("setTimeHourSharedPreferences", setTimeHour);
            setTimeMinuteSharedPreferences = mPreferences.getInt("setTimeMinuteSharedPreferences", setTimeMinute);
            //Toast.makeText(context, "You have rebooted your phone, your Happy Thought of the day has been rescheduled at "  + String.valueOf(setTimeHourSharedPreferences) + ":" + String.valueOf(setTimeMinuteSharedPreferences), Toast.LENGTH_SHORT).show();
            //Set daily notifications again
            //Create a calendar object to convert the time into milliseconds
            Calendar getTime = Calendar.getInstance();
            Calendar setTime = (Calendar) getTime.clone();
            //setTime.setTimeInMillis(System.currentTimeMillis());
            setTime.set(Calendar.HOUR_OF_DAY, setTimeHourSharedPreferences);
            setTime.set(Calendar.MINUTE, setTimeMinuteSharedPreferences);
            setTime.set(Calendar.SECOND, 0);
            setTime.set(Calendar.MILLISECOND, 0);

            if (setTime.compareTo(getTime) <= 0) {
                setTime.add(Calendar.DATE, 1);
            }

            /*
        Create a new intent that is the MyNotificationPublisher.class
        What this does is it queues up to call the MyNotificationPublisher.java file
        to run the code inside that class
        */
            Intent notificationIntent = new Intent(context, MyNotificationPublisher.class);
            //Set an ID to transfer over to the MyNotificationPublisher.java
            notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION_ID, 1);
            /*
        This pending intent is an intent that is waiting to happen, meaning that it will
        run when scheduled. To this pending action we attach the new intent we just created
        for MyNotificationPublisher.java.
         */
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        /*
        Everything in MyNotificationPublisher.java will be repeated every single time the
        AlarmManager goes off. The AlarmManager is a simple function that runs a timer in
        the background and lets you call a predestined class (function) at a given time. In
        this case, we are repeating the call once a day at a user-selected time
        */
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            assert alarmManager != null;
        /*
        Change to AlarmManager.INTERVAL_FIFTEEN_MINUTES for testing purposes

        This function call wakes up the device every time the alarm sets off, and it calls our
        PendingIntent which is ultimately binded to our Intent that is binded to
        MyNotificationPublisher.java class
        The setInexactRepeating instead of exact repeating is for the device because
        the notifications aren't so urgent so although they will be triggered at the
        same time, they will show up when readily-available for the device
        */
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, setTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);        }
    }
}
