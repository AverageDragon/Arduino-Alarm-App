package com.example.alarmclock;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import android.content.pm.PackageManager;
import java.io.IOException;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    TimePicker alarmTime;
    ToggleButton weak;
    ToggleButton medium;
    ToggleButton strong;
    Button testButton;
    ToggleButton alarmOn;
    Switch bluetoothConnectedSwitch;
    static MainActivity inst;
    AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    MyBluetoothController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.SEND_SMS)
                            != PackageManager.PERMISSION_DENIED) {
                    }

                // The beginning of the custom code

                alarmTime = findViewById(R.id.alarmTime);
                weak = findViewById(R.id.weakToggle);
                medium = findViewById(R.id.mediumToggle);
                strong = findViewById(R.id.strongToggle);
                testButton = findViewById(R.id.testButton);
                alarmOn = findViewById(R.id.alarmOn);
                bluetoothConnectedSwitch = findViewById(R.id.bluetoothConnectedSwitch);
                alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                controller = new MyBluetoothController();

                weak.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            medium.setChecked(false);
                            strong.setChecked(false);
                            alarmOn.setChecked(false);
                        } else if (!medium.isChecked() && !strong.isChecked()) {
                            weak.setChecked(true);
                        }
                    }
                });
                medium.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            strong.setChecked(false);
                            weak.setChecked(false);
                            alarmOn.setChecked(false);
                        } else if (!strong.isChecked() && !weak.isChecked()) {
                            medium.setChecked(true);
                        }
                    }
                });
                strong.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            medium.setChecked(false);
                            weak.setChecked(false);
                            alarmOn.setChecked(false);
                        } else if (!medium.isChecked() && !weak.isChecked()) {
                            strong.setChecked(true);
                        }
                    }
                });
                alarmTime.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                    @Override
                    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                        alarmOn.setChecked(false);
                    }
                });

                testButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(),"Loading...",Toast.LENGTH_LONG).show();
                        if (weak.isChecked()) {
                            soundAlarm("2");
                        } else if (medium.isChecked()) {
                            soundAlarm("3");
                        } else {
                            soundAlarm("4");
                        }
                    }
                });

                alarmOn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        long time;
                        if (alarmOn.isChecked()) {
                            Toast.makeText(MainActivity.this, "ALARM ON", Toast.LENGTH_LONG).show();
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(Calendar.HOUR_OF_DAY, alarmTime.getCurrentHour());
                            calendar.set(Calendar.MINUTE, alarmTime.getCurrentMinute());
                            Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);

                            String alarmType = "";
                            if (weak.isChecked()) {
                                alarmType = "2";
                            } else if (medium.isChecked()) {
                                alarmType = "3";
                            } else {
                                alarmType = "4";
                            }
                            intent.putExtra("alarmType", alarmType);

                            pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
                            time=(calendar.getTimeInMillis()-(calendar.getTimeInMillis()%60000));
                            if(System.currentTimeMillis()>time)
                            {
                                if (calendar.AM_PM == 0)
                                    time = time + (1000*60*60*12);
                                else
                                    time = time + (1000*60*60*24);
                            }
                            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, 10000, pendingIntent);
                        } else {
                            alarmManager.cancel(pendingIntent);
                            Log.d("MyActivity", "Alarm Off");
                        }
                    }
                });

                bluetoothConnectedSwitch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(),"Loading...",Toast.LENGTH_LONG).show();
                        if (controller.activateFunction("0") && controller.activateFunction("6")) {
                            bluetoothConnectedSwitch.setChecked(true);
                            Toast.makeText(getApplicationContext(),"Connected!",Toast.LENGTH_LONG).show();
                        } else {
                            bluetoothConnectedSwitch.setChecked(false);
                            Toast.makeText(getApplicationContext(),"Device not found.",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }

    public static MainActivity instance() {
        return inst;
    }

    public void soundAlarm(String input) {
        Toast.makeText(getApplicationContext(),"Loading...",Toast.LENGTH_LONG).show();
        if (controller.activateFunction(input)) {
            Toast.makeText(getApplicationContext(),"Alarm sounded.",Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(),"Failed to sound, the device wasn't connected.",Toast.LENGTH_LONG).show();
        }
        alarmOn.setChecked(false);
    }
}