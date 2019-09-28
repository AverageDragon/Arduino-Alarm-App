package com.example.alarmclock;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

public class AlarmReceiver extends BroadcastReceiver {

    ToggleButton weak;
    ToggleButton medium;
    ToggleButton strong;
    ToggleButton alarmOn;
    MyBluetoothController controller;
    MainActivity inst;

    @Override
    public void onReceive(final Context context, Intent intent) {
        String alarmType = intent.getExtras().getString("alarmType");
        controller = new MyBluetoothController();
        controller.activateFunction(alarmType);
    }
}